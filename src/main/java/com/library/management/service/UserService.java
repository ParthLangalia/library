package com.library.management.service;

import com.library.management.dao.UserDAO;
import com.library.management.model.User;
import java.util.List;

public class UserService {
   private UserDAO userDAO;
   public UserService() {
       this.userDAO = new UserDAO();
   }
   // CREATE
   public void insertUser(User user) {
       userDAO.insertUser(user);
   }
   // READ
   public User fetchUserById(int userId) {
       return userDAO.fetchUserById(userId);
   }
   public User fetchUserByUsername(String username) {
       return userDAO.fetchUserByUsername(username);
   }
   public User fetchUserByEmail(String email) {
       return userDAO.fetchUserByEmail(email);
   }
   public List<User> fetchAllUsers() {
       return userDAO.fetchAllUsers();
   }
   // UPDATE
   public void updateUser(int userId, String username, String password, String email) {
       userDAO.updateUser(userId, username, password, email);
   }
   public void updateUserByName(String username, String password, String email) {
       userDAO.updateUserByName(username, password, email);
   }
   public void updateUserByEmail(String email, String username, String password) {
       userDAO.updateUserByEmail(email, username, password);
   }
   // DELETE
   public void deleteUser(int userId) {
       userDAO.deleteUser(userId);
   }
   public void deleteUserByName(String username) {
       userDAO.deleteUserByName(username);
   }
   public void deleteUserByEmail(String email) {
       userDAO.deleteUserByEmail(email);
   }
}