package server;

import exception.ExceptionResult;
import requests.*;
import results.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public LoginResult register(String username, String password, String email) throws ExceptionResult {
      var path = serverUrl + "/session";
      RegisterRequest request = new RegisterRequest(username, password, email);
      return ClientCommunicator.makeRequest("POST", path, request, LoginResult.class, null);
    }


}
