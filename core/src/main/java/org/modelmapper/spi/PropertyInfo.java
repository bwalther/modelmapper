/**
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
package org.modelmapper.spi;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;

/**
 * Encapsulates information for a property.
 * 
 * @author Jonathan Halterman
 */
public interface PropertyInfo {
  /**
   * Returns the annotation on the property's member for the {@code annotationClass} or null if none
   * exists.
   * 
   * @param <T> annotation type
   * @param annotationClass to get annotation for
   */
  <T extends Annotation> T getAnnotation(Class<T> annotationClass);

  /**
   * Returns the generic type represented by the property. For fields this will be the field's
   * generic type. For accessor methods this will be the generic return type. For mutator methods
   * this will be the single parameter's generic type.
   */
  Type getGenericType();

  /**
   * Returns the encapsulated member.
   */
  Member getMember();

  /**
   * Returns the property name.
   */
  String getName();

  /**
   * Returns the member type.
   */
  PropertyType getPropertyType();

  /**
   * Returns the type represented by the property. For fields this will be the field type. For
   * accessor methods this will be the return type. For mutator methods this will be the single
   * parameter type.
   */
  Class<?> getType();
}