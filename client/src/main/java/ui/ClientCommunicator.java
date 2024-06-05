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

  private void throwIfNotSuccessful(HttpURLConnection http) throws ExceptionResult, IOException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ExceptionResult(status, "failure: " + status);
    }
    if (http.getResponseCode() < 200 || http.getResponseCode() >= 300) {
      throw new ExceptionResult(http.getResponseCode(), http.getResponseMessage());
    }
  }

  private boolean isSuccessful(int status) {
    return status >= 200 && status < 300;
  }

}
