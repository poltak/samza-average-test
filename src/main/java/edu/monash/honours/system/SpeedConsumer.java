package edu.monash.honours.system;

import edu.monash.honours.util.SpeedReadingPair;
import org.apache.samza.Partition;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.SystemStreamPartition;
import org.apache.samza.util.BlockingEnvelopeMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class SpeedConsumer extends BlockingEnvelopeMap
{
  private final ServerSocket serverSocket;
  private final SystemStreamPartition systemStreamPartition;

  public SpeedConsumer(final String systemName, final String streamName, final int listeningPort)
    throws IOException
  {
    this.systemStreamPartition = new SystemStreamPartition(systemName, streamName, new Partition((0)));
    this.serverSocket = new ServerSocket(listeningPort);
  }

  @Override
  public void start()
  {
    new Thread(
      () -> {
        try {
          SpeedConsumer.this.listenOnPort();
        } catch (InterruptedException e) {
          SpeedConsumer.this.stop();
        }
      }
    );
  }

  private void listenOnPort() throws InterruptedException
  {
    InputStream inputStream;

    try {
      while ((inputStream = serverSocket.accept().getInputStream()) != null) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        double speedReading = Double.valueOf(bufferedReader.readLine());

        put(systemStreamPartition,
            new IncomingMessageEnvelope(systemStreamPartition, null, null, new SpeedReadingPair(speedReading, false)));

        bufferedReader.close();
      }
    } catch (IOException e) {
      put(systemStreamPartition, new IncomingMessageEnvelope(systemStreamPartition, null, null,
                                                             "ERROR: Cannot read from port" + e.getMessage()));
    }
  }

  @Override
  public void stop()
  {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void register(final SystemStreamPartition systemStreamPartition, final String startingOffset)
  {
    super.register(systemStreamPartition, startingOffset);
  }

}
