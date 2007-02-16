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
package net.sf.morph.util;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.Transformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility functions for implementing and using Transformers.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public abstract class TransformerUtils {
	
	private static final Log log = LogFactory.getLog(TransformerUtils.class);
	
//	protected static boolean isAssignableFrom(Class[] candidates, Class type) {
//		for (int i = 0; i < candidates.length; i++) {
//			if (type == null) {
//				if (candidates[i] == null) {
//					return true;
//				}
//			}
//			else if (candidates[i] != null &&
//				candidates[i].isAssignableFrom(type)) {
//				return true;
//			}
//		}
//		
//		return false;
//	}
//	
	public static boolean isImplicitlyTransformable(Transformer transformer, Class destinationClass, Class sourceClass) {
		return
			ClassUtils.inheritanceContains(transformer.getDestinationClasses(), destinationClass) &&
			ClassUtils.inheritanceContains(transformer.getSourceClasses(), sourceClass);
	}
	
	public static boolean isTransformable(Transformer transformer, Class destinationClass, Class sourceClass) {
		if (transformer instanceof ExplicitTransformer) {
			return ((ExplicitTransformer) transformer).isTransformable(destinationClass, sourceClass);
		}
		return isImplicitlyTransformable(transformer, destinationClass, sourceClass);
	}
	
//	public static boolean isTransformable(Transformer transformer, Class destinationClass, Class sourceClass,
//		Class transformerType) throws CompositeException, ReflectionException {
//		if (transformerType == null) {
//			throw new ReflectionException("The transformerType must be specified");
//		}
//		
//		if (CompositeUtils.isSpecializable(transformer, transformerType)) {
//			Transformer transformerToUse = (Transformer) CompositeUtils.specialize(transformer, transformerType);
//			return TransformerUtils.isTransformable(transformerToUse, destinationClass, sourceClass);
//		}
//		else {
//			return false;
//		}
//	}
	
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
		
		int transformationType;
		
		// if the transformer is a Copier and no preference for transformation
		// type was specified or a preference for a copy operation was specified,
		// perform a copy operation
		if (// there is a non-null destination we can copy to
			destination != null &&
			// the transformer is a copier
			transformer instanceof Copier &&
			// the transformation type was not specified or a copy was requested
			(
				preferredTransformationType == null ||
				preferredTransformationType.equals(Copier.TRANSFORMATION_TYPE_COPY)
			)
		) {
			transformationType = Copier.TRANSFORMATION_TYPE_COPY.intValue();
		}		
		// otherwise, do a convert operation
		else {
			transformationType = Converter.TRANSFORMATION_TYPE_CONVERT.intValue();
		}
		
		if (transformationType == Converter.TRANSFORMATION_TYPE_CONVERT.intValue()) {
			if (log.isTraceEnabled()) {
				log.trace("Performing nested conversion of "
					+ ObjectUtils.getObjectDescription(source)
					+ " to destination type "
					+ ObjectUtils.getObjectDescription(destinationType));
			}
			try {
				return ((Converter) transformer).convert(destinationType, source, locale);
			}
			catch (TransformationException e) {
				throw e;
			}
			catch (Exception e) {
				throw new TransformationException("Unable to perform transformation", e);
			}
		}
		if (transformationType == Copier.TRANSFORMATION_TYPE_COPY.intValue()) {
			if (log.isTraceEnabled()) {
				log.trace("Performing nested copy of "
					+ ObjectUtils.getObjectDescription(source)
					+ " to destination "
					+ ObjectUtils.getObjectDescription(destination));
			}
			try {
				((Copier) transformer).copy(destination, source, locale);
			}
			catch (TransformationException e) {
				throw e;
			}
			catch (Exception e) {
				throw new TransformationException("Unable to perform graph transformation", e);
			}
			return destination;
		}
		// shouldn't happen unless a new transformer type is introduced
		// and this class has not yet been updated to handle it
		throw new TransformationException(
			"Unable to perform transformation using transformer "
				+ ObjectUtils.getObjectDescription(transformer));
	}

	public static Class getMappedDestinationType(Map typeMapping, Class requestedType) {
		if (typeMapping == null) {
			return null;
		}
		// see if the requested type has been directly mapped to some other type
		Class mappedDestinationType = (Class) typeMapping.get(requestedType);
		// see if the requested type has been indirectly mapped to some other type
		if (mappedDestinationType == null) {
			Set keys = typeMapping.keySet();
			for (Iterator i=keys.iterator(); i.hasNext(); ) {
				Class type = (Class) i.next();
				if (type.isAssignableFrom(requestedType)) {
					mappedDestinationType = (Class) typeMapping.get(type);
					break;
				}
			}
		}
		return mappedDestinationType;
	}

//	public Transformer getTransformer(Transformer[] transformers, Class destinationClass, Class sourceClass) {
//		for (int i=0; i<transformers.length; i++) {
//			if (TransformerUtils.isTransformable(transformers[i], destinationClass, sourceClass)) {
//				return transformers[i];
//			}
//		}
//		
//		return null;
//	}
	
//	public Object transform(Transformer transformer, Object destination, Object source, Locale locale) {
//		Object transformed = destination;
//		
//		if (transformer instanceof Copier) {
//			try {
//				((Copier) transformer).copy(destination, source, locale);				
//			}
//		}
//	}
	
//	public static Transformer getTransformerOfType(Transformer transformer,
//		Class type) throws TransformationException {
//		if (ObjectUtils.isEmpty(transformer)) {
//			throw new TransformationException("You must specify a transformer that can be searched for a transformer implementing type " + ObjectUtils.getObjectDescription(type));
//		}
//		if (type == null) {
//			throw new TransformationException("The type cannot be null");
//		}
//		Transformer[] transformers;
//		if (transformer instanceof CompositeTransformer) {
//			transformers = ((CompositeTransformer) transformer).getComponents();
//		}
//		else {
//			transformers = new Transformer[] { transformer };
//		}
//		List transformersOfType = new ArrayList();
//		for (int i=0; i<transformers.length; i++) {
//			if (type.isAssignableFrom(transformers[i].getClass())) {
//				transformersOfType.add(transformers[i]);
//			}
//		}
//		if (ObjectUtils.isEmpty(transformersOfType)) {
////			throw new TransformationException("No transformers of type " + ObjectUtils.getObjectDescription(type) + " could be found");
//			return null;
//		}
//		SimpleDelegatingTransformer transformerOfType = new SimpleDelegatingTransformer();
//		transformerOfType.setComponents((Transformer[]) transformersOfType.toArray(new Transformer[transformersOfType.size()]));
//		return transformerOfType;
//	}

	
}