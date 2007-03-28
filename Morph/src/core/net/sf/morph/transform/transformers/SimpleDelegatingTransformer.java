/*
 * Copyright 2004-2005, 2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package net.sf.morph.transform.transformers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.CompositeException;
import net.sf.composite.SpecializableComposite;
import net.sf.composite.specialize.Specializer;
import net.sf.composite.specialize.specializers.CachingSpecializerProxy;
import net.sf.composite.specialize.specializers.CloningSpecializer;
import net.sf.composite.util.ObjectPair;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.DefaultToBooleanConverter;
import net.sf.morph.transform.converters.DefaultToTextConverter;
import net.sf.morph.transform.converters.IdentityConverter;
import net.sf.morph.transform.converters.NullConverter;
import net.sf.morph.transform.converters.NumberConverter;
import net.sf.morph.transform.converters.NumberToTimeConverter;
import net.sf.morph.transform.converters.ObjectToClassConverter;
import net.sf.morph.transform.converters.TextConverter;
import net.sf.morph.transform.converters.TextToNumberConverter;
import net.sf.morph.transform.converters.TextToTimeConverter;
import net.sf.morph.transform.converters.TimeConverter;
import net.sf.morph.transform.converters.TimeToNumberConverter;
import net.sf.morph.transform.copiers.ContainerCopier;
import net.sf.morph.transform.copiers.PropertyNameMatchingCopier;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.Int;
import net.sf.morph.util.TransformerUtils;

/**
 * <p>
 * Delegates transformations to a list of transformers. The transformers are
 * tried in the order in which they appear in the <code>components</code>
 * property of this transformer.
 * </p>
 *
 * <p>
 * By default this transformer is initialized with a set of transformers that
 * will meet basic needs. This list of transformers is subject to change in
 * future versions of Morph, but we do not anticipate removing transformers,
 * only adding new ones or modifying existing ones. This means if a
 * transformation works in an older version of Morph it will also work in a
 * newer version. A transformation's behavior may in some cases change between
 * releases, but we will avoid this whenever possible.
 * </p>
 *
 * <p>
 * The default set of transformers includes both converters and copiers. For
 * calls to copy methods, only the copiers will be used. For calls to convert
 * methods, the converters <em>and</em> the copiers will be used, since all
 * Copiers provided by the Morph framework are also Converters.
 * </p>
 *
 * <p>
 * Any delegates which implement
 * {@link net.sf.morph.transform.NodeCopier} will automatically have this
 * transformer marked as the parent transformer.  This is important for
 * performing deep copies of object graphs.  Note that it is safe for a SimpleDelegatingTransformer
 * to implement the NodeCopier interface <em>only</em> if all its components do so.
 * </p>
 *
 * @author Matt Sgarlata
 * @since Dec 12, 2004
 */
