package edu.monash.honours.util;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class SpeedReadingPair
{
  private final double key;
  private boolean value;

  public SpeedReadingPair(final double key, final boolean value)
  {
    this.key = key;
    this.value = value;
  }

  public SpeedReadingPair(Map<String, Object> jsonObject)
  {
    this((Double) jsonObject.get("speed"), (Boolean) jsonObject.get("flag"));
  }

  @Override
  public String toString()
  {
    return "{" + this.key + ":" + this.value + "}";
  }

  public double getSpeed()
  {
    return this.key;
  }

  public boolean flagged()
  {
    return this.value;
  }

  public void flag()
  {
    this.value = true;
  }

  public static Map<String, Object> toMap(SpeedReadingPair pair) {
    Map<String, Object> jsonObject = new HashMap<String, Object>();

    jsonObject.put("speed", pair.getSpeed());
    jsonObject.put("flag", pair.flagged());

    return jsonObject;
  }

  public static String toJson(SpeedReadingPair pair) {
    return pair.toString();
  }
}
