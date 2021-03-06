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
package net.sf.morph2.transform.model;

import java.util.List;

/**
 * Person implementation for transfer to a different tier of the application.
 * 
 * @author Matt Sgarlata
 * @since Dec 2, 2005
 */
public class PersonTo implements Person {
	private String name;
	private List children;
	private Address homeAddress;
	
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public Address getHomeAddress() {
		return homeAddress;
	}
	public void setHomeAddress(Address homeAddress) {
		this.homeAddress = homeAddress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}
}
