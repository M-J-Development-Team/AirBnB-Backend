package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Apartment;
import beans.ApartmentComment;
import beans.Reservation;

public class ApartmentCommentDAO {

	private HashMap<String, ApartmentComment> comments = new HashMap<>();
	
	public ApartmentCommentDAO() {}
	
	
	public ApartmentCommentDAO(String contextPath) {
		comments  = new HashMap<String, ApartmentComment>();
		loadComments(contextPath);

	}
	
	
	
	public HashMap<String, ApartmentComment> getComments() {
		return comments;
	}


	public void setComments(HashMap<String, ApartmentComment> comments) {
		this.comments = comments;
	}
	
	public ArrayList<ApartmentComment> getAllCommentsFromApartment(Apartment a){
		ArrayList<ApartmentComment> retComments = new ArrayList<>();
		for(ApartmentComment comment : comments.values()) {
			if(comment.getApartment().getIdOne().equals(a.getIdOne())) {
				retComments.add(comment);
			}
		}
		return retComments;
	}


	public void saveComment(String path, ApartmentCommentDAO comments) {
		
		
		File f = new File(path + "/comments.txt");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String kategString = objectMapper.writeValueAsString(comments.getComments());
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
		private void loadComments(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/comments.txt");
			in = new BufferedReader(new FileReader(file));
	
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, ApartmentComment.class);
			mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			comments = ((HashMap<String, ApartmentComment>) mapper.readValue(file, type));
	
		} catch (FileNotFoundException fnfe) {
			try {
				System.out.println("im writing a new file"+file);
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(comments);
	
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

