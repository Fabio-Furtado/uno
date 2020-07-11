/*
 * Copyright (C) 2020  Fábio Furtado
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.uno.data;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


/**
 * This is an abstraction of the program's config file.
 * The values are stored in memory thru a key value system, supporting nested
 * values. This class has no writing functionality whatsoever as it's only meant
 * the read the configs which are externally written on the program's config file.
 *
 * @author Fábio Furtado
 */
public class ConfigReader {

    /**
     * Map with the settings
     */
    private static final Map<String, Object> settings = load();

    /**
     * Location in the disk where to look for the config file
     */
    private static final String CONFIGURATIONS_FILE_LOCATION = "uno.yml";

    /**
     * Not to be instantiated
     */
    private ConfigReader() {}

    /**
     * Returns the value of the setting corresponding to the given key or keys
     * (if nested). Will always return null if the config file was not found.
     *
     * @param keys can be a single or multiple keys depending if the value is
     *             nested
     * @return value of the setting corresponding to the given key or keys, null
     *         if the value does not exist or maybe the config file is missing
     */
    public static Object get(String... keys) {
        Object value = null;
        try {
            if (keys.length == 1)
                value = settings.get(keys[0]);
            else if (keys.length == 2) {
                Map<String, Object> sub = (Map<String, Object>) settings.get(keys[0]);
                if (sub != null)
                    return sub.get(keys[1]);
            }
        } catch (NullPointerException ignored) {
            // Thrown if trying to access a nested value from a parent which
            // does not exist
        }
        return value;
    }

    /**
     * Checks if the config file was found by the time this object was initialized
     * If the return value is false the {@link ConfigReader#get(String...)} method
     * will return null for any value requested.
     *
     * @return true if the config file exists, false if not
     */
    public static boolean configFileExists() {
        return settings.size() > 0;
    }

    /**
     * Loads the setting from the configuration file.
     *
     * @return map with the values, empty map if the file does not exist
     */
    private static Map<String, Object> load() {
        File file = new File(CONFIGURATIONS_FILE_LOCATION);
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        try (Scanner reader = new Scanner(file)) {
            while (reader.hasNextLine()) {
                sb.append(reader.nextLine());
                sb.append("\n");
            }
            Yaml yaml = new Yaml();
            map = (Map<String, Object>) yaml.load(sb.toString());
        } catch (FileNotFoundException ignored) {
            System.err.println("No configuration file could be found");
        }
        return map;
    }
}
