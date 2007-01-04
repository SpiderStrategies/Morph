package net.sf.morph.util;

import java.util.HashMap;
import java.util.Map;

import net.sf.composite.util.ObjectUtils;

/**
 * @author Matt Sgarlata
 * @since Jan 16, 2005
 */
public class BidirectionalMap extends HashMap {
	
	private final Map reverseMap; 
	
	public BidirectionalMap() {
		super();
		reverseMap = new HashMap();
	}
	public BidirectionalMap(int initialCapacity) {
		super(initialCapacity);
		reverseMap = new HashMap(initialCapacity);
	}
	public BidirectionalMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
		reverseMap = new HashMap(initialCapacity, loadFactor);
	}
	public BidirectionalMap(Map m) {
		this(m.size());
		putAll(m);
	}
	
	/**
	 * Retrieves the key that is registered for the given entry
	 */
	public Object getKey(Object entry) {
		return reverseMap.get(entry);
	}
	
	public void clear() {
		super.clear();
		reverseMap.clear();
	}
	public Object clone() {
		HashMap clone = (HashMap) super.clone();
		return new BidirectionalMap(clone);
	}

	public Object put(Object key, Object value) {
		if (reverseMap.containsKey(value)) {
			throw new IllegalArgumentException("The value '"
				+ ObjectUtils.getObjectDescription(value)
				+ "' has already been added to the map");
		}
		reverseMap.put(value, key);
		return super.put(key, value);
	}
//	public void putAll(Map m) {
//		super.putAll(m);
//		if (m != null) {
//			Iterator keys = m.keySet().iterator();
//			while (keys != null && keys.hasNext()) {
//				Object key = keys.next();
//				Object value = m.get(key);
//				if (reverseMap.containsKey(value)) {
//					throw new IllegalArgumentException("Duplicate value "
//						+ ObjectUtils.getObjectDescription(value)
//						+ " found in map "
//						+ ObjectUtils.getObjectDescription(m));
//				}
//				reverseMap.put(value, key);
//			}
//		}
//	}
	public Object remove(Object key) {
		Object value = get(key);
		reverseMap.remove(value);
		return super.remove(key);
	}
	
	public Map getReverseMap() {
		return reverseMap;
	}
}
