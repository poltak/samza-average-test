package edu.monash.honours.task;

import edu.monash.honours.util.SpeedReadingPair;
import org.apache.samza.system.IncomingMessageEnvelope;
import org.apache.samza.system.OutgoingMessageEnvelope;
import org.apache.samza.system.SystemStream;
import org.apache.samza.task.MessageCollector;
import org.apache.samza.task.StreamTask;
import org.apache.samza.task.TaskCoordinator;

import java.util.Map;

public class SpeedCheckStreamTask implements StreamTask
{
  // Constants
  private static final SystemStream FLAGGED_OUTPUT     = new SystemStream("kafka", "flagged-output");
  private static final SystemStream SPEED_CHECK_OUTPUT = new SystemStream("kafka", "speed-check-output");
  private static final double       SPEED_UPPER_LIMIT  = 90;
  private static final double       SPEED_LOWER_LIMIT  = 0;

  private static int totalMessageCount = 0;
  private int messageCount;


  public SpeedCheckStreamTask()
  {
    this.messageCount = 0;
  }


  /**
   * Listens on a stream providing speed readings, and processes those speed readings depending on whether or not they
   * exceed a specified threshold.
   * <p/>
   * Forwards on speed value to flagged stream if that value exceeds thresholds, else forwards on speed for further
   * processing.
   */
  public void process(final IncomingMessageEnvelope incomingMessageEnvelope, final MessageCollector messageCollector,
                      final TaskCoordinator taskCoordinator) throws Exception
  {
    /*
    SpeedReadingPair speedReadingPair = (SpeedReadingPair) incomingMessageEnvelope.getMessage();
    this.messageCount++;  // Keep count of number of messages that come through here
    totalMessageCount += this.messageCount; // Keep count of total number of messages received on this stream.


    if (speedReadingPair.getSpeed() < SPEED_LOWER_LIMIT || speedReadingPair.getSpeed() > SPEED_UPPER_LIMIT) {
      // Flag speeds that exceed thresholds
      speedReadingPair.flag();
      messageCollector.send(new OutgoingMessageEnvelope(FLAGGED_OUTPUT, speedReadingPair));
    }

    // Send speed on for further processing
    messageCollector.send(new OutgoingMessageEnvelope(SPEED_CHECK_OUTPUT, speedReadingPair));
    */
    Map<String, Object> outgoingMap = SpeedReadingPair.toMap((SpeedReadingPair) incomingMessageEnvelope.getMessage());
    messageCollector.send(new OutgoingMessageEnvelope(SPEED_CHECK_OUTPUT, outgoingMap));

  }
}
