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
package org.modelmapper;

import java.util.List;

import org.modelmapper.spi.Mapping;
import org.modelmapper.spi.PropertyInfo;

/**
 * Encapsulates mapping configuration for a source and destination type pair.
 * 
 * @param <S> source type
 * @param <D> destination type
 * 
 * @author Jonathan Halterman
 */
public interface TypeMap<S, D> {
  /**
   * Loads mappings from the {@code propertyMap} into the TypeMap. Mappings defined in the
   * {@code propertyMap} will override any implicit mappings for the same properties.
   * 
   * @param propertyMap from which mappings should be loaded
   * @throws IllegalArgumentException if {@code propertyMap} is null
   * @throws ConfigurationException if a configuration error occurs while adding mappings for the
   *           {@code propertyMap}
   */
  void addMappings(PropertyMap<S, D> propertyMap);

  /**
   * Returns the Condition that must apply in order for mapping to take place, else {@code null} if
   * no condition has been configured.
   * 
   * @see #setCondition(Condition)
   */
  Condition<?, ?> getCondition();

  /**
   * Returns the Converter configured for this TypeMap, else {@code null} if no Converter has been
   * configured.
   * 
   * @see #setConverter(Converter)
   */
  Converter<S, D> getConverter();

  /**
   * Returns the destination type for the TypeMap.
   */
  Class<D> getDestinationType();

  /**
   * Returns a snapshot of the TypeMap's mappings.
   * 
   * <p>
   * This method is part of the ModelMapper SPI.
   */
  List<Mapping> getMappings();

  /**
   * Returns the Provider configured for this TypeMap, else {@code null} if no Provider has been
   * configured.
   * 
   * @see #setProvider(Provider)
   */
  Provider<D> getProvider();

  /**
   * Returns the source type for the TypeMap.
   */
  Class<S> getSourceType();

  /**
   * Returns a snapshot list of destination properties that do not have mappings defined, else empty
   * list if all destination properties are mapped.
   * 
   * <p>
   * This method is part of the ModelMapper SPI.
   */
  List<PropertyInfo> getUnmappedProperties();

  /**
   * Maps {@code source} to an instance of type {@code D}.
   * 
   * @param source object to map from
   * @return fully mapped instance of type {@code D}
   * @throws IllegalArgumentException if {@code source} is null
   * @throws MappingException if an error occurs while mapping
   */
  D map(S source);

  /**
   * Maps {@code source} to {@code destination}.
   * 
   * @param source object to map from
   * @param destination object to map to
   * @throws IllegalArgumentException if {@code source} or {@code destination} are null
   * @throws MappingException if an error occurs while mapping
   */
  void map(S source, D destination);

  /**
   * Sets the {@code condition} that must apply in order for mapping to take place.
   * 
   * @throws IllegalArgumentException if {@code condition} is null
   */
  TypeMap<S, D> setCondition(Condition<?, ?> condition);

  /**
   * Sets the {@code converter} to be used for any conversion requests for the TypeMap's source to
   * destination type.
   * 
   * @throws IllegalArgumentException if {@code converter} is null
   */
  TypeMap<S, D> setConverter(Converter<S, D> converter);

  /**
   * Sets the {@code provider} to be used for providing instances of destination type {@code D}
   * during mapping.
   * 
   * @throws IllegalArgumentException if {@code provider} is null
   */
  TypeMap<S, D> setProvider(Provider<D> provider);

  /**
   * Validates that <b>every</b> top level destination property is mapped to one and only one source
   * property, or that a {@code Converter} was {@link #setConverter(Converter) set}. If not, a
   * ConfigurationException is thrown detailing any missing mappings.
   * 
   * @throws ValidationException if any TypeMaps contain unmapped properties
   */
  void validate();
}
