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
package net.sf.morph2.reflect;

/**
 * <p>
 * An extension of the Reflector interface that defines extra methods. All
 * methods specified in this interface can be easily implemented using just the
 * methods in the Reflector interface. Thus, if you are defining your own
 * reflector you should implement only the Reflector interface. If you extend
 * from {@link net.sf.morph2.reflect.reflectors.BaseReflector}, your reflector
 * will implement this inteface automatically.
 * </p>
 * 
 * <p>
 * If you don't want to extend from <code>BaseReflector</code>, you can still
 * easily expose this interface by using the
 * {@link net.sf.morph2.reflect.reflectors.ReflectorDecorator}.
 * </p>
 * 
 * @author Matt Sgarlata
 * @since Dec 13, 2004
 */
public interface DecoratedReflector extends Reflector {

	/**
	 * Indicates whether the given type is reflectable by this reflector.
	 * 
	 * @param reflectedType
	 *            the type to test
	 * @return <code>true</code>, if this reflector can reflect the given
	 *         class or <br>
	 *         <code>false</code>, if this reflector cannot reflect the given
	 *         class
	 * @throws ReflectionException
	 *             if an error occurrs
	 */
	public boolean isReflectable(Class reflectedType)
		throws ReflectionException;

}
