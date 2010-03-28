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
package net.sf.morph2.examples.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.copiers.ContainerCopier;
import net.sf.morph2.transform.copiers.PropertyNameMappingCopier;
import net.sf.morph2.transform.copiers.PropertyNameMatchingCopier;
import net.sf.morph2.transform.transformers.SimpleDelegatingTransformer;


/**
 * @author Matt Sgarlata
 * @since Feb 12, 2005
 */
public class PersonDAOToPersonVOExample {

	
	public void programmaticExample() {
		
		// this is the overall transformer we'll use to do the graph copy
		SimpleDelegatingTransformer graphTransformer = new SimpleDelegatingTransformer();
		
		// VehicleDAO[] to VehicleVO[]
		PropertyNameMatchingCopier vehicleCopier = new PropertyNameMatchingCopier();
		// PersonDAO[] to String[]
		ContainerCopier childrenCopier = new ContainerCopier();
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
		// we need to put this copier first, otherwise the vehicleCopier will
		// try to do the copy and the name property of personVO won't be set
		transformers.add(personCopier);
		transformers.add(vehicleCopier);
		transformers.add(childrenCopier);
		transformers.add(addressConverter);
		
		// convert our list of transformers into an array
		Transformer[] transformerArray = (Transformer[]) transformers.toArray(
			new Transformer[transformers.size()]);
		graphTransformer.setComponents(transformerArray);
		
		// copy the information from personDAO to personVO
		//graphTransformer.copy(personVO, personDAO);
	}
	
}
