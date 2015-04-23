package edu.monash.honours.system;

import java.net.ServerSocket;
import org.apache.samza.config.Config;

public class SpeedFeed
{
  private final ServerSocket listeningSocket;
  private final int port;

  public SpeedFeed(final String systemName)
  {
  	this.port = config.get("systems." + systemName + ".listeningPort");
  	listeningSocket = new ServerSocket(this.port);
  }

  public void start() {
		new Thread() {
			() -> {
				try {
					Socket socket;
					while ((socket = listeningSocket.accept()) != null) {
						socket.toString();
					}
				} catch (IOException e) {

				}
			}
		}.start();
  }
}
