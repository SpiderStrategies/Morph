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
 * A <code>Transformer</code> that explicitly states which of its source
 * classes are transformable to which destination classes. In most cases, you
 * can implicitly assume that a <code>Transformer</code> can transform all
 * sources to all destinations. However, if the <code>Transformer</code>
 * implements this interface, that is not the case.
 * 
 * @author Matt Sgarlata
 * @since Nov 26, 2004
 */
public interface ExplicitTransformer {
	
	/**
	 * Specifies which source classes are transformable to which destination
	 * classes.
	 * 
	 * @param destinationType
	 *            the destination type to test
	 * @param sourceType
	 *            the source type to test
	 * @return whether the destination type is transformable to the source
	 *         type
	 * @throws TransformationException
	 *             if it could not be determined if <code>sourceType</code>
	 *             is transformable into <code>destinationType</code>
	 */
	public boolean isTransformable(Class destinationType, Class sourceType)
		throws TransformationException;

}
