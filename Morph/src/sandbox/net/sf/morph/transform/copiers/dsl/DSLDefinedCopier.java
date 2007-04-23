/*
 * Copyright 2007 the original author or authors.
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
package net.sf.morph.transform.copiers.dsl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import antlr.ANTLRException;
import antlr.TokenBuffer;

import net.sf.morph.MorphException;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph.util.Assert;

/**
 * Provides a means to map a domain-specific language (DSL) based
 * representation of a Copier to an instance that will implement the specified
 * copy operations.  One or more class-copying associations may be defined, as:
 * <p>
 * <pre>my.package.LeftClass [<=|:|=>] my.package.RightClass {
 *     *, <em>//optional matchAllProperties indicator</em>
 *     propertyName, <em>//include this property (when not using *)</em>
 *     !propertyName, <em>//exclude this property when using *</em>
 *     leftProperty [<=|:|=>] rightProperty <em>//property mapping</em>
 * }
 * </pre>
 * The example text <code>[<=|:|=>]</code> indicated the three directional indicators:
 * right-to-left, bidirectional, and left-to-right, respectively.
 * </p>
 * Requires ANTLR 2 at runtime.
 *
 * @author Matt Benson
 */
public class DSLDefinedCopier extends SimpleDelegatingTransformer implements NodeCopier {
	private Class propertyMapClass;
	private InputStream inputStream;

	/**
	 * Construct a new DSLDefinedCopier.
	 */
	public DSLDefinedCopier() {
	}

	/**
	 * Construct a new DSLDefinedCopier.
	 * @param inputStream
	 */
	public DSLDefinedCopier(InputStream inputStream) {
		setInputStream(inputStream);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.SimpleDelegatingTransformer#createDefaultComponents()
	 */
	protected Transformer[] createDefaultComponents() {
		Assert.notNull(inputStream, "inputStream");
		ArrayList copierDefs = new ArrayList();
		try {
			MorphParser parser = new MorphParser(new TokenBuffer(new MorphLexer(
					inputStream)));
			copierDefs.addAll(parser.parse(this));
		} catch (ANTLRException e) {
			throw new MorphException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				//ignore
			}
		}
		ArrayList copiers = new ArrayList();
		for (Iterator iter = copierDefs.iterator(); iter.hasNext();) {
			copiers.addAll(((CopierDef) iter.next()).getCopiers(this));
		}
		return (Copier[]) copiers.toArray(new Copier[copiers.size()]);
	}

	/**
	 * Set the inputStream of this DSLDefinedCopier
	 * @param inputStream
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.SimpleDelegatingTransformer#createReusableSource(java.lang.Class, java.lang.Object)
	 */
	public Object createReusableSource(Class destinationClass, Object source) {
		return super.createReusableSource(destinationClass, source);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseCompositeTransformer#setNestedTransformer(net.sf.morph.transform.Transformer)
	 */
	public synchronized void setNestedTransformer(Transformer nestedTransformer) {
		super.setNestedTransformer(nestedTransformer);
	}

	/* (non-Javadoc)
	 * @see net.sf.morph.transform.transformers.BaseTransformer#getNestedTransformer()
	 */
	public synchronized Transformer getNestedTransformer() {
		return super.getNestedTransformer();
	}

	/**
	 * Get the Class propertyMapClass.
	 * @return Class
	 */
	public synchronized Class getPropertyMapClass() {
		if (propertyMapClass == null) {
			setPropertyMapClass(HashMap.class);
		}
		return propertyMapClass;
	}

	/**
	 * Set the Class propertyMapClass.
	 * @param propertyMapClass Class
	 */
	public synchronized void setPropertyMapClass(Class propertyMapClass) {
		this.propertyMapClass = propertyMapClass;
	}

}
