package org.uno.data;

import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.io.File;


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
    if (toReturn.getClass() != cls)
      toReturn = null;
    return toReturn;
  }

  private static Object format(Object value) {
    if (value.getClass() == String.class) {
      if (value == "True")
        return true;
      if (value == "False")
        return false;
    }
    return value;
  }

  private static Map<String, Object> load() {
    File file = new File(CONFIGURATIONS_FILE_LOCATION);
    StringBuilder sb = new StringBuilder();
    try (Scanner reader = new Scanner(file);) {
      while (reader.hasNextLine())
        sb.append(reader.nextLine() + "\n");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
    Yaml yaml = new Yaml();
    return (Map<String, Object>) yaml.load(sb.toString());

  }
}
