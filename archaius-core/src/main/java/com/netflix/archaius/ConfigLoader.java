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

import java.io.File;
import java.net.URL;
import java.util.Properties;

import com.netflix.archaius.exceptions.ConfigException;

/**
 * SPI for loading configurations.  The ConfigLoader provides a DSL 
 * @author elandau
 *
 */
public interface ConfigLoader {

    /**
     * DSL for loading a configuration
     * 
     * @author elandau
     *
     */
    public static interface Loader {
        /**
         * Cascading policy to use the loading based on a resource name.  All loaded
         * files will be merged into a single Config.
         * @param strategy
         */
        Loader withCascadeStrategy(CascadeStrategy strategy);
        
        /**
         * Arbitrary name assigned to the loaded Config.
         * @param name
         */
        Loader withName(String name);
        
        /**
         * Class loader to use
         * @param loader
         */
        Loader withClassLoader(ClassLoader loader);
        
        /**
         * When true, fail the entire load operation if the first resource name
         * can't be loaded.  By definition all cascaded variations are treated 
         * as overrides
         * @param flag
         */
        Loader withFailOnFirst(boolean flag);
        
        /**
         * Externally provided property overrides that are applied once 
         * all cascaded files have been loaded
         * 
         * @param props
         */
        Loader withOverrides(Properties props);
        
        /**
         * Once loaded add all the properties to System.setProperty()
         * @param toSystem
         */
        Loader withLoadToSystem(boolean toSystem);
        
        /**
         * Load configuration by cascade resource name.
         * @param resourceName
         * @return Config object representing the given resource.
         */
        Config load(String resourceName) throws ConfigException;
        
        /**
         * Load configuration from a specific URL
         * @param url
         * @return Config object representing the given url.
         */
        Config load(URL url) throws ConfigException;
        
        /**
         * Load configuration from a specific file
         * @param file
         * @return Config object representing the given file.
         * @throws ConfigException 
         */
        Config load(File file) throws ConfigException;
    }

    Loader newLoader();
}
