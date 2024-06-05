package ui;

import server.ServerFacade;

public class Client {
  private String visitorName = null;
  private final ServerFacade server;
  private final String serverUrl;
  private State state = State.SIGNEDOUT;

  public Client(String serverUrl) {
    this.serverUrl = serverUrl;
    this.server = new ServerFacade();
  }
}
