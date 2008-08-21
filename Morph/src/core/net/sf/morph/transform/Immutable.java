package net.sf.morph.transform;

import net.sf.morph.util.TransformerUtils;

/**
 * Marker interface for immutable types. While it is possible to explicitly implement
 * this interface in Java code, doing so would create a coupling to Morph, which is
 * not recommended. This interface is provided so that Morph can be guided at
 * runtime to know that a given type cannot be copied and must instead be converted.
 * It is envisioned that this interface could be introduced onto client classes via
 * AOP practices.
 * @since Morph 1.1.2
 * @see TransformerUtils#transform(Transformer, Class, Object, Object, java.util.Locale, Integer)
 */
public interface Immutable {
}
