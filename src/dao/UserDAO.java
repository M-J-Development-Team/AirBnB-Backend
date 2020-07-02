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
import beans.ApartmentStatus;
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
		User mina = new User("mina", "admin", "mina", "maras", Gender.FEMALE, Role.GUEST, null, new ArrayList<String>());
		User marina = new User("marina", "admin", "marina", "maras", Gender.FEMALE, Role.HOST, null, new ArrayList<String>());
		
		users.put("admin", admin);
		users.put("mina", mina);
		users.put("marina", marina);
		
		
	}
	
	public ArrayList<Apartment> myActiveApartments(User u) {
		u = findUserByUsername(u.getUsername());
		ArrayList<Apartment> myActiveApartments = new ArrayList<Apartment>();
		
		for(Apartment a : u.getApartmentsToRent()) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE)) {
				myActiveApartments.add(a);
			}
		}
		
		return myActiveApartments;
	}
	
	public ArrayList<Apartment> myInactiveApartments(User u) {
		u = findUserByUsername(u.getUsername());
		ArrayList<Apartment> myInctiveApartments = new ArrayList<Apartment>();
		
		for(Apartment a : u.getApartmentsToRent()) {
			if(a.getStatus().equals(ApartmentStatus.INACTIVE)) {
				myInctiveApartments.add(a);
			}
		}
		
		return myInctiveApartments;
	}
	
	@SuppressWarnings("unchecked")
	public void loadUser(String contextPath) {
		
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		
		try {
			file = new File(contextPath + "/users.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, User.class);
			mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			users = ((HashMap<String, User>) mapper.readValue(file, type));

			
		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(users);

				fileWriter.write(kategString);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public User find(User u) {
		for(User user: users.values()) {
			if(u.getUsername().equals(user.getUsername())) {
				if(u.getPassword().equals(user.getPassword())) {
					return user;
				} else {
					return null;
				}
			}
		}
		return null;
	}
	
	public HashMap<String,User> getUsers() {
		return users;
	}
	
	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}
	
	
	public User findbyID(String idOne) {
		for(User user: users.values()) {
			if(idOne.equals(user.getIdOne())) {
				return user;
			}
			
		}
		return null;
	}
	
	public User findUserByUsername(String username) {
		if(!users.containsKey(username))
		{
			return null;
		}
		
		User u = users.get(username);
		return u;
	}
	
	
	public void saveUser(String path, UserDAO users) {
			
			
			File f = new File(path + "/users.txt");
			System.out.println(path);
			FileWriter fileWriter = null;
			try {
				fileWriter = new FileWriter(f);
	
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(users.getUsers());
				fileWriter.write(kategString);
				fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	

}
