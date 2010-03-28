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
package net.sf.morph2.reflect.reflectors;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.TestObjects;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockPageContext;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class PageContextAttributeReflectorTestCase extends
	BaseServletReflectorTestCase {

	protected Reflector createReflector() {
		return new PageContextAttributeReflector();
	}

	protected List createReflectableObjects() {
		List list = new ArrayList();
		list.add(new MockPageContext());
		list.add((new TestObjects()).pageContext);
		return list;
	}

	protected List createNonReflectableObjects() {
		List list = new ArrayList();
		list.add(new MockHttpSession());
		list.add(new MockHttpServletResponse());
		list.add(new MockHttpServletRequest());
		list.add(new MockServletContext());
		return list;
	}

//	protected void setPropertyToRandomValue(Object bean, String propertyName)
//		throws Exception {
//		if (getReflector() instanceof BeanReflector) {
//			// make sure writeable properties are writeable and
//			// properties that aren't writeable aren't writeable
//			if (getBeanReflector().isWriteable(bean, propertyName)) {
//				Class type = getBeanReflector().getType(bean, propertyName);
//				if (type.isPrimitive()) {
//					getBeanReflector().set(bean, propertyName, TestUtils.getPrimitiveInstance(type));
//				}
//				else {
//					getBeanReflector().set(bean, propertyName, null);
//				}
//			}
//			else {
//				try {
//					getBeanReflector().set(bean, propertyName, null);
//					fail("The property shouldn't be writeable");
//				}
//				catch (ReflectionException e) { }
//			}
//		}
//	}
}
