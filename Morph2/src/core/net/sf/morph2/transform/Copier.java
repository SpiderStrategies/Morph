/*
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

import java.util.Locale;

/**
 * An object that can copy information from a source object to a destination
 * object.
 * 
 * @author Matt Sgarlata
 * @since October 31, 2004
 */
public interface Copier extends Transformer {

	/**
	 * <p>
	 * Copies information from the given source to the given destination.
	 * </p>
	 * 
	 * @param destination
	 *            the object to which information is written
	 * @param source
	 *            the object from which information is read
	 * @param locale
	 *            the locale of the current user, which may be null if the
	 *            locale is unknown or not applicable
	 * @throws TransformationException
	 *             if <code>source</code> or <code>destination</code> are
	 *             null or <br>
	 *             an error occurrs while copying
	 */
	public void copy(Object destination, Object source, Locale locale)
		throws TransformationException;

}
