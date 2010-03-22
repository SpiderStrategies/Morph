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

import java.util.Locale;

import net.sf.morph2.transform.TransformationException;
import net.sf.morph2.transform.TransformationType;
import net.sf.morph2.transform.copiers.PropertyNameMappingCopier;


/**
 * @author Matt Sgarlata
 * @since Feb 12, 2005
 */
public class PersonDAOToPersonVOCopier extends PropertyNameMappingCopier {

	protected void copyImpl(Object destination, Object source, Locale locale, TransformationType preferredTransformationType)
		throws TransformationException {
		
		super.copyImpl(destination, source, locale, preferredTransformationType);
		
		// this cast is safe because our superclass makes sure the source is of
		// the correct type and not null
		PersonDAO personDAO = (PersonDAO) source;
		// construct the name
		String name = personDAO.getFirstName() + " "
			+ personDAO.getMiddleName() + " " + personDAO.getLastName();
		
		// this cast is safe because our superclass makes sure the destination
		// is of the correct type and not null
		PersonVO personVO = (PersonVO) destination;
		// save the name
		personVO.setName(name);
		
	}
	
	protected Class[] getDestinationClassesImpl() throws Exception {
		return new Class[] { PersonVO.class };
	}
	
	protected Class[] getSourceClassesImpl() throws Exception {
		return new Class[] { PersonDAO.class };
	}
	
}
