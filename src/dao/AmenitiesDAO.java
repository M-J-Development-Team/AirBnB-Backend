package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import beans.Amenities;
import beans.AmenityStatus;
import beans.Apartment;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class AmenitiesDAO {
	
	private HashMap<String, Amenities> amenities = new HashMap<>();
	
	public AmenitiesDAO() {
		
	}
	
	public HashMap<String, Amenities> getAmenities() {
		return amenities;
	}



	public void setAmenities(HashMap<String, Amenities> amenities) {
		this.amenities = amenities;
	}
	
	public Amenities findById(String id) {
		for(Amenities a: amenities.values()) {
			if(id.equals(a.getIdOne().toString())) {
				return a;
			}
			
		}
		return null;
	}
	
	public ArrayList<Amenities> getAllAmenities(){
		ArrayList<Amenities> allAmenities = new ArrayList<Amenities>();
		
		for(Amenities a: amenities.values()) {
			if(a.getAmenityStatus().equals(AmenityStatus.ACTIVE))
			allAmenities.add(a);
		}
		return allAmenities;
	}
	
	public Amenities findByName(String name) {
		for(Amenities a : amenities.values()) {
			if(name.equals(a.getName())) {
				return a;
			}
		}
		
		return null;
	}
	
	public Collection<Amenities> findAmenitiesForApartment(Apartment a) {
		
		List<Amenities> returnlist =  new ArrayList<Amenities>();
		
		
		for(Amenities amenity : amenities.values()) {
			
			boolean contains = false;
			
			for(Amenities apartmentAmenity : a.getAmenities()) {
				if(amenity.getIdOne().equals(apartmentAmenity.getIdOne())) {
					contains = true;
				}else {
					contains = false;
				}
			}
			
			if(contains ==  false) {
				returnlist.add(amenity);
				System.out.println(amenity.getName());
			}
		
	}
		
		return returnlist;
	
	}

	public AmenitiesDAO(String contextPath) {
		amenities = new HashMap<String, Amenities>();
		loadAmenities(contextPath);
	}
	
	public void saveAmenities(String path, AmenitiesDAO amenities) {
		
		
		File f = new File(path + "/amenities.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String kategString = objectMapper.writeValueAsString(amenities.getAmenities());
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

	@SuppressWarnings("unchecked")
	public void loadAmenities(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/amenities.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Amenities.class);
			mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			amenities = ((HashMap<String, Amenities>) mapper.readValue(file, type));

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(amenities);

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

}
