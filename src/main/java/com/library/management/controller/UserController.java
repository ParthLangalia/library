package com.library.management.controller;

import com.library.management.model.User;
import com.library.management.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/users")
public class UserController {
   private UserService userService = new UserService();
   // CREATE
   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response insertUser(User user) {
       userService.insertUser(user);
       return Response.status(Response.Status.CREATED).entity("User added successfully").build();
   }
   // READ
   @GET
   @Path("/{id}")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getUserById(@PathParam("id") int id) {
       User user = userService.fetchUserById(id);
       if (user != null) {
           return Response.ok(user).build();
       } else {
           return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
       }
   }
   @GET
   @Path("/username/{username}")
   @Produces(MediaType.APPLICATION_JSON)
   public User getUserByUsername(@PathParam("username") String username) {
       return userService.fetchUserByUsername(username);
   }
   @GET
   @Path("/email/{email}")
   @Produces(MediaType.APPLICATION_JSON)
   public User getUserByEmail(@PathParam("email") String email) {
       return userService.fetchUserByEmail(email);
   }
   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public List<User> getAllUsers() {
       return userService.fetchAllUsers();
   }
   // UPDATE
   @PUT
   @Path("/{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response updateUser(@PathParam("id") int id, User user) {
       userService.updateUser(id, user.getUsername(), user.getPassword(), user.getEmail());
       return Response.ok("User updated successfully").build();
   }
   @PUT
   @Path("/username/{username}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response updateUserByName(@PathParam("username") String username, User user) {
       userService.updateUserByName(username, user.getPassword(), user.getEmail());
       return Response.ok("User with username = " + username + " updated").build();
   }
   @PUT
   @Path("/email/{email}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response updateUserByEmail(@PathParam("email") String email, User user) {
       userService.updateUserByEmail(email, user.getUsername(), user.getPassword());
       return Response.ok("User with email = " + email + " updated").build();
   }
   // DELETE
   @DELETE
   @Path("/{id}")
   public Response deleteUserById(@PathParam("id") int id) {
       userService.deleteUser(id);
       return Response.ok("User deleted successfully").build();
   }
   @DELETE
   @Path("/username/{username}")
   public Response deleteUserByUsername(@PathParam("username") String username) {
       userService.deleteUserByName(username);
       return Response.ok("User deleted by username").build();
   }
   @DELETE
   @Path("/email/{email}")
   public Response deleteUserByEmail(@PathParam("email") String email) {
       userService.deleteUserByEmail(email);
       return Response.ok("User deleted by email").build();
   }
}