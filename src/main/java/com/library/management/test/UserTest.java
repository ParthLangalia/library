package com.library.management.test;

import com.library.management.dao.*;
import com.library.management.model.*;
import java.util.*;

import javax.persistence.Persistence;
import javax.persistence.*;

public class UserTest {
	public static void main(String[] a){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
		EntityManager em = emf.createEntityManager();
		
		UserDAO uDAO = new UserDAO(em);
		
//		User newUser = new User();
//		newUser.setUsername("Harsh");
//		newUser.setPassword("Demo Password 42");
//		newUser.setEmail("harsh4@example.com");
//		newUser.setRole("user");
		
		
		//insert new user
//		uDAO.insertUser(newUser);
//		System.out.println("New user inserted");
		
		//update user
//		uDAO.updateUser(1002, "new demo user", "new pass 123", "newemail@example.com");
//		System.out.println("user info updated");
		
		//delete user
//		uDAO.deleteUser(1002);
//		System.out.println("user deleted");
		
//		User u1 = uDAO.fetchUserbyId(10);
//		System.out.println("User fetched : " + u1);
		
//		List<User> users = uDAO.fetchAllUsers();
//		for(User user: users ) {
//		 	System.out.println(user);
//		}
		
//		//for admin roles
//		 /*
//		 
//		 	uDAO.updateUserRole(3, "user");
//		 	System.out.println("user role updated!");
//		 
//		 
//		 	User fetchedUser = userDAO.fetchUserById(4);
//		 	if(fetchedUser != null) {
//		 		System.out.println(fetchedUser);
//		 	}
//		 */
		
		em.close();
		emf.close();
		
	}
}