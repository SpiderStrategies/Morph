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
package net.sf.morph.transform.converters;

import java.util.List;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.transform.ExplicitTransformer;
import net.sf.morph.transform.TransformationException;
import net.sf.morph.transform.transformers.BaseTransformerTestCase;
import net.sf.morph.util.ClassUtils;
import net.sf.morph.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Oct 25, 2004
 */
public abstract class BaseConverterTestCase extends BaseTransformerTestCase {

	protected Object[] listToArray(Class componentType, List list) {
		int size = ObjectUtils.isEmpty(list) ? 0 : list.size();
		Object[] array = (Object[]) ClassUtils.createArray(componentType, size);
		return size == 0 ? array : list.toArray(array);
	}

	protected void setUp() throws Exception {
		super.setUp();		
		invalidDestinationClasses = (Class[]) listToArray(Class.class, createInvalidDestinationClasses());
		invalidPairs = (ConvertedSourcePair[]) listToArray(ConvertedSourcePair.class, createInvalidPairs());
		invalidSources = listToArray(Object.class, createInvalidSources());
		invalidSourceClasses = (Class[]) listToArray(Class.class, createInvalidSourceClasses());
		validPairs = (ConvertedSourcePair[]) listToArray(ConvertedSourcePair.class, createValidPairs());
		destinationClasses = (Class[]) listToArray(Class.class, createDestinationClasses());
	}

	private Class[] invalidSourceClasses;
	
	private Class[] invalidDestinationClasses;	

	private ConvertedSourcePair[] invalidPairs;

	private Object[] invalidSources;

	private ConvertedSourcePair[] validPairs;

	private Class[] destinationClasses;

	public BaseConverterTestCase() {
	}

	public BaseConverterTestCase(String arg0) {
		super(arg0);
	}

// TESTS THAT DEPEND ONLY ON THE getConverter

//	 actually, we have a nullconverter that has the job of converting null to null			
//	public void testConvertArgumentChecking() {
//		if (getTransformer() instanceof Converter) {
//			try {
//				getConverter().convert(null, null, null);
//				fail("convert(null, <anything here>, <anything here>) should " +
//					"throw a TransformationException");
//			}
//			catch (TransformationException e) { }
//			try {
//				getConverter().convert(null, new Object(), null);
//				fail("convert(null, <anything here>, <anything here>) should throw a "
//					+ "TransformationException");
//			}
//			catch (TransformationException e) { }
//		}
//	}
	
// TESTS THAT DEPEND ON MULTIPLE INSTANCE VARIABLES

	public void testIsTransformable() {
		if (getTransformer() instanceof ExplicitTransformer) {
			ExplicitTransformer transformer =
				(ExplicitTransformer) getTransformer();
			
			// invalidDestinationClasses
			if (getInvalidDestinationClasses() != null) {
				for (int i = 0; i < getInvalidDestinationClasses().length; i++) {
					boolean isConvertible = transformer.isTransformable(
						getInvalidDestinationClasses()[i], Object.class);
					assertFalse("should not be able to convert " + Object.class + " to "
						+ ObjectUtils.getObjectDescription(getInvalidDestinationClasses()[i]), isConvertible);
				}
			}
		}
	}

//	public void testConvert() {
//		Object converted = null;
//
//		// invalidPairs
//		if (getInvalidPairs() != null) {
//			for (int i = 0; i < getInvalidPairs().length; i++) {
//				for (int j=0; j < getDestinationClasses().length; j++) {
//					Object source = getInvalidPairs()[i].getSource();
//					// set converted to source
//					converted = source;
//					try {
//						converted = getConverter().convert(getDestinationClasses()[j],
//							source, null);
//					} catch (TransformationException e) {
//						// a TransformationException indicates an invalid conversion was
//						// attempted... that's a good thing in this case :)
//						break;
//					}
//					assertFalse(
//						ObjectUtils.getObjectDescription(source) + " was converted to "
//							+ ObjectUtils.getObjectDescription(converted)
//							+ " but this should",
//						TestUtils.equals(getInvalidPairs()[i].getConverted(), converted));
//				}
//			}
//		}
//
//		// validPairs
//		if (getValidPairs() != null) {
//			for (int i = 0; i < getValidPairs().length; i++) {
//				for (int j=0; j<getDestinationClasses().length; j++) {
//					converted = getConverter().convert(getDestinationClasses()[j],
//						getValidPairs()[i].getSource(), null);
//					assertTrue(
//						ObjectUtils.getObjectDescription(getValidPairs()[i].getSource())
//							+ " was converted to "
//							+ ObjectUtils.getObjectDescription(converted)
//							+ " but should have been converted to "
//							+ ObjectUtils.getObjectDescription(getValidPairs()[i].getConverted()),
//							TestUtils.equals(
//							getValidPairs()[i].getConverted(), converted));
//				}
//			}
//		}
//	}
	
	public void testValidPairs() {
		Object converted;
		// validPairs
		if (getValidPairs() != null) {
			for (int i = 0; i < getValidPairs().length; i++) {
				Object correctConverted = getValidPairs()[i].getConverted();
				Object source = getValidPairs()[i].getSource();
				converted = getConverter().convert(ClassUtils.getClass(correctConverted),
					source, null);
				assertTrue(
					ObjectUtils.getObjectDescription(source)
						+ " was converted to "
						+ ObjectUtils.getObjectDescription(converted)
						+ " but should have been converted to "
						+ ObjectUtils.getObjectDescription(correctConverted),
						TestUtils.equals(correctConverted, converted));
			}
		}
	}
	
//	public void testValidPairs2() {
//		Object converted;
//		if (getValidPairs() != null) {
//			for (int i = 0; i < getValidPairs().length; i++) {
//				for (int j=0; j<getDestinationClasses().length; j++) {
//					converted = getConverter().convert(getDestinationClasses()[j],
//						getValidPairs()[i].getSource(), null);
//					assertTrue(
//						ObjectUtils.getObjectDescription(getValidPairs()[i].getSource())
//							+ " was converted to "
//							+ ObjectUtils.getObjectDescription(converted)
//							+ " but should have been converted to "
//							+ ObjectUtils.getObjectDescription(getValidPairs()[i].getConverted()),
//							TestUtils.equals(
//							getValidPairs()[i].getConverted(), converted));
//				}
//			}
//		}
//	}

