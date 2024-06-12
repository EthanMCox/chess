package sharedutil;

import com.google.gson.Gson;

public class JsonSerializer {
  public static String serialize(Object object) {
    var serializer = new Gson();
    return serializer.toJson(object);
  }

  public static <T> T deserialize(String json, Class<T> type) {
    var serializer = new Gson();
    return serializer.fromJson(json, type);
  }
}
