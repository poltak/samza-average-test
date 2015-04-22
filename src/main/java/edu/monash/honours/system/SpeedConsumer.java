package edu.monash.honours.system;

import org.apache.samza.Partition;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.SystemStreamPartition;
import org.apache.samza.util.BlockingEnvelopeMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SpeedConsumer extends BlockingEnvelopeMap {
  private final BufferedReader        reader;
  private final SystemStreamPartition systemStreamPartition;

  public SpeedConsumer(final String systemName, final String streamName, final String filePath)
    throws FileNotFoundException {
    this.systemStreamPartition = new SystemStreamPartition(systemName, streamName, new Partition((0)));
    this.reader = new BufferedReader(new FileReader(filePath));
  }

  @Override
  public void start() {
    Thread fileReadingThread = new Thread(
      () -> {
        try {
          readLines();
        } catch (InterruptedException e) {
          stop();
        }
      }
    );

  }

  // TODO: extract speed from lines and send that out
  private void readLines() throws InterruptedException {
    String line;
    try {
      while ((line = reader.readLine()) != null) {
        put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null, line));
      }
    } catch (IOException e) {
      put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null,
                                                             "ERROR: Cannot read from file" + e.getMessage()));
    }
  }

  @Override
  public void stop() {
    try {
      this.reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void register(final SystemStreamPartition systemStreamPartition, final String startingOffset) {
    super.register(systemStreamPartition, startingOffset);
  }

}
