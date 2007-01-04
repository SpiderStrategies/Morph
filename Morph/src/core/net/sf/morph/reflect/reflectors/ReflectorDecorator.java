package net.sf.morph.reflect.reflectors;

import net.sf.morph.reflect.Reflector;

/**
 * A wrapper for Reflectors that allows any reflector to implement 
 * {@link net.sf.morph.reflect.DecoratedReflector}.
 * 
 * @author Matt Sgarlata
 * @since Dec 29, 2004
 */
public class ReflectorDecorator extends BaseReflector {
	
	private Reflector reflector;
	
	public ReflectorDecorator(Reflector reflector) {
		this.reflector = reflector;
	}

	protected Class[] getReflectableClassesImpl() throws Exception {
		return reflector.getReflectableClasses();
	}

	protected Reflector getReflector() {
		return reflector;
	}
	protected void setReflector(Reflector reflector) {
		this.reflector = reflector;
	}
}
