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
package net.sf.morph2.util;

import java.beans.PropertyEditor;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 *
 */
public class ResourceArrayInputStreamPropertyEditorTest extends TestCase {
	public void test1() {
		assertEquals(
				"foobar",
				getContent("classpath:net/sf/morph2/util/foo,classpath:net/sf/morph2/util/bar"));
	}

	public void test2() {
		String content = getContent("classpath:net/sf/morph2/util/???");
		assertTrue("barfoo".equals(content) || "foobar".equals(content));
	}

	private String getContent(String resources) {
		PropertyEditor ed = new ResourceArrayInputStreamPropertyEditor();
		ed.setAsText(resources);
		InputStream is = (InputStream) ed.getValue();
		StringWriter sw = new StringWriter();
		try {
			for (int b = is.read(); b != -1; b = is.read()) {
				sw.write(b);
			}
		} catch (IOException e) {
			throw new NestableRuntimeException(e);
		}
		return sw.toString();
	}

}
