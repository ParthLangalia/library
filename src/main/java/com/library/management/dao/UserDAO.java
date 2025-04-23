package com.library.management.dao;

import com.library.management.model.*;
import com.library.management.util.*;
import org.hibernate.*;
import java.util.*;
import javax.persistence.*;
import javax.persistence.Query;

public class UserDAO {
	private EntityManager em;

	public UserDAO(EntityManager em) {
		super();
		this.em = em;
	}

	public UserDAO() {
		super();
	}

	//CRUD OPERATIONS - USER
	//CREATE USER - 'C'
	public void insertUser(User newUser) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.persist(newUser);
			transaction.commit();
		} catch (Exception e) {
			if(transaction.isActive()){
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
	
	//fetch books by id - 'R'
	public User fetchUserById(int userId) {
		User newUser = null;
		try{
			newUser = em.find(User.class, userId);
		}catch(Exception e){
			e.printStackTrace();
		}
		return newUser;
	}
	
	//fetch user by name
	public User fetchUserByUsername(String newUsername) {
		User newUser = null;
		try {
			newUser = em.find(User.class, newUsername);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return newUser;
	}
	
	//fetch user by email-id
	public User fetchUserByEmail(String newEmail) { 
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = : email", User.class);
		query.setParameter("title", newEmail);
		return query.getSingleResult();
	}
	
	//FETCH USER -'R'
	public List<User> fetchAllUsers() {
		List<User> userList = null;
		try{
			TypedQuery<User> query = em.createQuery("FROM User", User.class);
			userList = query.getResultList();
		}catch(Exception e){
			e.printStackTrace();
		}
		return userList;
	}
	
	//UPDATE USER - 'U'
	public void updateUser(int userId, String newUsername, String newPassword, String newEmail) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			User newUser = em.find(User.class, userId);
			if(newUser != null){
				if(newUsername != null && !newUsername.isEmpty()){
					newUser.setUsername(newUsername);
				}
				if(newPassword != null && !newPassword.isEmpty()){
					newUser.setPassword(newPassword);
				}
				if(newEmail != null && !newEmail.isEmpty()){
					newUser.setEmail(newEmail);
				}
				em.merge(newUser);
			}else {
				System.out.println("User is not present in the database!");
			}
			transaction.commit();
			
		} catch (Exception e) {
			if(transaction.isActive()) transaction.rollback();
			e.printStackTrace();
		}
	}
	
	public void updateUserByName(String newUsername, String newPassword, String newEmail){
		em.getTransaction().begin();
		Query query = em.createQuery("UPDATE User u SET u.password = :newPassword, u.email = :newEmail WHERE u.username = :newUsername");
		query.setParameter("newUsername", newUsername);
		query.setParameter("newPassword", newPassword);
		query.setParameter("newEmail", newEmail);
		int updatedCount = query.executeUpdate();
		em.getTransaction().commit();
		System.out.println(updatedCount + " user(s) updated ");
	}
	
	public void updateUserByEmail(String newUsername, String newPassword, String newEmail){
		em.getTransaction().begin();
		Query query = em.createQuery("UPDATE User u SET u.username = :newUsername, b.password = :newPassword WHERE u.email = :newEmail");
		query.setParameter("newUser", newUsername);
		query.setParameter("newPass", newPassword);
		query.setParameter("newEmail", newEmail);
		int updatedCount = query.executeUpdate();
		em.getTransaction().commit();
		System.out.println(updatedCount + " user(s) updated ");
	}
	
	//DELETE USER - 'D'
	public void deleteUser(int userId) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			User newUser = em.find(User.class, userId);
			if(newUser != null){
				em.remove(newUser);
			}else {
				System.out.println("User not present in db");
			}
			transaction.commit();
		}catch (Exception e) {
			if(transaction.isActive()) transaction.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteUserByName(String newUsername) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			User newUser = em.find(User.class, newUsername);
			if(newUser != null){
				em.remove(newUser);
			}else {
				System.out.println("User not present in db");
			}
			transaction.commit();
		}catch (Exception e) {
			if(transaction.isActive()) transaction.rollback();
			e.printStackTrace();
		}
	}
	
	public void deleteUserByEmail(String newEmail) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			User newUser = em.find(User.class, newEmail);
			if(newUser != null){
				em.remove(newUser);
			}else {
				System.out.println("User not present in db");
			}
			transaction.commit();
		}catch (Exception e) {
			if(transaction.isActive()) transaction.rollback();
			e.printStackTrace();
		}
	}

}