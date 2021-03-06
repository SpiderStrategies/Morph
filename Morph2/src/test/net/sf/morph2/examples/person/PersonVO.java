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

/**
 * @author Matt Sgarlata
 * @since Feb 12, 2005
 */
public class PersonVO {
	private String name;
	private String[] children;
	private String primaryAddress;
	private VehicleVO[] vehicles;

	public String[] getChildren() {
		return children;
	}
	public void setChildren(String[] children) {
		this.children = children;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrimaryAddress() {
		return primaryAddress;
	}
	public void setPrimaryAddress(String primaryAddress) {
		this.primaryAddress = primaryAddress;
	}
	public VehicleVO[] getVehicles() {
		return vehicles;
	}
	public void setVehicles(VehicleVO[] vehicles) {
		this.vehicles = vehicles;
	}
}
