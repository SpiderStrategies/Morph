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
import java.util.Hashtable;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sf.morph2.reflect.Reflector;
import net.sf.morph2.util.TestObjects;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Matt Sgarlata
 * @since Dec 21, 2004
 */
public class EnumerationReflectorTestCase extends BaseReflectorTestCase {

	protected Reflector createReflector() {
		return new EnumerationReflector();
	}

	protected List createReflectableObjects() {
		List list = new ArrayList();
		list.add((new TestObjects()).httpSession.getAttributeNames());
		list.add((new TestObjects()).pageContext.getAttributeNamesInScope(PageContext.PAGE_SCOPE));
		list.add((new TestObjects()).servletContext.getAttributeNames());
		list.add((new TestObjects()).servletRequest.getAttributeNames());
		list.add((new MockHttpServletRequest()).getAttributeNames());
		
		Hashtable h = new Hashtable();
		list.add(h.elements());
		
		h = (Hashtable) h.clone();
		h.put("something", "to test");
		list.add(h.elements());
		
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

}
