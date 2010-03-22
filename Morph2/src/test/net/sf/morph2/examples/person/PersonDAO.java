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
import java.util.List;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph2.util.StringUtils;

/**
 * @author Matt Sgarlata
 * @since Feb 12, 2005
 */
public class PersonDAO {
	private String firstName;
	private String middleName;
	private String lastName;
	private String creditCardNumber;
	private PersonDAO[] children;
	private AddressDAO[] addresses;
	private VehicleDAO[] vehicles;
	public AddressDAO[] getAddresses() {
		return addresses;
	}
	public void setAddresses(AddressDAO[] addresses) {
		this.addresses = addresses;
	}
	public PersonDAO[] getChildren() {
		return children;
	}
	public void setChildren(PersonDAO[] children) {
		this.children = children;
	}
	public String getCreditCardNumber() {
		return creditCardNumber;
	}
	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public VehicleDAO[] getVehicles() {
		return vehicles;
	}
	public void setVehicles(VehicleDAO[] vehicles) {
		this.vehicles = vehicles;
	}
	public String toString() {
		List nameComponents = new ArrayList();
		if (!ObjectUtils.isEmpty(getFirstName())) {
			nameComponents.add(getFirstName());
		}
		if (!ObjectUtils.isEmpty(getMiddleName())) {
			nameComponents.add(getMiddleName());
		}
		if (!ObjectUtils.isEmpty(getLastName())) {
			nameComponents.add(getLastName());
		}
		return StringUtils.join(nameComponents, " ");
	}
}
