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
package com.netflix.archaius;

import java.net.URL;

import com.netflix.archaius.exceptions.ConfigException;

/**
 * Contract for a configuration file loader.  A ConfigManager will likely be configured with 
 * multiple configuration loaders, each responsible for loading a specific configuration
 * format and loading from a specific location.
 * 
 * TODO: Consider splitting load(resource) into a separate abstraction
 * 
 * @author elandau
 *
 */
public interface ConfigReader {
    /**
     * Load configuration from a simple resource name.  A concrete ConfigLoader will need to add
     * location and type information to this resource.
     * 
     * For example, an WebAppConfigurationLoader will attempt load to the configuration from
     *    resourceName : 'application-prod'
     *    
     *    /WEB-INF/confg/application-prod.properties
     *    
     * @param resourceName
     * @return Loaded configuration.
     */
    Config load(ClassLoader loader, String name, String resourceName) throws ConfigException ;
    
    /**
     * Load a specific URL.  The URL is assumed to be fully formed.  The concrete ConfigLoader will
     * only need to check that the extension is supported (ex .properties)
     * 
     * @param name
     * @return Loaded configuration.
     */
    Config load(ClassLoader loader, String name, URL url) throws ConfigException;
    
    /**
     * Determine if this reader can load the provided resource name
     * 
     * @param resourceName
     */
    boolean canLoad(ClassLoader loader, String resourceName);

    /**
     * Determine if this reader can load the provided url
     * @param loader
     */
    boolean canLoad(ClassLoader loader, URL uril);
}
