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
package net.sf.morph.reflect.reflectors;

import net.sf.morph.reflect.Reflector;

/**
 * A wrapper for Reflectors that allows any reflector to implement 
 * {@link net.sf.morph.reflect.DecoratedReflector}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class ReflectorDecorator extends BaseReflector {//TODO finish this class!
	
	private Reflector reflector;
	
	public ReflectorDecorator(Reflector reflector) {
		this.reflector = reflector;
	}

	protected Class[] getReflectableClassesImpl() throws Exception {
		return reflector.getReflectableClasses();
	}

	protected Reflector getReflector() {
		return reflector;
	}
	protected void setReflector(Reflector reflector) {
		this.reflector = reflector;
	}
}
