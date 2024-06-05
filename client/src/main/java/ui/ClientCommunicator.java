package ui;

import exception.ExceptionResult;

import java.io.*;
import java.net.*;

public class ClientCommunicator {

  public static <T> T makeRequest (String method, String URLPath, Object request, Class<T> responseClass) throws ExceptionResult {
    try {
      URL url = (new URI("http://localhost:8080" + URLPath)).toURL();
      HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
      httpConnection.setReadTimeout(5000);
      httpConnection.setRequestMethod(method);
      if (method.equals("POST") || method.equals("PUT")) {
        httpConnection.setDoOutput(true);
      }
      httpConnection.connect();
    } catch (Exception ex) {
      throw new ExceptionResult(500, ex.getMessage());
    }

    return null;
  }

}
