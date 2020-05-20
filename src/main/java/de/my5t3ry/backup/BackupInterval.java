package de.my5t3ry.backup;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** User: my5t3ry Date: 5/19/20 2:16 PM */
public class BackupInterval {

  public static Map<String, Integer> values =
      Stream.of(
              new Object[][] {
                {"DAILY", 1},
                {"WEEKLY", 7},
              })
          .collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

  public static boolean isValide(String toUpperCase) {
    return values.keySet().contains(toUpperCase);
  }
}
