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

import net.sf.composite.util.ObjectUtils;

public class AssembledOrder {
	private Person person;
	private String text;
	private LineItem[] lineItems;

	public Person getPerson() {
		return person;
	}

	public String getText() {
		return text;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setText(String text) {
		this.text = text;
	}

	public LineItem[] getLineItems() {
		return lineItems;
	}

	public void setLineItems(LineItem[] lineItems) {
		this.lineItems = lineItems;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof AssembledOrder)) {
			return false;
		}
		AssembledOrder oao = (AssembledOrder) o;
		return ObjectUtils.equals(oao.person, person)
				&& ObjectUtils.equals(oao.text, text)
				&& ObjectUtils.equals(oao.lineItems, lineItems);
	}
}