public class SimpleDelegatingTransformer extends BaseCompositeTransformer implements
	SpecializableComposite, ExplicitTransformer, Transformer, DecoratedCopier, DecoratedConverter, Cloneable {

	private static class MapThreadLocal extends ThreadLocal {
		protected Object initialValue() {
			return new HashMap();
		}
	}

	private static class StackDepthThreadLocal extends ThreadLocal {
		protected Object initialValue() {
			return new Int();
		}
	}

	private static Transformer createObjectToMapCopier() {
		PropertyNameMatchingCopier result = new PropertyNameMatchingCopier();
		result.setDestinationClasses(new Class[] { Map.class });
		return result;
	}

	protected Transformer[] createDefaultComponents() {
		return new Transformer[] {
			new DefaultToBooleanConverter(),
			new NullConverter(),
			new IdentityConverter(),
			new ObjectToClassConverter(),
			new TextConverter(),
			new DefaultToTextConverter(),
			new TextToNumberConverter(),
			new TextToTimeConverter(),
			new NumberToTimeConverter(),
			new TimeToNumberConverter(),
			new NumberConverter(),
			new TimeConverter(),
			createObjectToMapCopier(),
			new ContainerCopier(),
			new PropertyNameMatchingCopier()
		};
	}

	private Specializer specializer;

	private transient ThreadLocal visitedSourceToDestinationMapThreadLocal = new MapThreadLocal();
	private transient ThreadLocal stackDepthThreadLocal = new StackDepthThreadLocal();

	private transient Map copierRegistry = Collections.synchronizedMap(new HashMap());
	private transient Map transformerRegistry = Collections.synchronizedMap(new HashMap());

	/**
	 * Construct a new SimpleDelegatingTransformer.
	 */
	public SimpleDelegatingTransformer() {
		super();
	}

	/**
	 * Construct a new SimpleDelegatingTransformer.
	 * @param components
	 */
	public SimpleDelegatingTransformer(Transformer[] components) {
		this(components, false);
	}

	/**
	 * Construct a new SimpleDelegatingTransformer.
	 * @param components
	 * @param appendDefaultComponents
	 */
	public SimpleDelegatingTransformer(Transformer[] components, boolean appendDefaultComponents) {
		if (appendDefaultComponents) {
			Transformer[] defaultComponents = createDefaultComponents();
			if (ObjectUtils.isEmpty(components)) {
				components = defaultComponents;
			}
			else {
				Transformer[] newComponents = (Transformer[]) ClassUtils.createArray(
						getComponentType(), components.length + defaultComponents.length);
				System.arraycopy(components, 0, newComponents, 0, components.length);
				System.arraycopy(defaultComponents, 0, newComponents, components.length, defaultComponents.length);
				components = newComponents;
			}
		}
		setComponents(components);
	}

	protected void initializeImpl() throws Exception {
		super.initializeImpl();
		if (getNestedTransformer() == null) {
			setNestedTransformer(this);
		}
	}

	/**
	 * Determines if one of the delegate transformers is capable of performing
	 * the given transformation. This method is necessary because otherwise the
	 * delegating transformer would be too eager in what it says it can convert.
	 * For example, if a delegate transformer was capable of transforming A to B
	 * and another delegate was capable of transforming C to D, this transformer
	 * would incorrectly state that transforming A to D and C to B was possible
	 * even though they are not.
	 *
	 * @param destinationType
	 *            the destination type to test
	 * @param sourceType
	 *            the source type to test
	 * @return whether the destination type is transformable to the source type
	 * @throws TransformationException
	 *             if it could not be determined if <code>sourceType</code> is
	 *             transformable into <code>destinationType</code>
	 */
	protected boolean isTransformableImpl(Class destinationType,
		Class sourceType) throws Exception {
		for (int i=0; i<getTransformers().length; i++) {
			Transformer transformer = getTransformers()[i];
			if (TransformerUtils.isTransformable(transformer,
				destinationType, sourceType)) {
				return true;
			}
		}
		return false;
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		Set sourceClasses = new HashSet();
		for (int i=0; i<getComponents().length; i++) {
			sourceClasses.addAll(Arrays.asList(getTransformers()[i].getSourceClasses()));
		}
		return (Class[]) sourceClasses.toArray(new Class[sourceClasses.size()]);
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		Set destinationClasses = new HashSet();
		for (int i=0; i<components.length; i++) {
			Transformer transformer = (Transformer) components[i];
			destinationClasses.addAll(Arrays.asList(transformer.getDestinationClasses()));
		}
		return (Class[]) destinationClasses.toArray(new Class[destinationClasses.size()]);
	}

	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType)
			throws Exception {
		incrementStackDepth();
		try {
			Class destinationType = ClassUtils.getClass(destination);
			Class sourceType = ClassUtils.getClass(source);
			if (!hasVisited(source, destinationType)) {
				Copier copier = getCopier(destinationType, sourceType);
				recordVisit(source, destinationType, destination);
				copier.copy(destination, source, locale);
			}
		} finally {
			decrementStackDepth();
			clearVisitedSourceToDestinationMapIfNecessary();
		}
	}

	protected Object convertImpl(Class destinationType, Object source, Locale locale)
			throws Exception {
		incrementStackDepth();
		try {
			Class sourceClass = ClassUtils.getClass(source);
			Transformer transformer = getTransformer(destinationType, sourceClass);

			if (hasVisited(source, destinationType)) {
				return getCachedResult(source, destinationType);
			}
			if (transformer instanceof NodeCopier) {
				NodeCopier nodeCopier = (NodeCopier) transformer;
				Object reuseableSource = nodeCopier.createReusableSource(destinationType, source);
				Object newInstance = nodeCopier.createNewInstance(destinationType,
						reuseableSource);
				recordVisit(source, destinationType, newInstance);
				nodeCopier.copy(newInstance, reuseableSource, locale);
				return newInstance;
			}
			Converter converter = (Converter) transformer;
			return converter.convert(destinationType, source, locale);
		} finally {
			decrementStackDepth();
			clearVisitedSourceToDestinationMapIfNecessary();
		}
	}

	protected void incrementStackDepth() {
		((Int) stackDepthThreadLocal.get()).value++;
	}

	protected void decrementStackDepth() {
		((Int) stackDepthThreadLocal.get()).value--;
	}

	protected void clearVisitedSourceToDestinationMapIfNecessary() {
		if (((Int) stackDepthThreadLocal.get()).value == 0) {
			getVisitedSourceToDestinationMap().clear();
		}
	}

	protected void recordVisit(Object source, Class destinationType, Object destination) {
		Object key = new ObjectPair(source, destinationType);
		getVisitedSourceToDestinationMap().put(key, destination);
	}

	protected boolean hasVisited(Object source, Class destinationType) {
		Object key = new ObjectPair(source, destinationType);
		return getVisitedSourceToDestinationMap().containsKey(key);
	}

	protected Object getCachedResult(Object source, Class destinationType) {
		Object key = new ObjectPair(source, destinationType);
		if (!getVisitedSourceToDestinationMap().containsKey(key)) {
			throw new IllegalArgumentException(
					"Cannot return a cached conversion result for "
							+ ObjectUtils.getObjectDescription(source)
							+ " to destination type '"
							+ destinationType
							+ "' because that conversion hasn't been performed before");
		}
		return getVisitedSourceToDestinationMap().get(key);
	}

	public Object specialize(Class compositeType) {
		return getSpecializer().specialize(this, compositeType);
	}

	public boolean isSpecializable(Class type) throws CompositeException {
		return getSpecializer().isSpecializable(this, type);
	}

	/**
	 * Finds a transformer of type <code>transformerType</code> that is
	 * capable of transforming <code>sourceClass</code> to
	 * <code>destinationClass</code>. Caches results in the
	 * <code>registry</code>.
	 *
	 * @param registry
	 *            a cache that remembers which transformers can be used for
	 *            which transformations
	 * @param transformerType
	 *            the type of the returned transformer
	 * @param destinationClass
	 *            the destinationClass of the transformation
	 * @param sourceClass
	 *            the sourceClass of the transformation
	 * @return the transformer of the requested type capable of performing the
	 *         requested transformation
	 * @throws TransformationException
	 *             if no suitable transformer could be found
	 */
	protected Transformer getTransformer(Map registry, Class transformerType, Class destinationClass, Class sourceClass) {
		ObjectPair key = new ObjectPair(destinationClass, sourceClass);
		Transformer transformer = (Transformer) registry.get(key);
		if (transformer == null) {
			transformer = getTransformer(transformerType, destinationClass, sourceClass);
			registry.put(key, transformer);
		}
		return transformer;
	}

	/**
	 * Finds a Copier that is capable of transforming <code>sourceClass</code>
	 * to <code>destinationClass</code>.
	 *
	 * @param destinationClass
	 *            the destinationClass of the transformation
	 * @param sourceClass
	 *            the sourceClass of the transformation
	 * @return the transformer of the requested type capable of performing the
	 *         requested transformation
	 * @throws TransformationException
	 *             if no suitable copier could be found
	 */
	protected Copier getCopier(Class destinationClass, Class sourceClass) {
		return (Copier) getTransformer(copierRegistry, Copier.class, destinationClass, sourceClass);
	}

	/**
	 * Finds a Transformer that is capable of transforming
	 * <code>sourceClass</code> to <code>destinationClass</code>.
	 *
	 * @param destinationClass
	 *            the destinationClass of the transformation
	 * @param sourceClass
	 *            the sourceClass of the transformation
	 * @return the transformer of the requested type capable of performing the
	 *         requested transformation
	 * @throws TransformationException
	 *             if no suitable transformer could be found
	 */
	protected Transformer getTransformer(Class destinationClass, Class sourceClass) {
		return getTransformer(transformerRegistry, Transformer.class, destinationClass, sourceClass);
	}

	/**
	 * Finds a transformer of type <code>transformerType</code> that is
	 * capable of transforming <code>sourceClass</code> to
	 * <code>destinationClass</code>.
	 *
	 * @param transformerType
	 *            the type of the returned transformer
	 * @param destinationClass
	 *            the destinationClass of the transformation
	 * @param sourceClass
	 *            the sourceClass of the transformation
	 * @return the transformer of the requested type capable of performing the
	 *         requested transformation
	 * @throws TransformationException
	 *             if no suitable transformer could be found
	 */
	private Transformer getTransformer(Class transformerType, Class destinationClass, Class sourceClass) throws TransformationException {
		for (int i=0; i<components.length; i++) {
			// if the transformer is the correct type
			Transformer transformer = (Transformer) components[i];
			if (transformerType.isAssignableFrom(transformer.getClass())) {
				// if the transformer is capable of performing the transformation
				if (TransformerUtils.isTransformable(
						transformer, destinationClass, sourceClass)) {
					if (getLog().isTraceEnabled()) {
						getLog().trace("Using "
							+ ClassUtils.getUnqualifiedClassName(transformerType)
							+ " " + transformer.getClass().getName()
							+ " to transform "
							+ ObjectUtils.getObjectDescription(sourceClass)
							+ " to "
							+ ObjectUtils.getObjectDescription(destinationClass));
					}
					return transformer;
				}
			}

		}

		throw new TransformationException(
			"Could not find a transformer that can transform objects of "
				+ ObjectUtils.getObjectDescription(sourceClass)
				+ " to objects of "
				+ ObjectUtils.getObjectDescription(destinationClass));
	}

	public Transformer[] getTransformers() {
		return (Transformer[]) getComponents();
	}

	public Object[] getComponents() {
		if (components == null) {
			setComponents(createDefaultComponents());
		}
		return super.getComponents();
	}

	public synchronized void setComponents(Object[] components) {
		if (this.components == components) {
			return;
		}
		this.components = components;
		transformerRegistry.clear();
		copierRegistry.clear();

		if (isInitialized() && components != null) {
			updateNestedTransformerComponents(getNestedTransformer(), null);
		}
	}

	/**
	 * Let the delegate do the logging
	 */
	protected boolean isPerformingLogging() {
		return false;
	}

	protected boolean isAutomaticallyHandlingNulls() {
		return false;
	}

	public Object clone() throws CloneNotSupportedException {
		SimpleDelegatingTransformer result = (SimpleDelegatingTransformer) super.clone();
		result.copierRegistry = Collections.synchronizedMap(new HashMap());
		result.transformerRegistry = Collections.synchronizedMap(new HashMap());
		result.visitedSourceToDestinationMapThreadLocal = new MapThreadLocal();
		result.stackDepthThreadLocal = new StackDepthThreadLocal();
		return result;
	}

	/**
	 * Gets a Map of all the nodes in the object graph that have been
	 * transformed so far by this transformer. The keys in the Map are the
	 * visited source nodes, and the values are the converted representation of
	 * the node.
	 *
	 * @return a Map of all the nodes in the object graph that have been
	 *         transformed so far by this transformer
	 */
	protected Map getVisitedSourceToDestinationMap() {
		return (Map) visitedSourceToDestinationMapThreadLocal.get();
	}

	public Specializer getSpecializer() {
		if (specializer == null) {
			specializer = new CachingSpecializerProxy(new CloningSpecializer());
		}
		return specializer;
	}

	public void setSpecializer(Specializer specializer) {
		this.specializer = specializer;
	}

	protected Object createReusableSource(Class destinationClass, Object source) {
		Transformer t = getTransformer(destinationClass, ClassUtils.getClass(source));
		return t instanceof NodeCopier ? ((NodeCopier) t).createReusableSource(
				destinationClass, source) : super.createReusableSource(destinationClass,
				source);
	}

}