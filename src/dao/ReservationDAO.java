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
import beans.Reservation;
import beans.ReservationStatus;
import beans.User;

public class ReservationDAO {
	
	private HashMap<String, Reservation> reservations = new HashMap<>();
	
	public ReservationDAO() {
		
	}
	
	public ReservationDAO(String contextPath) {
		System.out.println("loading reservations with given contextPath"+contextPath);
		reservations = new HashMap<String, Reservation>();
		loadReservation(contextPath);

	}
	
	public Reservation findReservationById(UUID id) {
		for(Reservation r : reservations.values()) {
			if(id.equals(r.getIdOne())) {
				return r;
			}
		}
		
		return null;
	}
	

	
	public ArrayList<Reservation> createdReservations() {
		ArrayList<Reservation> created = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getReservationStatus().equals(ReservationStatus.CREATED)) {
				created.add(r);
			}
		}
		
		return created;
	}
	
	public ArrayList<Reservation> deniedReservations() {
		ArrayList<Reservation> denied = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getReservationStatus().equals(ReservationStatus.DENIED)) {
				denied.add(r);
			}
		}
		
		return denied;
	}
	
	public ArrayList<Reservation> cancelledReservations() {
		ArrayList<Reservation> cancelled = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getReservationStatus().equals(ReservationStatus.CANCLED)) {
				cancelled.add(r);
			}
		}
		
		return cancelled;
	}
	
	public ArrayList<Reservation> acceptedReservations() {
		ArrayList<Reservation> accepted = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getReservationStatus().equals(ReservationStatus.ACCEPTED)) {
				accepted.add(r);
			}
		}
		
		return accepted;
	}
	
	public ArrayList<Reservation> completedReservations() {
		ArrayList<Reservation> completed = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getReservationStatus().equals(ReservationStatus.COMPLETED)) {
				completed.add(r);
			}
		}
		
		return completed;
	}
	
	public ArrayList<Reservation> allReservations() {
		ArrayList<Reservation> all = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
				all.add(r);
		}
		
		return all;
	}
	
	public ArrayList<Reservation> allMyReservations(String username) {
		ArrayList<Reservation> myReservations = new ArrayList<Reservation>();
		
		for(Reservation r : reservations.values()) {
			if(r.getGuest().equals(username)) {
				myReservations.add(r);
			}
		}
		
		return myReservations;
	}
	
	public ArrayList<User> allGuestsForMyApartments(String username,ArrayList<Apartment> myapartments,UserDAO userdao,ApartmentDAO apartmentdao) {	
		ArrayList<User> guests = new ArrayList<User>();
		Collection<Reservation> res = reservations.values();
		if(myapartments != null && res != null) {
		for(Reservation r : res) {
			if(myapartments.contains(apartmentdao.findApartmentByName(r.getApartment()))) {
				guests.add(userdao.findUserByUsername(r.getGuest()));
			}
		}
	}	
		
	return guests;
	}
	
	public ArrayList<Reservation> allReservationsForMyApartments(User host) {	
		
		ArrayList<Reservation> reservationsList = new ArrayList<Reservation>();
		for(String r : host.getReservations()) {
			Reservation res = findReservationById(UUID.fromString(r));
			reservationsList.add(res);
		}
		
		return reservationsList;
	}
	
	@SuppressWarnings("unchecked")
	private void loadReservation(String contextPath) {
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(contextPath + "/reservations.txt");
			in = new BufferedReader(new FileReader(file));

			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Reservation.class);
			mapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			reservations = ((HashMap<String, Reservation>) mapper.readValue(file, type));

		} catch (FileNotFoundException fnfe) {
			try {
				System.out.println("im writing a new file"+file);
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String kategString = objectMapper.writeValueAsString(reservations);

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
	
	public void saveReservation(String path, ReservationDAO reservations) {
		
		
		File f = new File(path + "/reservations.txt");
		System.out.println("HERE"+path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String kategString = objectMapper.writeValueAsString(reservations.getReservations());
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

	public HashMap<String, Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(HashMap<String, Reservation> reservations) {
		this.reservations = reservations;
	}
	
	

	
}
