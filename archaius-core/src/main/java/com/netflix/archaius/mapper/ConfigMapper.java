/**
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.archaius.mapper;

import com.netflix.archaius.Config;
import com.netflix.archaius.PropertyFactory;
import com.netflix.archaius.exceptions.MappingException;

/**
 * API for mapping configuration to an object based on annotations or naming convention
 * 
 * @author elandau
 *
 */
public interface ConfigMapper {

    /**
     * Map the configuration from the provided config object onto the injectee and use
     * the provided IoCContainer to inject named bindings.
     * 
     * @param injectee
     * @param config
     * @param ioc
     * @throws MappingException
     */
    <T> void mapConfig(T injectee, Config config, IoCContainer ioc) throws MappingException;
    
    /**
     * Map the configuration from the provided config object onto the injectee.
     * 
     * @param injectee
     * @param config
     * @throws MappingException
     */
    <T> void mapConfig(T injectee, Config config) throws MappingException;

    /**
     * Create a proxy for the provided interface type for which all getter methods are bound
     * to a Property.
     * 
     * @param type
     * @param factory
     */
    <T> T newProxy(Class<T> type, PropertyFactory factory);
}
