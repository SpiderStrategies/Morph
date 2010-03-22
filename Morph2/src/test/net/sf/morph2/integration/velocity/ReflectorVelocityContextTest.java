package net.sf.morph2.integration.velocity;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import junit.framework.TestCase;

public class ReflectorVelocityContextTest extends TestCase {
	public static class Bean {
		private String name;
		private Bag bag;

		/**
		 * Get the name.
		 * @return String
		 */
		public String getName() {
			return name;
		}

		/**
		 * Set the name.
		 * @param name the String to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Get the bag.
		 * @return Bag
		 */
		public Bag getBag() {
			return bag;
		}

		/**
		 * Set the bag.
		 * @param bag the Bag to set
		 */
		public void setBag(Bag bag) {
			this.bag = bag;
		}

	}

	public static class Bag {
		private String shape = "round";

		/**
		 * Get the shape.
		 * @return String
		 */
		public String getShape() {
			return shape;
		}

		/**
		 * Set the shape.
		 * @param shape the String to set
		 */
		public void setShape(String shape) {
			this.shape = shape;
		}

	}

	private VelocityEngine engine;
	private Context mapContext;
	private Context beanContext;
	private Context chainedContext;

	protected void setUp() throws Exception {
		super.setUp();
		engine = new VelocityEngine();
		engine.init();

		HashMap map = new HashMap();
		map.put("name", "map");
		HashMap child = new HashMap();
		child.put("a", "A");
		child.put("b", "B");
		child.put("c", "C");
		map.put("foo", child);
		child = new HashMap();
		child.put("x", "X");
		child.put("y", "Y");
		child.put("z", "Z");
		map.put("bar", child);
		mapContext = new ReflectorVelocityContext(map);

		Bean bean = new Bean();
		bean.setName("garbanzo");
		bean.setBag(new Bag());
		beanContext = new ReflectorVelocityContext(bean);

		chainedContext = new ReflectorVelocityContext(bean, mapContext);
	}

	public void testBasic() throws Exception {
		evaluate(mapContext, "map", "$name");
		evaluate(beanContext, "garbanzo", "$name");
		evaluate(chainedContext, "garbanzo", "$name");
	}

	public void testEmptyTemplate() throws Exception {
		evaluate(mapContext, "", "");
		evaluate(beanContext, "", "");
		evaluate(chainedContext, "", "");
	}

	public void testNested() throws Exception {
		evaluate(mapContext, "foo.a = A ; bar.x = X", "foo.a = $foo.a ; bar.x = $bar.x");
		evaluate(beanContext, "bag.shape = round", "bag.shape = $bag.shape");
		evaluate(chainedContext, "foo.a = A ; bar.x = X ; bag.shape = round", "foo.a = $foo.a ; bar.x = $bar.x ; bag.shape = $bag.shape");
	}

	public void testUpdate() throws Exception {
		assertEquals("map", mapContext.put("name", "oderus"));
		evaluate(mapContext, "oderus", "$name");
		assertEquals("garbanzo", beanContext.put("name", "jumping"));
		evaluate(beanContext, "jumping", "$name");
	}

	private void evaluate(Context context, String expected, String template)
			throws Exception {
		StringWriter sw = new StringWriter();
		engine.evaluate(context, sw, "", new StringReader(template));
		assertEquals(expected, sw.toString());
	}
}
