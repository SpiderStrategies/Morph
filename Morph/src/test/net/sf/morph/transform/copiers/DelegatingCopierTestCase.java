package net.sf.morph.transform.copiers;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph.Defaults;
import net.sf.morph.transform.Transformer;
import net.sf.morph.transform.converters.NumberConverterTestCase;
import net.sf.morph.transform.converters.NumberToTimeConverterTestCase;
import net.sf.morph.transform.converters.ObjectToClassConverterTestCase;
import net.sf.morph.transform.converters.TextConverterTestCase;
import net.sf.morph.transform.converters.TextToNumberConverterTestCase;
import net.sf.morph.transform.converters.TextToTimeConverterTestCase;
import net.sf.morph.transform.converters.TimeConverterTestCase;
import net.sf.morph.transform.converters.TimeToNumberConverterTestCase;
import net.sf.morph.transform.converters.toboolean.DefaultToBooleanConverterTestCase;
import net.sf.morph.transform.converters.totext.DefaultToTextConverterTestCase;
import net.sf.morph.util.TestObjects;

/**
 * @author Matt Sgarlata
 * @since Feb 22, 2005
 */
public class DelegatingCopierTestCase extends BaseCopierTestCase {

	protected Transformer createTransformer() {
		return Defaults.createCopier();
	}

	public List createInvalidDestinationClasses() throws Exception {
		return null;
	}

	public List createInvalidSources() throws Exception {
		List list = new ArrayList();
		list.add(null);
		return list;
	}

	public List createValidPairs() throws Exception {
		ArrayList result = new ArrayList();
		result.addAll(new ArrayCopierTestCase().createValidPairs());
		result.addAll(new DefaultToBooleanConverterTestCase().createValidPairs());
		result.addAll(new ObjectToClassConverterTestCase().createValidPairs());
		result.addAll(new TextConverterTestCase().createValidPairs());
		result.addAll(new DefaultToTextConverterTestCase().createValidPairs());
		result.addAll(new TextToNumberConverterTestCase().createValidPairs());
		result.addAll(new TextToTimeConverterTestCase().createValidPairs());
		result.addAll(new NumberToTimeConverterTestCase().createValidPairs());
		result.addAll(new TimeToNumberConverterTestCase().createValidPairs());
		result.addAll(new NumberConverterTestCase().createValidPairs());
		result.addAll(new TimeConverterTestCase().createValidPairs());

		//a bunch of container stuff, minus interfaces and enumerations:
		TestObjects to = new TestObjects();
		result.add(new ConvertedSourcePair(to.emptyList, to.emptyMap));
		result.add(new ConvertedSourcePair(to.emptyVector, to.emptyMap));
		result.add(new ConvertedSourcePair(to.emptyVector, to.emptyObjectArray));
		result.add(new ConvertedSourcePair(to.emptyList, to.emptyPrimitiveArray));
		result.add(new ConvertedSourcePair(to.emptyPrimitiveArray, to.emptyList));
		result.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalObjectArray));
		result.add(new ConvertedSourcePair(to.multidimensionalLongArray, to.multidimensionalPrimitiveArray));
		result.add(new ConvertedSourcePair(to.multidimensionalPrimitiveArray, to.multidimensionalLongArray));

		result.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeMap));
		result.add(new ConvertedSourcePair(to.oneTwoThreeList, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeObjectArray, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeNumberArray, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeSet, to.oneTwoThreeList));
		result.add(new ConvertedSourcePair(to.oneTwoThreeVector, to.oneTwoThreeList));

		return result;
	}

	public List createDestinationClasses() throws Exception {
		return null;
	}

}