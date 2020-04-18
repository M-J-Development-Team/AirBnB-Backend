package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import beans.Amenities;
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
	
	public Amenities findById(String name) {
		for(Amenities a: amenities.values()) {
			if(name.equals(a.getName())) {
				return a;
			}
			
		}
		return null;
	}
	
	public ArrayList<Amenities> getAllAmenities(){
		ArrayList<Amenities> allAmenities = new ArrayList<Amenities>();
		
		for(Amenities a: amenities.values()) {
			allAmenities.add(a);
		}
		return allAmenities;
	}



	public AmenitiesDAO(String contextPath) {
		amenities = new HashMap<String, Amenities>();
		loadAmenities(contextPath);
	}

}
