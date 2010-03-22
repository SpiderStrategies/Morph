/*
 * Copyright 2004-2005, 2007-2008 the original author or authors.
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
package net.sf.morph2.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.CompositeUtils;
import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.Copier;
import net.sf.morph2.transform.ExplicitTransformer;
import net.sf.morph2.transform.ImpreciseTransformer;
import net.sf.morph2.transform.NestingAwareTransformer;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.Transformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility functions for implementing and using Transformers.
 *
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public abstract class TransformerUtils {

	private static abstract class ClassStrategy {
		abstract Class[] get(Transformer t);
	}

	private static final ClassStrategy SOURCE = new ClassStrategy() {
		Class[] get(Transformer t) {
			return t.getSourceClasses();
		}
	};

	private static final ClassStrategy DEST = new ClassStrategy() {
		Class[] get(Transformer t) {
			return t.getDestinationClasses();
		}
	};

	private static final Log log = LogFactory.getLog(TransformerUtils.class);
	private static final Class[] CLASS_NONE = new Class[0];

	/**
	 * Learn whether <code>sourceClass</code> is transformable to <code>destinationClass</code>
	 * by <code>transformer</code> considering only source and destination types.
	 * @param transformer
	 * @param destinationClass
	 * @param sourceClass
	 * @return boolean
	 */
	public static boolean isImplicitlyTransformable(Transformer transformer, Class destinationClass, Class sourceClass) {
		return ClassUtils.inheritanceContains(transformer.getDestinationClasses(), destinationClass)
			&& ClassUtils.inheritanceContains(transformer.getSourceClasses(), sourceClass);
	}

	/**
	 * Learn whether <code>sourceClass</code> is transformable to <code>destinationClass</code>
	 * by <code>transformer</code> by implicit or explicit rules.
	 * @param transformer
	 * @param destinationClass
	 * @param sourceClass
	 * @return boolean
	 * @see #isImplicitlyTransformable(Transformer, Class, Class)
	 * @see ExplicitTransformer
	 */
	public static boolean isTransformable(Transformer transformer, Class destinationClass, Class sourceClass) {
		if (transformer instanceof ExplicitTransformer) {
			return ((ExplicitTransformer) transformer).isTransformable(destinationClass, sourceClass);
		}
		return isImplicitlyTransformable(transformer, destinationClass, sourceClass);
	}

	/**
	 * Learn whether <code>transformer</code>'s transformation
	 * of <code>sourceClass</code> to <code>destinationClass</code> might yield an imprecise result.
	 * @param transformer
	 * @param destinationClass
	 * @param sourceClass
	 * @return boolean
	 * @see ImpreciseTransformer
	 * @since Morph 1.1
	 */
	public static boolean isImpreciseTransformation(Transformer transformer,
			Class destinationClass, Class sourceClass) {
		if (transformer instanceof ImpreciseTransformer) {
			return ((ImpreciseTransformer) transformer).isImpreciseTransformation(
					destinationClass, sourceClass);
		}
		return destinationClass == null && sourceClass != null;
	}

	/**
	 * Performs a transformation of one object graph into another object graph.
	 *
	 * @param destinationType
	 *            the type of the root node of the destination object graph
	 * @param the
	 *            optional root node of the destination object graph. If this
	 *            parameter is specified, it will be possible to copy
	 *            information to an existing object graph rather than requiring
	 *            a new object graph be created
	 * @param source
	 *            the root node of the source object graph
	 * @param locale
	 *            the locale in which any needed transformations should take
	 *            place
	 * @param preferredTransformationType
	 *            the preferred type of transformation to be performed
	 * @return the transformed object graph
	 * @throws TransformationException
	 *             if the graph could not be transformed for some reason
	 * @see Converter#TRANSFORMATION_TYPE_CONVERT
	 * @see Copier#TRANSFORMATION_TYPE_COPY
	 */
	public static Object transform(Transformer transformer, Class destinationType, Object destination,
		Object source, Locale locale, Integer preferredTransformationType)
		throws TransformationException {

		//default to preferredTransformationType if specified, else by Transformer type
		Integer xform = preferredTransformationType != null ? preferredTransformationType
				: transformer instanceof Copier ? Transformer.TRANSFORMATION_TYPE_COPY
						: Transformer.TRANSFORMATION_TYPE_CONVERT;

		boolean mutableDest = !ClassUtils.isImmutableObject(destination);

		// next, override impossible operations with possible ones
		// (this block is somewhat more verbose than necessary but
		// should be proof against possible additional Transformer types):
		if (Transformer.TRANSFORMATION_TYPE_COPY.equals(xform)) {
			if (!(transformer instanceof Copier && mutableDest) && transformer instanceof Converter) {
				xform = Transformer.TRANSFORMATION_TYPE_CONVERT;
			}
		}
		else if (Transformer.TRANSFORMATION_TYPE_CONVERT.equals(xform)) {
			if (!(transformer instanceof Converter) && transformer instanceof Copier
					&& mutableDest) {
				xform = Transformer.TRANSFORMATION_TYPE_COPY;
			}
		}

		Exception copyException = null;
		if (Transformer.TRANSFORMATION_TYPE_COPY.equals(xform)) {
			if (log.isTraceEnabled()) {
				log.trace("Performing nested copy of "
						+ ObjectUtils.getObjectDescription(source)
						+ " to destination "
						+ ObjectUtils.getObjectDescription(destination));
			}
			try {
				((Copier) transformer).copy(destination, source, locale);
				return destination;
			}
			catch (Exception e) {
				/* if copy fails, try to fall back to conversion. This can
				 * only happen if the choice to copy was externally specified,
				 * so we assume the failing transformation was nested,
				 * further evidenced by the fact that this method should
				 * usually be called by the framework itself. :)
				 */
				if (CompositeUtils.isSpecializable(transformer, Converter.class)) {
					transformer = (Transformer) CompositeUtils.specialize(transformer, Converter.class);
					//make sure the transformation we're looking for didn't fall out during specialization:
					if (TransformerUtils.isTransformable(transformer, destinationType, ClassUtils.getClass(source))) {
						if (log.isInfoEnabled()) {
							log.info("Trying to fall back on conversion due to copy failure", e);
						}
						xform = Transformer.TRANSFORMATION_TYPE_CONVERT;
						copyException = e;
					}
				}
				if (copyException != null) {
					throw e instanceof TransformationException ? (TransformationException) e
							: new TransformationException("Unable to perform graph transformation",
									e);
				}
			}
		}
		if (Transformer.TRANSFORMATION_TYPE_CONVERT.equals(xform)) {
			if (log.isTraceEnabled()) {
				log.trace("Performing nested conversion of "
					+ ObjectUtils.getObjectDescription(source)
					+ " to destination type "
					+ ObjectUtils.getObjectDescription(destinationType));
			}
			try {
				return ((Converter) transformer).convert(destinationType, source, locale);
			}
			catch (Exception e) {
				//if this was originally an attempted copy, throw the original exception:
				if (copyException != null) {
					e = copyException;
				}
				throw e instanceof TransformationException ? (TransformationException) e
						: new TransformationException("Unable to perform transformation", e);
			}
		}
		// shouldn't happen unless a new transformer type is introduced
		// and this class has not yet been updated to handle it
		throw new TransformationException("Unable to perform transformation using transformer "
				+ ObjectUtils.getObjectDescription(transformer));
	}

	/**
	 * Get the mapped destination type from the specified typemap.
	 * @param typeMapping Map
	 * @param requestedType Class
	 * @return Class
	 */
	public static Class getMappedDestinationType(Map typeMapping, Class requestedType) {
		if (typeMapping == null) {
			return null;
		}
		// see if the requested type has been directly mapped to some other type
		Class mappedDestinationType = (Class) typeMapping.get(requestedType);
		// see if the requested type has been indirectly mapped to some other type
		if (mappedDestinationType == null) {
			Set keys = typeMapping.keySet();
			for (Iterator i = keys.iterator(); i.hasNext();) {
				Class type = (Class) i.next();
				if (type.isAssignableFrom(requestedType)) {
					mappedDestinationType = (Class) typeMapping.get(type);
					break;
				}
			}
		}
		return mappedDestinationType;
	}

	/**
	 * Get the set of source classes available from the specified Transformer for the specified destination type.
	 * @param transformer
	 * @param destinationType
	 * @return Class[]
	 */
	public static Class[] getSourceClasses(Transformer transformer, Class destinationType) {
		if (!ClassUtils.inheritanceContains(transformer.getDestinationClasses(), destinationType)) {
			return CLASS_NONE;
		}
		Class[] sourceTypes = transformer.getSourceClasses();
		if (transformer instanceof ExplicitTransformer) {
			Set result = ContainerUtils.createOrderedSet();
			for (int i = 0; i < sourceTypes.length; i++) {
				if (((ExplicitTransformer) transformer).isTransformable(destinationType, sourceTypes[i])) {
					result.add(sourceTypes[i]);
				}
			}
			return result.isEmpty() ? CLASS_NONE : (Class[]) result.toArray(new Class[result.size()]);
		}
		return sourceTypes;
	}

	/**
	 * Get the set of destination classes available from the specified Transformer for the specified source type.
	 * @param transformer
	 * @param sourceType
	 * @return Class[]
	 * @since Morph 1.1
	 */
	public static Class[] getDestinationClasses(Transformer transformer, Class sourceType) {
		if (!ClassUtils.inheritanceContains(transformer.getSourceClasses(), sourceType)) {
			return CLASS_NONE;
		}
		Class[] destinationTypes = transformer.getDestinationClasses();
		if (transformer instanceof ExplicitTransformer) {
			Set result = ContainerUtils.createOrderedSet();
			for (int i = 0; i < destinationTypes.length; i++) {
				if (((ExplicitTransformer) transformer).isTransformable(destinationTypes[i], sourceType)) {
					result.add(destinationTypes[i]);
				}
			}
			return result.isEmpty() ? CLASS_NONE : (Class[]) result.toArray(new Class[result.size()]);
		}
		return destinationTypes;
	}

	/**
	 * Get the set of source classes common to all specified Transformers.
	 * @param transformers
	 * @return Class[]
	 */
	public static Class[] getSourceClassIntersection(Transformer[] transformers) {
		return getClassIntersection(transformers, SOURCE);
	}

	/**
	 * Get the set of destination classes common to all specified Transformers.
	 * @param transformers
	 * @return Class[]
	 */
	public static Class[] getDestinationClassIntersection(Transformer[] transformers) {
		return getClassIntersection(transformers, DEST);

	}

	/**
	 * If <code>nestingAware</code> implements {@link NestingAwareTransformer} and does not
	 * currently have a nested {@link Transformer} set, set <code>nestedTransformer</code>
	 * as its nested {@link Transformer}.
	 * @param nestingAware
	 * @param nestedTransformer
	 * @return whether a change was made
	 * @since Morph 1.1.2
	 */
	public static boolean setDefaultNestedTransformer(Transformer nestingAware,
			Transformer nestedTransformer) {
		return replaceNestedTransformer(nestingAware, null, nestedTransformer);
	}

	/**
	 * Replace <code>oldNestedTransformer</code> with <code>newNestedTransformer</code> as nested
	 * {@link Transformer} of <code>nestingAware</code> if <code>nestingAware</code> implements
	 * {@link NestingAwareTransformer}.
	 * @param nestingAware
	 * @param oldNestedTransformer
	 * @param newNestedTransformer
	 * @return whether a change was made
	 * @since Morph 1.1.2
	 */
	public static boolean replaceNestedTransformer(Transformer nestingAware,
			Transformer oldNestedTransformer, Transformer newNestedTransformer) {
		if (nestingAware instanceof NestingAwareTransformer) {
			NestingAwareTransformer nat = (NestingAwareTransformer) nestingAware;
			if (ObjectUtils.equals(nat.getNestedTransformer(), oldNestedTransformer)) {
				nat.setNestedTransformer(newNestedTransformer);
				return true;
			}
		}
		return false;
	}

	private static Class[] getClassIntersection(Transformer[] transformers, ClassStrategy strategy) {
		Set s = ContainerUtils.createOrderedSet();
		s.addAll(Arrays.asList(strategy.get(transformers[0])));

		for (int i = 1; i < transformers.length; i++) {
			Set survivors = ContainerUtils.createOrderedSet();
			Class[] c = strategy.get(transformers[i]);
			for (int j = 0; j < c.length; j++) {
				if (s.contains(c[j])) {
					survivors.add(c[j]);
					break;
				}
				if (c[j] == null) {
					break;
				}
				for (Iterator it = s.iterator(); it.hasNext();) {
					Class next = (Class) it.next();
					if (next != null && next.isAssignableFrom(c[j])) {
						survivors.add(c[j]);
						break;
					}
				}
			}
			if (!survivors.containsAll(s)) {
				for (Iterator it = s.iterator(); it.hasNext();) {
					Class next = (Class) it.next();
					if (survivors.contains(next) || next == null) {
						break;
					}
					for (int j = 0; j < c.length; j++) {
						if (c[j] != null && c[j].isAssignableFrom(next)) {
							survivors.add(next);
							break;
						}
					}
				}
			}
			s = survivors;
		}
		return s.isEmpty() ? CLASS_NONE : (Class[]) s.toArray(new Class[s.size()]);
	}

}
