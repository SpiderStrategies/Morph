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
package net.sf.morph.transform.copiers;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.IndexedContainerReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.support.ResetableIteratorWrapper;
import net.sf.morph.transform.transformers.BaseReflectorTransformer;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.IteratorEnumeration;
import net.sf.morph.util.ReflectorUtils;
import net.sf.morph.util.TransformerUtils;
import net.sf.morph.util.TypeMap;

/**
 * <p>
 * Copies information from any container object to any object that has either a
 * {@link GrowableContainerReflector}or a
 * {@link MutableIndexedContainerReflector}. If the source object has a
 * growable container reflector, information is added to the end of the
 * destination when a copy is performed. If the source object does not have a
 * growable container reflector (i.e. - it has a mutable indexed reflector),
 * information copied from the source to the destination will overwrite the
 * information in the destination.
 * </p>
 * 
 * <p>
 * By default, this means:
 * </p>
 * <table border="1">
 * <tr>
 * <th align="left">Destinations</th>
 * <th align="left">Sources</th>
 * </tr>
 * <tr>
 * <td>
 * java.util.List <br>
 * java.util.Set <br>
 * java.lang.Object[], int[], etc. (arrays)</td>
 * <td>
 * java.util.Iterator <br>
 * java.util.Enumeration <br>
 * java.util.Collection <br>
 * java.lang.Object[], int[], etc. (arrays) <br>
 * java.util.Map (values are extracted) <br>
 * java.lang.Object (added to the end of the destination) <br>
 * </td>
 * </tr>
 * </table>
 * 
 * @author Matt Sgarlata
 * @since Nov 27, 2004
 */
