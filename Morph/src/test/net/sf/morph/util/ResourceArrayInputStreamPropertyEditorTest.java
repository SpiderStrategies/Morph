package net.sf.morph.util;

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
				getContent("classpath:net/sf/morph/util/foo,classpath:net/sf/morph/util/bar"));
	}

	public void test2() {
		String content = getContent("classpath:net/sf/morph/util/???");
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
