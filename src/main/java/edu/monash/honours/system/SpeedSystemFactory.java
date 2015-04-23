package edu.monash.honours.system;

import org.apache.samza.SamzaException;
import org.apache.samza.config.Config;
import org.apache.samza.metrics.MetricsRegistry;
import org.apache.samza.system.SystemAdmin;
import org.apache.samza.system.SystemConsumer;
import org.apache.samza.system.SystemFactory;
import org.apache.samza.system.SystemProducer;
import org.apache.samza.util.SinglePartitionWithoutOffsetsSystemAdmin;

import java.io.IOException;

public class SpeedSystemFactory implements SystemFactory
{
  private static final String OUTPUT_STREAM_NAME = "speeds";

  @Override
  public SystemConsumer getConsumer(final String systemName, final Config config, final MetricsRegistry metricsRegistry)
  {
    int listeningPort = Integer.getInteger(config.get("systems." + systemName + ".listeningPort"));

    try {
      return new SpeedConsumer(systemName, OUTPUT_STREAM_NAME, listeningPort);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public SystemProducer getProducer(final String s, final Config config, final MetricsRegistry metricsRegistry)
  {
    throw new SamzaException("You cannot produce to this feed!");
  }

  @Override
  public SystemAdmin getAdmin(final String s, final Config config)
  {
    return new SinglePartitionWithoutOffsetsSystemAdmin();
  }
}
