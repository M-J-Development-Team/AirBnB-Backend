package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;

import beans.Apartment;
import beans.Gender;
import beans.Reservation;
import beans.Role;
import beans.User;

public class UserDAO {
	
	private HashMap<String, User> users = new HashMap<>();
	
	
	public UserDAO() {}
	
	public UserDAO(String contextPath) {
		
		users = new HashMap<String, User>();
		loadUser(contextPath);
		
		
		User admin = new User("admin","admin","Admin","Admin",Gender.FEMALE,Role.ADMIN,null,null);
		users.put("admin", admin);
		
		
	}
	
