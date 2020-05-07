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
 * @author Fábio Furtado
 */
public class Settings {

    private static final Map<String, Object> settings = load();
    private static final String CONFIGURATIONS_FILE_LOCATION = "uno.yml";

    public static Object get(Class<?> cls, String... keys) {
        Object toReturn = null;
        if (keys.length == 1)
            toReturn = settings.get(keys[0]);
        else if (keys.length == 2) {
            Map<String, Object> sub = (Map<String, Object>) settings.get(keys[0]);
            if (sub != null)
                return sub.get(keys[1]);
        }
        toReturn = format(toReturn);
        try {
            if (toReturn.getClass() != cls)
                toReturn = null;
        } catch (NullPointerException e) {
            toReturn = null;
        }
        return toReturn;
    }

    private static Object format(Object value) {
        try {
            if (value.getClass() != null) {
                if (value.getClass() == String.class) {
                    if (value == "True")
                        return true;
                    if (value == "False")
                        return false;
                }
            }
        } catch (NullPointerException ignored) {
        }
        return value;
    }

    private static Map<String, Object> load() {
        File file = new File(CONFIGURATIONS_FILE_LOCATION);
        StringBuilder sb = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        try (Scanner reader = new Scanner(file);) {
            while (reader.hasNextLine())
                sb.append(reader.nextLine() + "\n");
            Yaml yaml = new Yaml();
            map = (Map<String, Object>) yaml.load(sb.toString());
        } catch (FileNotFoundException ignored) {
        }
        return map;

    }
}
