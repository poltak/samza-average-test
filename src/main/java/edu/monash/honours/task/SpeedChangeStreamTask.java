package edu.monash.honours.task;

import edu.monash.honours.util.SpeedReadingPair;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;

public class SpeedChangeStreamTask implements StreamTask
{
  private static final SystemStream FLAGGED_OUTPUT = new SystemStream("kafka", "flagged-output");
  private static final SystemStream FINAL_OUTPUT   = new SystemStream("kafka", "final-output");

  private static int totalMessageCount = 0;
  private int messageCount;

  public SpeedChangeStreamTask()
  {
    this.messageCount = 0;
  }


  /**
   * Listens on a stream providing speed readings, and processes those speed readings to see if there are any abnormally
   * fast rates of change.
   * <p/>
   * Forwards on speed value to flagged stream if any abnormally fast rates of change detected, else forwards on speed
   * for further processing.
   */
  public void process(final IncomingMessageEnvelope incomingMessageEnvelope, final MessageCollector messageCollector,
                      final TaskCoordinator taskCoordinator) throws Exception
  {
    SpeedReadingPair speedReadingPair = (SpeedReadingPair) incomingMessageEnvelope.getMessage();
    this.messageCount++;
    totalMessageCount += this.messageCount;

    // TODO: Detect rate of change here


    // Send speed on for further processing
    messageCollector.send(new OutgoingMessageEnvelope(FINAL_OUTPUT, speedReadingPair));
  }
}
