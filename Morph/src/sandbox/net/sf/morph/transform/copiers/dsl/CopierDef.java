/*
 * Copyright 2007-2008 the original author or authors.
 * 
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
package net.sf.morph.transform.copiers.dsl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.composite.util.ObjectUtils;
import net.sf.morph.MorphException;
import net.sf.morph.transform.Copier;
import net.sf.morph.transform.NodeCopier;
import net.sf.morph.transform.copiers.CumulativeCopier;
import net.sf.morph.transform.copiers.PropertyExpressionMappingCopier;
import net.sf.morph.transform.copiers.PropertyNameMatchingCopier;
import net.sf.morph.util.Assert;
import net.sf.morph.util.ContainerUtils;
import net.sf.morph.util.ProxyUtils;

/**
 * Helper class to hold a copier definition.
 */
class CopierDef {
	private class PropertyMaps {
		Map rightward;
		Map leftward;
		Map exposeLeftward;
		Map bidi;

		PropertyMaps() {
			try {
				rightward = (Map) parent.getPropertyMapClass().newInstance();
				leftward = (Map) parent.getPropertyMapClass().newInstance();
			} catch (Exception e) {
				throw new MorphException(e);
			}
			exposeLeftward = new AbstractMap() {
				public Set entrySet() {
					return Collections.EMPTY_SET;
				}
				public Object put(Object right, Object left) {
					return leftward.put(left, right);
				}
			};
			bidi = new AbstractMap() {
				public Set entrySet() {
					return Collections.EMPTY_SET;
				}

				public Object put(Object left, Object right) {
					rightward.put(left, right);
					exposeLeftward.put(left, right);
					return null;
				}
			};
		}
		Map getExposedMap(Direction d) {
			return d == Direction.LEFT ? exposeLeftward : d == Direction.RIGHT ? rightward : bidi;
		}
		Map getAdjustedMap(Direction d) {
			return d == Direction.LEFT ? leftward : d == Direction.RIGHT ? rightward : null;
		}
		boolean isEmpty() {
			return rightward.isEmpty() && leftward.isEmpty();
		}
	}

	private DSLDefinedCopier parent;
	private Class leftClass;
	private Direction direction;
	private Class rightClass;

	private boolean matchProperties;
	private Set includeProperties;
	private Set ignoreProperties;
	private PropertyMaps propertyMapps;

	/**
	 * Construct a new CopierDef.
	 * @param leftClass
	 * @param direction
	 * @param rightClass
	 */
	public CopierDef(DSLDefinedCopier parent, Class leftClass, Direction direction,
			Class rightClass) {
		Assert.notNull(parent, "parent");
		this.parent = parent;
		Assert.notNull(leftClass, "leftClass");
		this.leftClass = leftClass;
		Assert.notNull(direction, "direction");
		this.direction = direction;
		Assert.notNull(rightClass, "rightClass");
		this.rightClass = rightClass;
		this.propertyMapps = new PropertyMaps();
	}

	/**
	 * Set the matchProperties of this CopierDef.
	 * @param matchProperties the matchProperties to set
	 */
	public synchronized void setMatchProperties(boolean matchProperties) {
		this.matchProperties = matchProperties;
	}

	/**
	 * Get the relevant property map.
	 * @param direction
	 * @return
	 */
	public synchronized Map getPropertyMap(Direction direction) {
		return propertyMapps.getExposedMap(resolveDirection(direction));
	}

	/**
	 * Get the ignoreProperties of this CopierDef.
	 * @return the ignoreProperties
	 */
	public synchronized Set getIgnoreProperties() {
		if (ignoreProperties == null) {
			ignoreProperties = ContainerUtils.createOrderedSet();
		}
		return ignoreProperties;
	}

	/**
	 * Get the includeProperties of this CopierDef.
	 * @return the includeProperties
	 */
	public synchronized Set getIncludeProperties() {
		if (includeProperties == null) {
			includeProperties = ContainerUtils.createOrderedSet();
		}
		return includeProperties;
	}

