package edu.monash.honours.task;

import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;

public class DataProcessorStreamTask implements StreamTask
{
  private static final SystemStream OUTPUT_STREAM = new SystemStream("kafka", "samza-test-output");

  private double speedSum;
  private int    messageCount;

  public DataProcessorStreamTask()
  {
    this.speedSum = 0;
    this.messageCount = 0;
  }

  public void process(final IncomingMessageEnvelope incomingMessageEnvelope, final MessageCollector messageCollector,
                      final TaskCoordinator taskCoordinator) throws Exception
  {
    double speed = Double.valueOf((String) incomingMessageEnvelope.getMessage());

    this.speedSum += speed;
    this.messageCount++;

    double average = speedSum / (double) messageCount;

    messageCollector.send(new OutgoingMessageEnvelope(OUTPUT_STREAM, average));
  }
}
