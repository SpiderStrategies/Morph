/*
 * Copyright 2007-2008 the original author or authors.
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
package net.sf.morph2.transform.copiers;

import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;

import net.sf.morph2.reflect.IndexedContainerReflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.DecoratedConverter;
import net.sf.morph2.transform.DecoratedCopier;
import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.TransformationType;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.util.ClassUtils;
import net.sf.morph2.util.TransformerUtils;

/**
 * A copier that copies a source to multiple destination objects, implementing a "Disassembler."
 *
 * @see http://www.martinfowler.com/eaaCatalog/dataTransferObject.html
 *
 * @author Matt Benson
 * @since Morph 1.1
 */
public class DisassemblerCopier extends AssemblyCopierSupport implements DecoratedCopier,
		DecoratedConverter {

	/**
	 * Adds the index to the source object to pass to the classConverter;
	 */
	public static class Disassembly {
		private Object source;
		private int index;

		private Disassembly(Object source) {
			this.source = source;
		}

		/**
		 * Get the source object
		 * @return Object
		 */
		public Object getSource() {
			return source;
		}

		/**
		 * Get the current index.
		 * @return int index
		 */
		public int getIndex() {
			return index;
		}

	}

	private static class DisassemblyIterator implements Iterator {
		private Disassembly disassembly;
		private int size;
		private int index;

		private DisassemblyIterator(Object source, int size) {
			this.disassembly = new Disassembly(source);
			this.size = size;
		}

		public synchronized boolean hasNext() {
			return index < size;
		}

		public synchronized Object next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			try {
				disassembly.index = index;
				return disassembly;
			} finally {
				index++;
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	private static final Class[] DEST_CLASS = new Class[] { Class.class };
	private static final Class[] SOURCE_DISASSEMBLY = new Class[] { Disassembly.class };

	private class DefaultClassConverter implements Converter {
		public Object convert(Class destinationClass, Object source, Locale locale)
				throws TransformationException {
			Disassembly disassembly = (Disassembly) source;
			Class[] destinationTypes = TransformerUtils.getDestinationClasses(
					getCopier(disassembly.getIndex()), ClassUtils.getClass(disassembly
							.getSource()));
			if (destinationTypes.length != 1) {
				throw new TransformationException(
						"Could not infer destination mapping for index "
								+ disassembly.getIndex());
			}
			return destinationTypes[0];
		}

		public Class[] getDestinationClasses() {
			return DEST_CLASS;
		}

		public Class[] getSourceClasses() {
			return SOURCE_DISASSEMBLY;
		}
	}

	private class DisassemblyContainerCopier extends ContainerCopier {
		/**
		 * Create a new DisassemblyContainerCopier.
		 */
		public DisassemblyContainerCopier() {
			setPreferGrow(false);
		}

		/**
		 * {@inheritDoc}
		 */
		protected Class determineDestinationContainedType(Object destination,
				Object sourceValue, Class containedValueClass, Locale locale) {
			try {
				return (Class) getClassConverter().convert(Class.class, sourceValue,
						locale);
			} catch (TransformationException e) {
				if (getComponents() == null) {
					//assume that destination was not empty; try to return type of existing element
					Class c = ClassUtils
							.getClass(((IndexedContainerReflector) getReflector(IndexedContainerReflector.class))
									.get(destination, ((Disassembly) sourceValue)
											.getIndex()));
					if (c != null) {
						return c;
					}
				}
				throw e;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		protected Object nestedTransform(Class destinationContainedType,
				Object destinationValue, Object sourceValue, Locale locale,
				TransformationType preferredTransformationType) {
			Disassembly d = (Disassembly) sourceValue;
			return TransformerUtils.transform(getCopier(d.getIndex()),
					destinationContainedType, destinationValue, d.getSource(), locale,
					preferredTransformationType);
		}

	}

	private Converter classConverter;
	private DisassemblyContainerCopier containerCopier = new DisassemblyContainerCopier();

	/**
	 * Create a new DisassemblerCopier.
	 */
	public DisassemblerCopier() {
		super();
	}

	/**
	 * Create a new DisassemblerCopier.
	 * @param components
	 */
	public DisassemblerCopier(Object[] components) {
		super(components);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#copyImpl(java.lang.Object, java.lang.Object, java.util.Locale, TransformationType)
	 */
	protected void copyImpl(Object destination, Object source, Locale locale,
			TransformationType preferredTransformationType) throws Exception {
		int size;
		if (getComponents() == null) {
			SizableReflector szr = (SizableReflector) getReflector(SizableReflector.class);
			size = szr.getSize(destination);
		}
		else {
			size = getComponents().length;
		}
		containerCopier.copy(destination, new DisassemblyIterator(source, size), locale);
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#getSourceClassesImpl()
	 */
	protected Class[] getSourceClassesImpl() throws Exception {
		Object[] components = getComponents();
		if (components == null) {
			return getNestedTransformer().getSourceClasses();
		}
		if (components.length == 0) {
			return new Class[0];
		}
		return TransformerUtils
				.getSourceClassIntersection((Transformer[]) getComponents());
	}

	/**
	 * {@inheritDoc}
	 * @see net.sf.morph2.transform.transformers.BaseTransformer#getDestinationClassesImpl()
	 */
	protected synchronized Class[] getDestinationClassesImpl() throws Exception {
		return containerCopier.getDestinationClasses();
	}

	/**
	 * Get the Converter used to convert a Disassembly object to a destination class.
	 * @return the classConverter
	 */
	public synchronized Converter getClassConverter() {
		if (classConverter == null) {
			setClassConverter(new DefaultClassConverter());
		}
		return classConverter;
	}

	/**
	 * Set the classConverter used to convert a Disassembly object to a destination class.
	 * @param classConverter the classConverter to set
	 */
	public synchronized void setClassConverter(Converter classConverter) {
		this.classConverter = classConverter;
	}

}
