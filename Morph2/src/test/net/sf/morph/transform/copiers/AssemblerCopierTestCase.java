package net.sf.morph.transform.copiers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.model.AddressImpl;
import net.sf.morph.transform.model.AssembledOrder;
import net.sf.morph.transform.model.LineItemImpl;
import net.sf.morph.transform.model.OrderImpl;
import net.sf.morph.transform.model.PersonImpl;

/**
 * Default AssemblerCopier test case.
 */
public class AssemblerCopierTestCase extends BaseCopierTestCase {

	public List createValidPairs() throws Exception {
		List source = new ArrayList();
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
		source.add(address);
		source.add(order);

		AssembledOrder dest = new AssembledOrder();
		dest.setPerson(person);
		dest.setText(addressText);
		dest.setLineItems(lineItems);

		return Collections.singletonList(new ConvertedSourcePair(dest, source));
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(HttpSession.class);
		list.add(Object.class);
		list.add(getClass());
		list.add(Integer.class);
		list.add(ResultSet.class);
		return list;
	}

	//not needed
	public List createDestinationClasses() throws Exception {
		return Collections.EMPTY_LIST;
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	protected Transformer createTransformer() {
		return new AssemblerCopier();
	}
}