	/**
	 * Get the defined Copiers.
	 * @return Copier[]
	 */
	public synchronized List getCopiers() {
		PropertyNameMatchingCopier matcher = createMatcher();
		//if there are no properties, a single bidi matcher, or null (?), will suffice: 
		if (propertyMapps.isEmpty()) {
			if (matcher == null) {
				return Collections.EMPTY_LIST;
			}
			matcher.setSourceClasses(getSourceClasses(direction));
			matcher.setDestinationClasses(getDestClasses(direction));
			return Collections.singletonList(matcher);
		}
		List leftCopiers = null;
		List rightCopiers = null;

		if (direction != Direction.RIGHT) {
			//if we got here, use any matcher we already had:
			leftCopiers = new ArrayList();
			if (matcher != null) {
				matcher.setSourceClasses(getSourceClasses(Direction.LEFT));
				matcher.setDestinationClasses(getDestClasses(Direction.LEFT));
				leftCopiers.add(matcher);
			}
			addNotNull(leftCopiers, createMapper(Direction.LEFT));
			//null matcher ref so rightward will generate a new one if needed:
			matcher = null;
		}
		if (direction != Direction.LEFT) {
			rightCopiers = new ArrayList();
			//catch matcher we might have nulled in !RIGHT block:
			if (matcher == null) {
				matcher = createMatcher();
			}
			if (matcher != null) {
				matcher.setSourceClasses(getSourceClasses(Direction.RIGHT));
				matcher.setDestinationClasses(getDestClasses(Direction.RIGHT));
				rightCopiers.add(matcher);
			}
			addNotNull(rightCopiers, createMapper(Direction.RIGHT));
		}
		List result = addNotNull(new ArrayList(), accumulate(rightCopiers));
		result = addNotNull(result, accumulate(leftCopiers));
		return result;
	}

	private Copier accumulate(List l) {
		if (ObjectUtils.isEmpty(l)) {
			return null;
		}
		if (l.size() == 1) {
			return (Copier) l.get(0);
		}
		CumulativeCopier result = new CumulativeCopier();
		result.setComponents(l.toArray(new Copier[l.size()]));
		return (NodeCopier) ProxyUtils.getProxy(result, NodeCopier.class);
	}

	private PropertyNameMatchingCopier createMatcher() {
		if (!matchProperties && ObjectUtils.isEmpty(includeProperties)) {
			return null;
		}
		PropertyNameMatchingCopier matcher = new PropertyNameMatchingCopier();
		if (!ObjectUtils.isEmpty(includeProperties)) {
			matcher.setPropertiesToCopy(toStringArray(includeProperties));
		}
		if (!ObjectUtils.isEmpty(ignoreProperties)) {
			matcher.setPropertiesToIgnore(toStringArray(ignoreProperties));
		}
		matcher.setReflector(parent.getReflector());
		return matcher;
	}

	private String[] toStringArray(Set s) {
		return (String[]) s.toArray(new String[s.size()]);
	}

	private Direction resolveDirection(Direction dir) {
		if (direction == dir || this.direction == Direction.BIDI) {
			return dir;
		}
		if (dir == Direction.BIDI) {
			return this.direction;
		}
		throw new IllegalArgumentException("property mapping direction: " + dir);
	}

	private PropertyExpressionMappingCopier createMapper(Direction d) {
		Map m = propertyMapps.getAdjustedMap(d);
		if (m.isEmpty()) {
			return null;
		}
		PropertyExpressionMappingCopier mapper = new PropertyExpressionMappingCopier();
		//true reverse mapping rather than PropertyNameMappingCopier's reverse-as-fallback implementation:
		mapper.setMapping(m);
		mapper.setReflector(parent.getReflector());
		mapper.setSourceClasses(getSourceClasses(d));
		mapper.setDestinationClasses(getDestClasses(d));
		return mapper;
	}

	private List addNotNull(List l, Object o) {
		if (o != null) {
			l.add(o);
		}
		return l;
	}

	private Class[] getSourceClasses(Direction dir) {
		Direction d = resolveDirection(dir);
		return d == Direction.BIDI && leftClass != rightClass ? new Class[] {
				leftClass, rightClass } : new Class[] { d == Direction.LEFT ? rightClass
				: leftClass };
	}

	private Class[] getDestClasses(Direction dir) {
		Direction d = resolveDirection(dir);
		return d == Direction.BIDI && leftClass != rightClass ? new Class[] {
				leftClass, rightClass } : new Class[] { d == Direction.LEFT ? leftClass
				: rightClass };
	}

	/**
	 * Get the direction of this CopierDef.
	 * @return the direction
	 */
	public synchronized Direction getDirection() {
		return direction;
	}

	/**
	 * Get the leftClass of this CopierDef.
	 * @return the leftClass
	 */
	public synchronized Class getLeftClass() {
		return leftClass;
	}

	/**
	 * Get the matchProperties of this CopierDef.
	 * @return the matchProperties
	 */
	public synchronized boolean isMatchProperties() {
		return matchProperties;
	}

	/**
	 * Get the rightClass of this CopierDef.
	 * @return the rightClass
	 */
	public synchronized Class getRightClass() {
		return rightClass;
	}

}
