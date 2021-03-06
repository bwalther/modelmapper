/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.modelmapper.internal;

import java.util.HashMap;
import java.util.Map;

import org.modelmapper.config.Configuration;

/**
 * Statically stores and retrieves TypeInfo instances by type and configuration.
 * 
 * @author Jonathan Halterman
 */
class TypeInfoRegistry {
  private static final Map<Integer, TypeInfoImpl<?>> cache = new HashMap<Integer, TypeInfoImpl<?>>();

  private static Integer hashCodeFor(Class<?> type, Configuration configuration) {
    return Integer.valueOf(type.hashCode() * 31 + configuration.hashCode());
  }

  static synchronized <T> TypeInfoImpl<T> typeInfoFor(Class<T> type, Configuration configuration) {
    Integer hashCode = hashCodeFor(type, configuration);
    @SuppressWarnings("unchecked")
    TypeInfoImpl<T> typeInfo = (TypeInfoImpl<T>) cache.get(hashCode);
    if (typeInfo == null) {
      typeInfo = new TypeInfoImpl<T>(type, configuration);
      cache.put(hashCode, typeInfo);
    }

    return typeInfo;
  }
}
