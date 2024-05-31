package util;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class Security {
  public static boolean passwordMatches(UserData user, String password) {
    return BCrypt.checkpw(password, user.password());
  }

  public static String encyptPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt());
  }
}
