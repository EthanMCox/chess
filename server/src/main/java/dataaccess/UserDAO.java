package dataaccess;

import model.UserData;

interface UserDAO {
  void clear();
  void createUser(String username, String password, String email);
  UserData getUser(String username);
}
