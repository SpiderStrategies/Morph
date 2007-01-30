/*
 * Copyright 2004-2005 the original author or authors.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.reflect.GrowableContainerReflector;
import net.sf.morph.reflect.MutableIndexedContainerReflector;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.DecoratedConverter;
import net.sf.morph.transform.DecoratedCopier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.support.ResetableIteratorWrapper;
import net.sf.morph.transform.transformers.BaseReflectorTransformer;
import net.sf.morph.util.ClassUtils;
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
public class ContainerCopier extends BaseReflectorTransformer implements Copier, DecoratedCopier, Converter, DecoratedConverter, NodeCopier {
	
	// map of Class to Class
	private Map containedSourceToDestinationTypeMap;
	
	public ContainerCopier() {
		super();
	}
	
	protected Class determineDestinationContainedType(Object destination, Class containedValueClass) {
		// determine the destinationType
		Class destinationType = null;
		
		// first, see if there is an explicitly registered mapping of contained
		// value classes to destination classes, and if so, use that.
		destinationType = TransformerUtils.getMappedDestinationType(
				containedSourceToDestinationTypeMap, containedValueClass);

		// if no such mapping is found
		if (destinationType == null) {
			Class candidateDestinationType =
				getContainerReflector().getContainedType(destination.getClass());
			// if the destination has a defined contained type than simply
			// Object.class (which basically just means untyped)
			if (!candidateDestinationType.equals(Object.class)) {
				// use that contained type as the destination type
				destinationType = candidateDestinationType;
			}
		}
		
		// if no mapping was found and the destination is untyped
		if (destinationType == null) {
			// choose the class of the source as the destination class			
			destinationType = containedValueClass;
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
			determineDestinationContainedType(destination, sourceValueClass);
		if (ClassUtils.isImmutable(destinationContainedType)) {
			preferredTransformationType = Converter.TRANSFORMATION_TYPE_CONVERT;
		}
				
		// if we can just add items to the end of the existing container
		if (ReflectorUtils.isReflectable(getReflector(),
			destination.getClass(), GrowableContainerReflector.class)) {
			
			// do a nested conversion so that we have a new instance called
			// convertedValue that we can ...
			Object convertedValue = TransformerUtils.transform(
				getNestedTransformer(), destinationContainedType, null,
				sourceValue, locale, Converter.TRANSFORMATION_TYPE_CONVERT);
			// ... add to the end of the existing container
			getGrowableContainerReflector().add(destination, convertedValue);
			
		}
		// else we are overwriting a value at a given index of the destination
		// container
		else if (ReflectorUtils.isReflectable(getReflector(),
			destination.getClass(), MutableIndexedContainerReflector.class)) {
			
			// we may want to do a copy or a convert, depending on the
			// capabilities of our graph transformer and whether a copy operation
			// is preferred.  this logic is implemented in the transformGraph
			// method
			Object destinationValue = getMutableIndexedContainerReflector().get(
				destination, index);
			Object transformedValue = TransformerUtils.transform(
				getNestedTransformer(), destinationContainedType,
				destinationValue, sourceValue, locale,
				preferredTransformationType);
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

	protected void copyImpl(Object destination, Object source, Locale locale, Integer preferredTransformationType)
		throws TransformationException {
		
// sgarlatam 4/18/2005: this check is taken care of in BaseTransformer
//		if (ReflectorUtils.isReflectable(getReflector(),
//			source.getClass(), ContainerReflector.class)) {
		
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
		
//		}
//		else {
//			throw new TransformationException(
//				"Could not copy from "
//					+ ObjectUtils.getObjectDescription(source)
//					+ " to "
//					+ ObjectUtils.getObjectDescription(destination)
//					+ " because no container reflector could be found for the source object");
//		}
	}
	
	public Object createReusableSource(Class destinationClass, Object source) {
		// if to array, get a resetable iterator over the source object:
		return destinationClass.isArray() ? new ResetableIteratorWrapper(
				getContainerReflector().getIterator(source))
				: super.createReusableSource(destinationClass, source);
	}
	
	protected Object createNewInstanceImpl(Class destinationClass, Object source) throws Exception {
		if (destinationClass.isArray()) {
			ResetableIteratorWrapper iterator = (ResetableIteratorWrapper) source;
			
			// determine the size of the source
			int size = 0;
			while (iterator.hasNext()) {
				iterator.next();
				size++;
			}
			iterator.reset();
			
			// create the destination array
			Class containedType = ClassUtils.getContainedClass(destinationClass);
			Object destination = ClassUtils.createArray(containedType, size);
			return destination;
		}
		return super.createNewInstanceImpl(destinationClass, source);
	}

	public Object createNewInstance(Class destinationClass, Object source) {
		return super.createNewInstance(destinationClass, source);
	}

	protected Class[] getDestinationClassesImpl() throws Exception {
		Set set = new HashSet();
		set.addAll(Arrays.asList(getGrowableContainerReflector().getReflectableClasses()));
		set.addAll(Arrays.asList(getIndexedContainerReflector().getReflectableClasses()));
		set.add(Iterator.class);
		set.add(Enumeration.class);
		return (Class[]) set.toArray(new Class[set.size()]);
	}

	protected Class[] getSourceClassesImpl() throws Exception {
		return getContainerReflector().getReflectableClasses();
	}

	public Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}
	public void setNestedTransformer(Transformer transformer) {
		super.setNestedTransformer(transformer);
	}

	public Map getContainedSourceToDestinationTypeMap() {
		return containedSourceToDestinationTypeMap;
	}
	public void setContainedSourceToDestinationTypeMap(
		Map containedSourceToDestinationMapping) {
		this.containedSourceToDestinationTypeMap = new TypeMap(containedSourceToDestinationMapping);
	}
}