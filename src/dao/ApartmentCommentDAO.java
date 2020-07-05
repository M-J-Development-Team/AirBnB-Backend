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
import java.util.UUID;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import beans.Apartment;
import beans.ApartmentComment;
import beans.CommentStatus;
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
	
	public ArrayList<ApartmentComment> getAllCommentsFromApartment(String name){
		ArrayList<ApartmentComment> retComments = new ArrayList<>();
		for(ApartmentComment comment : comments.values()) {
			if(comment.getApartmentName().equals(name)) {
				retComments.add(comment);
			}
		}
		return retComments;
	}
	
	public Collection<ApartmentComment> getAllComments(){
		return comments.values();
	}
	
	public ArrayList<ApartmentComment> getAllCommentsFromApartmentApproved(String name){
		ArrayList<ApartmentComment> retComments = new ArrayList<>();
		for(ApartmentComment comment : comments.values()) {
			if(comment.getApartmentName().equals(name)) {
				if(comment.getStatus().equals(CommentStatus.VISIBLE)) {
				retComments.add(comment);
				}
			}
		}
		return retComments;
	}
	
	public ArrayList<ApartmentComment> getAllCommentsUnapprovedForHost(String username,ArrayList<Apartment> hostApartments){
		ArrayList<ApartmentComment> retComments = new ArrayList<>();
		for(Apartment a : hostApartments) {
				for(ApartmentComment comment : comments.values()) {
					if(comment.getApartmentName().equals(a.getName())) {
							if(comment.getStatus().equals(CommentStatus.HIDDEN)) {
								retComments.add(comment);
								}
							}
						}
					}
	return retComments;
	}
	
	public boolean setCommentToVisible(UUID idOne) {
		
		for(ApartmentComment comment : comments.values()) {
			if(comment.getIdOne().equals(idOne)) {
				comment.setStatus(CommentStatus.VISIBLE);
				return true;
				}
			}	
		return false;
	}
	
	public boolean setCommentToDeclined(UUID idOne) {
		
		for(ApartmentComment comment : comments.values()) {
			if(comment.getIdOne().equals(idOne)) {
				comment.setStatus(CommentStatus.DECLINED);
				return true;
				}
			}	
		return false;
	}
		
	public boolean checkHasCommented(String idOne,String apartmentId,ServletContext context) {
		
		ApartmentCommentDAO apartmentCommentDAO = (ApartmentCommentDAO) context.getAttribute("ApartmentCommentDAO");
		context.setAttribute("UserDAO", new UserDAO(context.getRealPath("")));
		UserDAO userDAO = (UserDAO) context.getAttribute("UserDAO");
		
		if(apartmentCommentDAO.getAllComments() != null) {
		if(apartmentCommentDAO.getAllCommentsFromApartment(UUID.fromString(apartmentId)) != null) {
		for(ApartmentComment comment : apartmentCommentDAO.getAllCommentsFromApartment(UUID.fromString(apartmentId))) {
			if(comment.getGuest().equals(userDAO.findbyID(idOne).getUsername())) {
				return true;
			}
		}
		}	
	}
		return false;
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

