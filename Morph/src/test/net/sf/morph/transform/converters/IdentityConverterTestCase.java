package net.sf.morph.transform.converters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.morph.transform.Transformer;

/**
 * @author Matt Sgarlata
 * @since Jan 9, 2005
 */
public class IdentityConverterTestCase extends BaseConverterTestCase {
	
	private Character zeroCharacter = new Character('0');
	private Integer zeroInteger = new Integer("0");
	private Long zeroLong = new Long("0");
	private String emptyString = "";

	public List createInvalidDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(null);
		return list;
	}

	public List createInvalidSources() throws Exception {
		return null;
	}

	public List createValidPairs() throws Exception {
		List list = new ArrayList();
		list.add(new ConvertedSourcePair(zeroCharacter, zeroCharacter));
		list.add(new ConvertedSourcePair(zeroInteger, zeroInteger));
		list.add(new ConvertedSourcePair(list, list));
		list.add(new ConvertedSourcePair(this, this));
		list.add(new ConvertedSourcePair(emptyString, emptyString));
		list.add(new ConvertedSourcePair(zeroLong, zeroLong));
		return list;
	}

	public List createDestinationClasses() throws Exception {
		List list = new ArrayList();
		list.add(Object.class);
		list.add(Number.class);
		list.add(String.class);
		list.add(Long.TYPE);
		list.add(Date.class);
		list.add(Map.class);
		list.add(List.class);
		return list;
	}

	protected Transformer createTransformer() {
		return new IdentityConverter();
	}

}
