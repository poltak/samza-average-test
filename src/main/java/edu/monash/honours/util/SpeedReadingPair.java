package edu.monash.honours.util;

import java.util.AbstractMap;

public class SpeedReadingPair extends AbstractMap.SimpleEntry<Double, Boolean>
{
  public SpeedReadingPair(final Double key, final Boolean value)
  {
    super(key, value);
  }

  @Override
  public String toString()
  {
    return "{" + this.getKey() + ": " + this.getValue() + "}";
  }

  public double getSpeed()
  {
    return this.getKey();
  }

  public boolean flagged()
  {
    return this.getValue();
  }

  public void flag()
  {
    this.setValue(true);
  }
}
