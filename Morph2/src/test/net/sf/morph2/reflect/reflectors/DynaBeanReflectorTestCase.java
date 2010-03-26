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
package net.sf.morph2.reflect.reflectors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.morph2.reflect.BeanReflector;
import net.sf.morph2.reflect.ReflectionException;
import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.reflect.SizableReflector;
import net.sf.morph2.util.TestClass;
import net.sf.morph2.util.TestUtils;

import org.apache.commons.beanutils.WrapDynaBean;

/**
 * @author Matt Sgarlata
 * @since Dec 22, 2004
 */
public class DynaBeanReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new DynaBeanReflector();
	}

	protected List createImplicitPropertyNames() {
		// the BeanReflector.IMPLICIT_PROPERTY_CLASS is not included, because
		// DynaBeans sometimes include this property and there's not really
		// a way for us to distinguish between when the property was included
		// because of Reflection on an underlying object and when a DynaClass
		// was created that explicitly had a class property defined
		List list = new ArrayList();
		list.add(SizableReflector.IMPLICIT_PROPERTY_SIZE);
		list.add(BeanReflector.IMPLICIT_PROPERTY_PROPERTY_NAMES);
		list.add(DynaBeanReflector.IMPLICIT_PROPERTY_DYNA_CLASS);
		return list;
	}

	protected List createReflectableObjects() {
		List list = new ArrayList();
		list.add(new WrapDynaBean(new Object()));
		list.add(new WrapDynaBean(TestClass.getEmptyObject()));
		list.add(new WrapDynaBean(TestClass.getPartialObject()));
		list.add(new WrapDynaBean(TestClass.getFullObject()));
		return list;
	}

	protected List createNonReflectableObjects() {
		List beans = new ArrayList();
		beans.add(new ArrayList());
		beans.add(new HashSet());
		beans.add(new Object[0]);
		beans.add(new BigDecimal(3));
		beans.add(new Object());
		return beans;
	}

	protected void doTestReadableProperty(Object bean, String name)
		throws ReflectionException {
		// this is a hack to make this test case pass.  BeanUtils doesn't
		// support reading an indexed property as a whole, unlike Morph
		if (name.equals("funkyArray")) {
			return;
		}
		Object value = getBeanReflector().get(bean, name);
		value = TestUtils.getDifferentInstance(getBeanReflector().getType(bean, name), value);
		// if the property is writeable
		if (getBeanReflector().isWriteable(bean, name) &&
			!name.equals("class") &&
			!name.equals("size")) {
			// make sure the set method works
			getBeanReflector().set(bean, name, value);
		}
		else {
			// make sure the set method doesn't work
			try {
				getBeanReflector().set(bean, name, value);
				fail("The property shouldn't be writeable");
			}
			catch (ReflectionException e) { }
		}
	}
}
