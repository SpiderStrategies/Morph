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
package net.sf.morph2.transform;

/**
 * <p>
 * An extension of the Copier interface that defines a few more convenient
 * methods. All methods specified in this interface can be easily implemented
 * using just the methods in the Copier interface. Thus, if you are defining
 * your own copier you should implement the only Copier interface. If you extend
 * from {@link net.sf.morph2.transform.converters.BaseCopier}, your copier will
 * implement this inteface automatically. If you don't want to extend from
 * <code>BaseCopier</code>, you can easily expose this interface by using the
 * {@link net.sf.morph2.transform.copiers.CopierDecorator}.
 * </p>
 * 
 * <p>
 * <em>You should not directly implement this interface, because additional
 * methods may be introduced in later versions of Morph.  Instead, implement the
 * Copier interface and use the CopierDecorator to expose this interface.</em>
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 5, 2004
 */
public interface DecoratedCopier extends Copier, DecoratedTransformer {

	/**
	 * <p>
	 * Copies information from the given source to the given destination.
	 * </p>
	 * 
	 * @param destination
	 *            the object to which information is written
	 * @param source
	 *            the object from which information is read
	 * @throws TransformationException
	 *             if <code>source</code> or <code>destination</code> are
	 *             null
	 */
	public void copy(Object destination, Object source)
		throws TransformationException;	
	
}
