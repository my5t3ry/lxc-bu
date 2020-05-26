package de.my5t3ry.backup;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** User: my5t3ry Date: 5/19/20 2:16 PM */
public class BackupInterval {
  public static Map<String, Integer> values =
      Stream.of(
              new Object[][] {
                {"HOURLY", 1},
                {"FOUR_HOURLY", 4},
                {"TWELVE_HOURLY", 24},
                {"DAILY", 12},
                {"TWO_DAILY", 48},
                {"WEEKLY", 168},
              })
          .collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));

  public static String getKey(int value) {
    final List<Map.Entry<String, Integer>> result =
        BackupInterval.values.entrySet().stream()
            .filter(curEntry -> curEntry.getValue().equals(value))
            .collect(Collectors.toList());
    if (result.size() != 1) {
      throw new IllegalStateException("could not find BackupInterval key for ['" + value + "']");
    }
    return result.get(0).getKey();
  }

  public static boolean isValid(String toUpperCase) {
    return values.containsKey(toUpperCase);
  }
}