public class ContainerCopier extends BaseReflectorTransformer implements DecoratedCopier,
		DecoratedConverter, NodeCopier {

	// map of Class to Class
	private Map containedSourceToDestinationTypeMap;
	private boolean preferGrow = true;

	/**
	 * Create a new ContainerCopier.
	 */
	public ContainerCopier() {
		super();
	}

	/**
	 * Determine the container element destination type.  By default delegates to {@link #determineDestinationContainedType(Object, Class)}.
	 * @param destination container
	 * @param sourceValue source value
	 * @param sourceValueClass source type, or type source value would be if not null
	 * @return destination element type
	 */
	protected Class determineDestinationContainedType(Object destination,
			Object sourceValue, Class sourceValueClass, Locale locale) {
		return determineDestinationContainedType(destination, sourceValueClass);
	}

	/**
	 * Determine the container element destination type.
	 * @param destination container
	 * @param sourceValueClass source type
	 * @return destination element type
	 * @deprecated in favor of fully-specified method
	 */
	protected Class determineDestinationContainedType(Object destination,
			Class sourceValueClass) {
		// determine the destinationType
		Class destinationType = null;

		// first, see if there is an explicitly registered mapping of contained
		// value classes to destination classes, and if so, use that.
		destinationType = TransformerUtils.getMappedDestinationType(
				containedSourceToDestinationTypeMap, sourceValueClass);

		// if no such mapping is found
		if (destinationType == null) {
			Class candidateDestinationType = getContainerReflector().getContainedType(
					destination.getClass());
			// if the destination has a defined contained type than simply
			// Object.class (which basically just means untyped)
			if (candidateDestinationType != Object.class) {
				// use that contained type as the destination type
				destinationType = candidateDestinationType;
			}
		}

		if (destinationType == null) {
			//check whether nestedTransformer has only one possible destination for the given source:
			Class[] availableDestinationTypes = TransformerUtils.getDestinationClasses(getNestedTransformer(), sourceValueClass);
			if (availableDestinationTypes.length == 1 && availableDestinationTypes[0] != Object.class) {
				destinationType = availableDestinationTypes[0];
			}
		}
		// if no mapping was found and the destination is untyped
		if (destinationType == null) {
			// choose the class of the source as the destination class			
			destinationType = sourceValueClass;
		}
		return destinationType;
	}

	/**
	 * Adds an element to the destination object that is from the given index of
	 * the source object.
	 * 
	 * @param index
	 *            the current index into the container that is being transformed
	 * @param destination
	 *            the destination container to which values are being copied
	 *            from the source container
	 * @param sourceValue
	 *            the value the source object has at the current index
	 * @param sourceValueClass
	 *            the type of sourceValue, or the type the sourceValue would be
	 *            were it not <code>null</code>
	 * @param locale
	 *            the locale in which the current transformation is taking place
	 * @param preferredTransformationType
	 *            the preferred transformation type to perform when transforming
	 *            the sourceValue for addition into the destination
	 */
	protected void put(int index, Object destination, Object sourceValue, Class sourceValueClass, Locale locale, Integer preferredTransformationType) {
		Class destinationContainedType =
			determineDestinationContainedType(destination, sourceValue, sourceValueClass, locale);

		boolean canGrow = ReflectorUtils.isReflectable(getReflector(),
				destination.getClass(), GrowableContainerReflector.class);
		boolean canMutate = ReflectorUtils.isReflectable(getReflector(),
				destination.getClass(), MutableIndexedContainerReflector.class)
				&& getMutableIndexedContainerReflector().getSize(destination) > index;
		Integer xform = null;

		if (isPreferGrow() || TRANSFORMATION_TYPE_CONVERT.equals(preferredTransformationType)) {
			xform = canGrow ? TRANSFORMATION_TYPE_CONVERT
					: canMutate ? TRANSFORMATION_TYPE_COPY : null;
		}
		else if (TRANSFORMATION_TYPE_COPY.equals(preferredTransformationType)) {
			xform = canMutate ? TRANSFORMATION_TYPE_COPY
					: canGrow ? TRANSFORMATION_TYPE_CONVERT : null;
		}
		// if we can just add items to the end of the existing container
		if (TRANSFORMATION_TYPE_CONVERT.equals(xform)) {

			// do a nested conversion so that we have a new instance called
			// convertedValue that we can ...
			Object convertedValue = nestedTransform(destinationContainedType, null,
					sourceValue, locale, TRANSFORMATION_TYPE_CONVERT);
			// ... add to the end of the existing container
			getGrowableContainerReflector().add(destination, convertedValue);
		}
		// else we are overwriting a value at a given index of the destination
		// container
		else if (TRANSFORMATION_TYPE_COPY.equals(xform)) {
			// we may want to do a copy or a convert, depending on the
			// capabilities of our graph transformer and whether a copy
			// operation is preferred. this logic is implemented in the
			// TransformerUtils.transform method method
			Object destinationValue = getMutableIndexedContainerReflector().get(
				destination, index);
			Object transformedValue = nestedTransform(destinationContainedType,
					destinationValue, sourceValue, locale, preferredTransformationType);
			getMutableIndexedContainerReflector().set(destination, index,
				transformedValue);
		}
		else {
			// this shouldn't happen
			throw new TransformationException("Unable to copy value at index "
				+ index + " to the destination because "
				+ ObjectUtils.getObjectDescription(getReflector())
				+ ", the reflector specified for "
				+ ObjectUtils.getObjectDescription(this)
				+ ", cannot reflect destination "
				+ ObjectUtils.getObjectDescription(destination)
				+ " with a reflector that implements "
				+ GrowableContainerReflector.class.getName() + " or "
				+ MutableIndexedContainerReflector.class.getName());
		}
	}

	/**
	 * Do a nested transformation.
	 * @param destinationContainedType
	 * @param destinationValue
	 * @param sourceValue
	 * @param locale
	 * @param preferredTransformationType
	 * @return result
	 */
	protected Object nestedTransform(Class destinationContainedType,
			Object destinationValue, Object sourceValue, Locale locale,
			Integer preferredTransformationType) {
		return TransformerUtils.transform(getNestedTransformer(),
				destinationContainedType, destinationValue, sourceValue, locale,
				preferredTransformationType);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Object convertImpl(Class destinationClass, Object source, Locale locale) throws Exception {
		// The code here for Iterators and Enumerations is not quite 
		// as rigorous as it could be.  Being as rigorous as possible, we would
		// take into account the possibility of converting from one type of
		// Iterator to another type of Iterator or one type of Enumeration to
		// another type of Enumeration.  That's kind of silly though because
		// there aren't any stand-alone Iterator or Enumeration implementations
		// that come with the JDK.  Thus, if any Iterator or Iterator subclass
		// or Enumeration or Enumeration subclass is requested, we just return
		// whatever type is most readily available.
		boolean iter = Iterator.class.isAssignableFrom(destinationClass);
		if (iter || Enumeration.class.isAssignableFrom(destinationClass)) {
			// a newInstance call doesn't really make sense... just return the
			// final Iterator that will be returned to the user of the
			// ContainerCopier
			Iterator iterator = getContainerReflector().getIterator(source);
			return iter ? (Object) iterator : new IteratorEnumeration(iterator);
		}
		return super.convertImpl(destinationClass, source, locale);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType)
		throws TransformationException {
		// if the destination is an Iterator or Enumeration, we actually already
		// did all the required work in the createNewInstance method, so just
		// return
		if (destination instanceof Iterator ||
			destination instanceof Enumeration) {
			return;
		}
		int i = 0;
		Iterator sourceIterator = getContainerReflector().getIterator(source);
		while (sourceIterator.hasNext()) {
			Object sourceValue = sourceIterator.next();
			// determine the 
			Class sourceValueClass;
			if (sourceValue == null) {
				sourceValueClass = getContainerReflector().getContainedType(
					source.getClass());				
			}
			else {
				sourceValueClass = sourceValue.getClass();
			}
			put(i++, destination, sourceValue, sourceValueClass, locale,
				preferredTransformationType);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object createReusableSource(Class destinationClass, Object source) {
		// if to array, get a resetable iterator over the source object:
		return destinationClass.isArray() ? new ResetableIteratorWrapper(
				getContainerReflector().getIterator(source))
				: super.createReusableSource(destinationClass, source);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getDestinationClassesImpl() throws Exception {
		Set set = ContainerUtils.createOrderedSet();
		if (hasReflector(GrowableContainerReflector.class)) {
			set.addAll(Arrays.asList(getGrowableContainerReflector().getReflectableClasses()));
		}
		if (hasReflector(IndexedContainerReflector.class)) {
			set.addAll(Arrays.asList(getIndexedContainerReflector().getReflectableClasses()));
		}
		set.add(Iterator.class);
		set.add(Enumeration.class);
		return (Class[]) set.toArray(new Class[set.size()]);
	}

	/**
	 * {@inheritDoc}
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		return getContainerReflector().getReflectableClasses();
	}

	/**
	 * {@inheritDoc}
	 */
	protected boolean isWrappingRuntimeExceptions() {
		// this transformer can recursively call other transformers, so we don't
		// want to eat user defined exceptions
	    return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}

	/**
	 * {@inheritDoc}
	 */
	public void setNestedTransformer(Transformer transformer) {
		super.setNestedTransformer(transformer);
	}

	/**
	 * Get the mapping of source to destination container element types.
	 * @return Map
	 * @see {@link net.sf.morph.util.TypeMap}
	 */
	public Map getContainedSourceToDestinationTypeMap() {
		return containedSourceToDestinationTypeMap;
	}

	/**
	 * Set the mapping of source to destination container element types.
	 * @param containedSourceToDestinationMapping Map
	 * @see {@link net.sf.morph.util.TypeMap}
	 */
	public void setContainedSourceToDestinationTypeMap(
		Map containedSourceToDestinationMapping) {
		this.containedSourceToDestinationTypeMap = new TypeMap(containedSourceToDestinationMapping);
	}

	/**
	 * Learn whether this ContainerCopier prefers to grow the destination when possible.
	 * @return boolean
	 * @since Morph 1.1
	 */
	public boolean isPreferGrow() {
		return preferGrow;
	}

	/**
	 * Set whether this ContainerCopier prefers to grow the destination when possible.
	 * Backward-compatible default setting is <code>true</code>; when <code>false</code>
	 * grow vs. mutate will be determined at runtime by the capabilities of the configured
	 * reflector with regard to the copy destination; preferredTransformationType will also
	 * be observed.
	 * @see #copyImpl(Object, Object, Locale, Integer)
	 * @since Morph 1.1
	 * @param preferGrow the boolean to set
	 */
	public void setPreferGrow(boolean preferGrow) {
		this.preferGrow = preferGrow;
	}
}