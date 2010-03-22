/*
 * Copyright 2008 the original author or authors.
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
 * Defines a transformer whose operation may result in a loss of data precision.
 *
 * @author mbenson
 * @since Morph 1.1
 */
public interface ImpreciseTransformer extends Transformer {

	/**
	 * Learn whether the specified conversion might yield an imprecise result.
	 * 
	 * @param destinationClass
	 *            the destination type for the transformation
	 * @param sourceClass
	 *            the source type for the transformation
	 * @return boolean <code>true</code> if the transformation might yield an
	 *         imprecise result or<br>
	 *         <code>false</code>, otherwise
	 */
	boolean isImpreciseTransformation(Class destinationClass, Class sourceClass);

}
