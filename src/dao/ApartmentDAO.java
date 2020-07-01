package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;

import beans.Apartment;
import beans.ApartmentStatus;
import beans.User;

public class ApartmentDAO {
	
	private HashMap<String, Apartment> apartments = new HashMap<>();
	
	public ApartmentDAO() {
		
	}
	
	public ApartmentDAO(String contextPath) {
		apartments = new HashMap<String, Apartment>();
		loadApartment(contextPath);
	}
	
	public Apartment findApartmentById(String id) {
		for(Apartment a: apartments.values()) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE)) {
				if(id.equals(a.getIdOne().toString())) {
					return a;
				}
			}
		}
		return null;
	}
	
	public ArrayList<Apartment> activeApartments(){
		ArrayList<Apartment> active = new ArrayList<Apartment>();
		
		for(Apartment a: apartments.values()) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE)) {
				active.add(a);
			}
		}
		return active;
	}
	
	public ArrayList<Apartment> allApartments(){
		ArrayList<Apartment> all = new ArrayList<Apartment>();
		
		for(Apartment a: apartments.values()) {
			all.add(a);
		}
		
		return all;
	}
	
	public ArrayList<Apartment> allActiveApartments(){
		ArrayList<Apartment> active = new ArrayList<Apartment>();
		
		for(Apartment a: apartments.values()) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE))
			active.add(a);
		}
		
		return active;
	}
	
	
	public ArrayList<Apartment> allActiveApartmentsFromHost(User host){
		ArrayList<Apartment> active = new ArrayList<Apartment>();
		
		for(Apartment a: apartments.values()) {
			if(a.getStatus().equals(ApartmentStatus.ACTIVE) && (a.getHost().equals(host.getUsername()))) {
			active.add(a);
			}
			
		}
		
		return active;
	}
	
	
	public ArrayList<Apartment> allInactiveApartmentsFromHost(User host){
		ArrayList<Apartment> active = new ArrayList<Apartment>();
		
		for(Apartment a: apartments.values()) {
			if(a.getStatus().equals(ApartmentStatus.INACTIVE) && (a.getHost().equals(host.getUsername()))) {
			active.add(a);
			
			}
			
		}
		
		return active;
	}
	

	@SuppressWarnings("unchecked")
	private void loadApartment(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/apartments.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Apartment.class);
			mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			apartments = ((HashMap<String, Apartment>) mapper.readValue(file, type));

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(apartments);

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
	
	public void saveApartment(String path, ApartmentDAO apartments) {
		
		
		File f = new File(path + "/apartments.txt");
		System.out.println(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String kategString = objectMapper.writeValueAsString(apartments.getApartments());
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
	

	public HashMap<String, Apartment> getApartments() {
		return apartments;
	}

	public void setApartments(HashMap<String, Apartment> apartments) {
		this.apartments = apartments;
	}
	
	

}
