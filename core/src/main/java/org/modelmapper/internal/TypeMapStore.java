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

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.modelmapper.Converter;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;

/**
 * @author Jonathan Halterman
 */
public final class TypeMapStore {
  private final Map<TypePair<?, ?>, TypeMap<?, ?>> typeMaps = new ConcurrentHashMap<TypePair<?, ?>, TypeMap<?, ?>>();
  private final Map<TypePair<?, ?>, TypeMap<?, ?>> immutableTypeMaps = Collections
      .unmodifiableMap(typeMaps);
  private final Object lock = new Object();
  private final InheritingConfiguration config;

  TypeMapStore(InheritingConfiguration config) {
    this.config = config;
  }

  /**
   * Gets or creates a TypeMap. If {@code converter} is null, the TypeMap is configured with
   * implicit mappings, else the {@code converter} is set against the TypeMap.
   */
  public <S, D> TypeMap<S, D> create(Class<S> sourceType, Class<D> destinationType,
      Configuration configuration, MappingEngineImpl engine) {
    TypeMapImpl<S, D> typeMap = new TypeMapImpl<S, D>(sourceType, destinationType, configuration,
        engine);
    new PropertyMappingBuilder<S, D>(typeMap, config.typeMapStore, config.converterStore).build();
    typeMaps.put(TypePair.of(sourceType, destinationType), typeMap);
    return typeMap;
  }

  public Collection<TypeMap<?, ?>> get() {
    return immutableTypeMaps.values();
  }

  /**
   * Returns a TypeMap for the {@code sourceType} and {@code destinationType}, else null if none
   * exists.
   */
  @SuppressWarnings("unchecked")
  public <S, D> TypeMap<S, D> get(Class<S> sourceType, Class<D> destinationType) {
    return (TypeMap<S, D>) typeMaps.get(TypePair.of(sourceType, destinationType));
  }

  /**
   * Gets or creates a TypeMap. If {@code converter} is null, the TypeMap is configured with
   * implicit mappings, else the {@code converter} is set against the TypeMap.
   */
  public <S, D> TypeMap<S, D> getOrCreate(Class<S> sourceType, Class<D> destinationType,
      MappingEngineImpl engine) {
    return this.<S, D>getOrCreate(sourceType, destinationType, null, null, engine);
  }

  /**
   * Gets or creates a TypeMap. If {@code converter} is null, the TypeMap is configured with
   * implicit mappings, else the {@code converter} is set against the TypeMap.
   * 
   * @param propertyMap to add mappings for (nullable)
   * @param converter to set (nullable)
   */
  @SuppressWarnings("unchecked")
  public <S, D> TypeMap<S, D> getOrCreate(Class<S> sourceType, Class<D> destinationType,
      PropertyMap<S, D> propertyMap, Converter<S, D> converter, MappingEngineImpl engine) {
    TypePair<S, D> typePair = TypePair.of(sourceType, destinationType);
    TypeMapImpl<S, D> typeMap = (TypeMapImpl<S, D>) typeMaps.get(typePair);

    if (typeMap == null) {
      typeMap = new TypeMapImpl<S, D>(sourceType, destinationType, config, engine);
      if (propertyMap != null)
        typeMap.addMappings(propertyMap);
      if (converter == null)
        new PropertyMappingBuilder<S, D>(typeMap, config.typeMapStore, config.converterStore)
            .build();

      typeMaps.put(typePair, typeMap);
    } else if (propertyMap != null)
      typeMap.addMappings(propertyMap);

    if (converter != null)
      typeMap.setConverter(converter);
    return typeMap;
  }

  public Object lock() {
    return lock;
  }
}
