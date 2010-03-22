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
package net.sf.morph2.examples.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.DefaultToBooleanConverter;
import net.sf.morph2.transform.converters.DefaultToTextConverter;
import net.sf.morph2.transform.converters.IdentityConverter;
import net.sf.morph2.transform.converters.NumberConverter;
import net.sf.morph2.transform.converters.TextConverter;
import net.sf.morph2.transform.converters.TextToNumberConverter;
import net.sf.morph2.transform.copiers.ContainerCopier;
import net.sf.morph2.transform.copiers.PropertyNameMappingCopier;
import net.sf.morph2.transform.copiers.PropertyNameMatchingCopier;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;
import net.sf.morph2.util.TestUtils;

/**
 * @author Matt Sgarlata
 * @since Apr 12, 2005
 */
public class PersonExampleTestCase extends TestCase {

	private PersonDAO johnSmith;
	
	private SimpleDelegatingTransformer graphTransformer;
	
	protected void setUp() throws Exception {
		setUpJohnSmith();
		setUpTransformer();
	}
	
	protected void setUpJohnSmith() {
		PersonDAO matthew = new PersonDAO();
		matthew.setFirstName("Matthew");
		
		PersonDAO natalie = new PersonDAO();
		natalie.setFirstName("Natalie");
		
		AddressDAO home = new AddressDAO();
		home.setName("Home");
		home.setStreetAddress("1 Home St");
		
		AddressDAO work = new AddressDAO();
		work.setName("Work");
		work.setStreetAddress("2 Work Circle");
		
		VehicleDAO fordTaurus = new VehicleDAO();
		fordTaurus.setManufacturer("Ford");
		fordTaurus.setModel("Taurus");
		
		VehicleDAO hondaCivic = new VehicleDAO();
		hondaCivic.setManufacturer("Honda");
		hondaCivic.setModel("Civic");		
		
		johnSmith = new PersonDAO();
		johnSmith.setFirstName("John");
		johnSmith.setMiddleName("A.");
		johnSmith.setLastName("Smith");
		johnSmith.setCreditCardNumber("5555555555555555");		
		johnSmith.setChildren(new PersonDAO[] { matthew, natalie });
		johnSmith.setAddresses(new AddressDAO[] { home, work });
		johnSmith.setVehicles(new VehicleDAO[] { fordTaurus, hondaCivic });
	}
	
	protected void setUpTransformer() {
		// this is the overall transformer we'll use to do the graph copy
		graphTransformer = new SimpleDelegatingTransformer();
		
		// AddressDAO[] to String
		AddressDAOArrayToStringConverter addressConverter =
			new AddressDAOArrayToStringConverter();		
		// PersonDAO[] to PersonVO[]
		PropertyNameMappingCopier personCopier = new PersonDAOToPersonVOCopier();
		Map personMapping = new HashMap();
		personMapping.put("children", "children");
		personMapping.put("addresses", "primaryAddress");
		personMapping.put("vehicles", "vehicles");		
		personCopier.setMapping(personMapping);
		personCopier.setNestedTransformer(graphTransformer);

		// the list of transformers that are involved in our overall graph
		// transformation
		List transformers = new ArrayList();
		// always put your custom transformers first
		transformers.add(personCopier);
		transformers.add(addressConverter);
		
		// then put in the default set of transformers as listed in the
		// SimpleDelegatingTransformer.  this makes sure all the normal conversions
		// you would expect from Morph are available (e.g. Integer 1 -> Long 1)
		transformers.add(new DefaultToBooleanConverter());
		transformers.add(new IdentityConverter());
		transformers.add(new DefaultToTextConverter());
		transformers.add(new TextToNumberConverter());
		transformers.add(new NumberConverter());
		transformers.add(new TextConverter());
		// will automatically take care of PersonDAO[] to String[]
		transformers.add(new ContainerCopier());
		// will automatically take care of VehicleDAO[] to VehicleVO[]
		transformers.add(new PropertyNameMatchingCopier());

		// convert our list of transformers into an array
		Transformer[] transformerArray = (Transformer[]) transformers.toArray(
			new Transformer[transformers.size()]);
		graphTransformer.setComponents(transformerArray);
	}

	public void testTransformation() throws Exception {
		// copy the information from personDAO to personVO
		PersonVO johnSmithVO = new PersonVO();
		graphTransformer.copy(johnSmithVO, johnSmith);
		
		TestUtils.assertEquals(johnSmithVO.getName(), "John A. Smith");
		TestUtils.assertEquals(johnSmithVO.getPrimaryAddress(), "1 Home St");
		
		String[] childrenNames = johnSmithVO.getChildren();
		TestUtils.assertEquals(childrenNames[0], "Matthew");
		TestUtils.assertEquals(childrenNames[1], "Natalie");
		
		VehicleVO[] vehicles = johnSmithVO.getVehicles();
		TestUtils.assertEquals(vehicles[0].getModel(), "Taurus");
		TestUtils.assertEquals(vehicles[1].getModel(), "Civic");
	}
}
