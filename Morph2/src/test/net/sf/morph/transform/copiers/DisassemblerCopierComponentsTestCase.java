package net.sf.morph.transform.copiers;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.morph.reflect.Reflector;
import net.sf.morph.reflect.reflectors.SimpleDelegatingReflector;
import net.sf.morph.reflect.reflectors.SimpleInstantiatingReflector;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.model.Address;
import net.sf.morph.transform.model.AddressImpl;
import net.sf.morph.transform.model.AssembledOrder;
import net.sf.morph.transform.model.LineItemImpl;
import net.sf.morph.transform.model.Order;
import net.sf.morph.transform.model.OrderImpl;
import net.sf.morph.transform.model.PersonImpl;
import net.sf.morph.util.TypeMap;

/**
 * Default DisassemblerCopier test case.
 */
public class DisassemblerCopierComponentsTestCase extends BaseCopierTestCase {

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
		return new DisassemblerCopier(new PropertyNameMatchingCopier[] {
				getMatcher(Address.class), getMatcher(Order.class) });
	}

	private static final Reflector NESTED_REFLECTOR;
	static {
		TypeMap modelMap = new TypeMap();
		modelMap.put(Address.class, AddressImpl.class);
		modelMap.put(Order.class, OrderImpl.class);
		NESTED_REFLECTOR = new SimpleDelegatingReflector(
				new Reflector[] { new SimpleInstantiatingReflector(modelMap) }, true);
	}

	protected static PropertyNameMatchingCopier getMatcher(Class destinationType) {
		PropertyNameMatchingCopier result = new PropertyNameMatchingCopier();
		result.setDestinationClasses(new Class[] { destinationType });
		result.setReflector(NESTED_REFLECTOR);
		return result;
	}
}
