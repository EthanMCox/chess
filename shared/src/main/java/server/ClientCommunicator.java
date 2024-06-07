package server;

import com.google.gson.Gson;
import exception.ExceptionResult;

import java.io.*;
import java.net.*;

public class ClientCommunicator {

  public static <T> T makeRequest (String method, String urlPath, Object request, Class<T> responseClass,
                                   String authToken) throws ExceptionResult {
    try {
      URL url = (new URI(urlPath)).toURL();
      HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
      httpConnection.setReadTimeout(5000);
      httpConnection.setRequestMethod(method);
      if (authToken != null) {
        httpConnection.setRequestProperty("authorization", authToken);
      }
      if (method.equals("POST") || method.equals("PUT")) {
        httpConnection.setDoOutput(true);
        writeBody(request, httpConnection);
      }
      httpConnection.connect();
      throwIfNotSuccessful(httpConnection);
      return readBody(httpConnection, responseClass);
    } catch (Exception ex) {
        throw new ExceptionResult(500, ex.getMessage());
    }
  }

  private static void writeBody(Object request, HttpURLConnection http) throws IOException {
    if (request != null) {
      http.addRequestProperty("Content-Type", "application/json");
      String reqData = new Gson().toJson(request);
      try (OutputStream reqBody = http.getOutputStream()) {
        reqBody.write(reqData.getBytes());
      }
    }
  }

  private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
    T response = null;
    if (http.getContentLength() < 0) {
      try (InputStream respBody = http.getInputStream()) {
        InputStreamReader reader = new InputStreamReader(respBody);
        if (responseClass != null) {
          response = new Gson().fromJson(reader, responseClass);
        }
      }
    }
    return response;
  }

  private static void throwIfNotSuccessful(HttpURLConnection http) throws ExceptionResult, IOException {
    var status = http.getResponseCode();
    if (!isSuccessful(status)) {
      throw new ExceptionResult(status, "failed to execute: " + status);
    }
  }

  private static boolean isSuccessful(int status) {
    return status >= 200 && status < 300;
  }

}
