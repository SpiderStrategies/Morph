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
package net.sf.morph2.transform.copiers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.model.AddressImpl;
import net.sf.morph2.transform.model.AssembledOrder;
import net.sf.morph2.transform.model.LineItemImpl;
import net.sf.morph2.transform.model.OrderImpl;
import net.sf.morph2.transform.model.PersonImpl;

/**
 * Default DisassemblerCopier test case.
 */
public class DisassemblerCopierTestCase extends BaseCopierTestCase {

	public List createValidPairs() throws Exception {
		List dest = new ArrayList();
		PersonImpl person = new PersonImpl();
		person.setName("foo bar");
		AddressImpl address = new AddressImpl();
		address.setPerson(person);
		String addressText = "123 somewhere; blah XX 99999";
		address.setText(addressText);
		person.setHomeAddress(address);
		LineItemImpl[] lineItems = new LineItemImpl[2];
		lineItems[0] = new LineItemImpl();
		lineItems[0].setQuantity(1);
		lineItems[0].setItemId("A1000");
		lineItems[1] = new LineItemImpl();
		lineItems[1].setQuantity(10);
		lineItems[1].setItemId("B1000");
		OrderImpl order = new OrderImpl();
		order.setLineItems(lineItems);
		dest.add(address);
		dest.add(order);

		AssembledOrder source = new AssembledOrder();
		source.setPerson(person);
		source.setText(addressText);
		source.setLineItems(lineItems);

		return Collections.singletonList(new ConvertedSourcePair(dest, source));
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	//not needed
	public List createDestinationClasses() throws Exception {
		return Collections.EMPTY_LIST;
	}

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(HttpSession.class);
		list.add(Object.class);
		list.add(getClass());
		list.add(Integer.class);
		list.add(ResultSet.class);
		return list;
	}

	protected Transformer createTransformer() {
		//test copy only:
		return new DisassemblerCopier() {
			protected Object convertImpl(Class destinationClass, Object source,
					Locale locale) throws Exception {
				ArrayList l = new ArrayList();
				l.add(new AddressImpl());
				l.add(new OrderImpl());
				copy(l, source, locale);
				return l;
			}
		};
	}

}
