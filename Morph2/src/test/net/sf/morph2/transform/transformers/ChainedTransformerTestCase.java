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
package net.sf.morph2.transform.transformers;

import java.util.ArrayList;
import java.util.List;

import net.sf.morph2.transform.Converter;
import net.sf.morph2.transform.Transformer;
import net.sf.morph2.transform.converters.DefaultToTextConverter;
import net.sf.morph2.transform.converters.TextConverter;
import net.sf.morph2.transform.converters.TextToNumberConverter;
import net.sf.morph2.transform.converters.TextToNumberConverterTestCase;
import net.sf.morph2.transform.transformers.ChainedTransformer;

/**
 * @author Matt Sgarlata
 * @since Apr 14, 2005
 */
public class ChainedTransformerTestCase extends TextToNumberConverterTestCase {

	protected Transformer createTransformer() {
		ChainedTransformer chainedConverter = new ChainedTransformer();
		List chain = new ArrayList();
		TextToNumberConverter textToNumberConverter = new TextToNumberConverter() {
			protected Class[] getDestinationClassesImpl() throws Exception {
				return rearrange(super.getDestinationClassesImpl());
			}
		};
		chain.add(textToNumberConverter);
		chain.add(new DefaultToTextConverter() {
			protected Class[] getDestinationClassesImpl() throws Exception {
				return rearrange(super.getDestinationClassesImpl());
			}
		});
		chain.add(new TextConverter() {
			protected Class[] getDestinationClassesImpl() throws Exception {
				return rearrange(super.getDestinationClassesImpl());
			}
		});
		chain.add(textToNumberConverter);
		chainedConverter.setComponents(chain.toArray(new Converter[chain.size()]));
		return chainedConverter;
	}

	private static Class[] rearrange(Class[] c) {
		ArrayList l = new ArrayList();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == char.class || c[i] == Character.class || c[i] == null) {
				l.add(0, c[i]);
			} else {
				l.add(c[i]);
			}
		}
		return (Class[]) l.toArray(c);
	}
}