	public void testInvalidPairs() {
		Object converted;
		// invalidPairs
		if (getInvalidPairs() != null) {
			for (int i = 0; i < getInvalidPairs().length; i++) {
				Object source = getInvalidPairs()[i].getSource();
				// set converted to source
				converted = source;
				try {
					converted = getConverter().convert(getInvalidPairs()[i].getConverted().getClass(),
						source, null);
				} catch (TransformationException e) {
					// a TransformationException indicates an invalid conversion was
					// attempted... that's a good thing in this case :)
					break;
				}
				assertFalse(
					ObjectUtils.getObjectDescription(source) + " was converted to "
						+ ObjectUtils.getObjectDescription(converted)
						+ " but that should have been an invalid conversion ",
					TestUtils.equals(getInvalidPairs()[i].getConverted(), converted));
			}
		}
	}

	/**
	 * Verifies that conversions fail with the supplied list of invalid
	 * source objects.  Note that most transformers don't test their own
	 * isTransformable method before attempting to perform a confusion for
	 * performance reasons.  This means a transformer may actually be able to
	 * handle inputs that it wasn't originally designed to handle.  In this
	 * case, it's recommended to just reduce the supplied list of source classes
	 * than worry about why extra inputs are allowed.
	 *
	 */
	public void testInvalidSources() {
		// invalidSources
		if (getInvalidSources() != null) {
			for (int i = 0; i < getInvalidSources().length; i++) {
				for (int j=0; j < getDestinationClasses().length; j++) {
					try {
						// use local variables for ease of debugging
						Class destinationClass = getDestinationClasses()[j];
						Object source = getInvalidSources()[i];
						getConverter().convert(destinationClass,
							source, null);
						fail("convert(" + destinationClass.getName() + ", "
							+ ObjectUtils.getObjectDescription(source)
							+ ") should throw a TransformationException");
					} catch (TransformationException e) {
					}
				}
			}
		}
	}
	
	

	public void testGetSourceClasses() {
		super.testGetSourceClasses();
		if (invalidSourceClasses != null) {
			for (int i=0; i<invalidSourceClasses.length; i++) {
				Class[] typeArray = getTransformer().getSourceClasses();
				Class sourceType = invalidSourceClasses[i];
				boolean result = ClassUtils.inheritanceContains(typeArray,
					sourceType);
				String message =
					ObjectUtils.getObjectDescription(invalidSourceClasses) +
					" is a subclass of one of the source classes for the transformer, but it should not be.  The source classes of the transformer are " + 
					ObjectUtils.getObjectDescription(getTransformer().getSourceClasses());
				assertFalse(message, result);
			}
		}
	}
	
	public void runAllTests() {
//		this.testConvertArgumentChecking();
		this.testIsTransformable();
		this.testValidPairs();
//		this.testValidPairs2();
		this.testInvalidPairs();
		this.testInvalidSources();
		this.testGetSourceClasses();
		this.testGetDestinationClasses();
	}

	public class ConvertedSourcePair {
		private Object source;

		private Object converted;

		public ConvertedSourcePair() { }
		
		public ConvertedSourcePair(Object converted, Object source) {
			this.source = source;
			this.converted = converted;
		}

		public Object getConverted() {
			return converted;
		}

		public void setConverted(Object converted) {
			this.converted = converted;
		}

		public Object getSource() {
			return source;
		}

		public void setSource(Object source) {
			this.source = source;
		}

		public ConvertedSourcePair invert() {
			return new ConvertedSourcePair(source, converted);
		}
	}

	public abstract List createInvalidDestinationClasses() throws Exception;

	public List createInvalidPairs() throws Exception {
		return null;
	}

	public abstract List createInvalidSources() throws Exception;
	
	public List createInvalidSourceClasses() throws Exception {
		return null;
	}

	public abstract List createValidPairs() throws Exception;

	public abstract List createDestinationClasses() throws Exception;

	public Class[] getDestinationClasses() {
		return destinationClasses;
	}

	public void setDestinationClasses(Class[] destinationClass) {
		this.destinationClasses = destinationClass;
	}

	public Class[] getInvalidDestinationClasses() {
		return invalidDestinationClasses;
	}

	public void setInvalidDestinationClasses(Class[] invalidDestinationClasses) {
		this.invalidDestinationClasses = invalidDestinationClasses;
	}

	public ConvertedSourcePair[] getInvalidPairs() {
		return invalidPairs;
	}

	public void setInvalidPairs(ConvertedSourcePair[] invalidPairs) {
		this.invalidPairs = invalidPairs;
	}

	public Object[] getInvalidSources() {
		return invalidSources;
	}

	public void setInvalidSources(Object[] invalidSources) {
		this.invalidSources = invalidSources;
	}

	public ConvertedSourcePair[] getValidPairs() {
		return validPairs;
	}

	public void setValidPairs(ConvertedSourcePair[] validPairs) {
		this.validPairs = validPairs;
	}
}