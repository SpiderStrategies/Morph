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
package net.sf.morph.transform.transformers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.morph.transform.Converter;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.model.Address;
import net.sf.morph.transform.model.AddressImpl;
import net.sf.morph.transform.model.AddressTo;
import net.sf.morph.transform.model.Customer;
import net.sf.morph.transform.model.CustomerImpl;
import net.sf.morph.transform.model.Person;
import net.sf.morph.transform.model.PersonImpl;
import net.sf.morph.transform.model.PersonTo;

/**
 * 
 * @author Matt Sgarlata
 * @since Dec 2, 2005
 */
public class TypeChangingGraphTransformerTestCase extends TestCase {
	
	private static final String MATT_NAME = "Matt";
	private static final String MARK_NAME = "Mark";
	private static final String TOM_NAME = "Tom";
	private static final String MARK_ADDRESS = "789 Third St";
	private static final String MATT_ADDRESS = "456 Second St";
	private static final String TOM_ADDRESS = "123 First St";

	public TypeChangingGraphTransformer createTransformer() {
		Map sourceToDestinationTypeMapping = new HashMap();
		sourceToDestinationTypeMapping.put(Person.class, PersonTo.class);
		sourceToDestinationTypeMapping.put(Address.class, AddressTo.class);		
		
		TypeChangingGraphTransformer transformer = new TypeChangingGraphTransformer();
		transformer.setSourceToDestinationTypeMapping(sourceToDestinationTypeMapping);
		return transformer;
	}
	
	public void testConvert() {
		Person source = createMatt();
		Converter converter = (Converter) createTransformer();
		
		Object converted = converter.convert(PersonTo.class, source, null);
		performAssertions(converted);
		
		// verify that if the conversion is done a second time, the same result
		// is achieved
		Object converted2 = converter.convert(PersonTo.class, source, null);
		performAssertions(converted2);
		
		// verify that converted and converted2 are not the same instance
		assertFalse(converted == converted2);
	}
	
	public void testCopy() {
		Person source = createMatt();
		Copier copier = (Copier) createTransformer();
		
		Object copied = new PersonTo();
		copier.copy(copied, source, null);
		performAssertions(copied);
		
		// verify that if the copy is done a second time, the same result
		// is achieved
		copied = new PersonTo();
		copier.copy(copied, source, null);
		performAssertions(copied);
		
		// verify that if the copy is done a third time, the same result
		// is achieved.  this may seem like overkill, but the first time i
		// tried to fix this the 2nd copy worked but the 3rd didn't!
		copied = new PersonTo();
		copier.copy(copied, source, null);
		performAssertions(copied);
	}

	protected void performAssertions(Object transformed) {
		assertTrue(transformed instanceof PersonTo);
		PersonTo matt = (PersonTo) transformed;
		assertEquals(MATT_NAME, matt.getName());
		
		assertTrue(matt.getHomeAddress() instanceof AddressTo);
		AddressTo mattAddress = (AddressTo) matt.getHomeAddress();
		assertSame(matt, mattAddress.getPerson());
		assertEquals(MATT_ADDRESS, mattAddress.getText());
		
		assertEquals(2, matt.getChildren().size());
		assertTrue("Matt's first child should be of type PersonTo but instead it is of type "
			+ matt.getChildren().get(0).getClass(),
			matt.getChildren().get(0) instanceof PersonTo);
		assertTrue("Matt's second child should be of type PersonTo but instead it is of type "
			+ matt.getChildren().get(1).getClass(),
			matt.getChildren().get(1) instanceof PersonTo);
		
		PersonTo tom = (PersonTo) matt.getChildren().get(0); 
		assertEquals(TOM_NAME, tom.getName());
		assertTrue(tom.getHomeAddress() instanceof AddressTo);
		AddressTo tomAddress = (AddressTo) tom.getHomeAddress();
		assertSame(tom, tomAddress.getPerson());
		assertEquals(TOM_ADDRESS, tomAddress.getText());
		
		PersonTo mark = (PersonTo) matt.getChildren().get(1); 
		assertEquals(MARK_NAME, mark.getName());
		assertTrue(mark.getHomeAddress() instanceof AddressTo);
		AddressTo markAddress = (AddressTo) mark.getHomeAddress();
		assertSame(mark, markAddress.getPerson());
		assertEquals(MARK_ADDRESS, markAddress.getText());		
	}

	public Person createMatt() {
		Address tomAddress = new AddressImpl();
		tomAddress.setText(TOM_ADDRESS);
		Address mattAddress = new AddressImpl();
		mattAddress.setText(MATT_ADDRESS);
		Address markAddress = new AddressImpl();
		markAddress.setText(MARK_ADDRESS);
		
		Person tom = new PersonImpl();
		tom.setName(TOM_NAME);
		tom.setHomeAddress(tomAddress);
		tomAddress.setPerson(tom);
		
		Customer mark = new CustomerImpl();
		mark.setName(MARK_NAME);
		mark.setCustomerNumber("12354");
		mark.setHomeAddress(markAddress);
		markAddress.setPerson(mark);
		
		Customer matt = new CustomerImpl();
		matt.setName(MATT_NAME);
		matt.setCustomerNumber("522354");
		matt.setHomeAddress(mattAddress);
		mattAddress.setPerson(matt);
		List children = new ArrayList();
		children.add(tom);
		children.add(mark);
		matt.setChildren(children);
		
		return matt;
	}
	

}
