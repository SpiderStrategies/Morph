package net.sf.morph.wrap.support;

import java.lang.reflect.InvocationHandler;

/**
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public interface WrapperInvocationHandler extends InvocationHandler {
	
	public Class[] getInterfaces(Object object);
	
}
