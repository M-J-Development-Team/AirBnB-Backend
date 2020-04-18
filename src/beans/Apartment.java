package beans;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

public class Apartment {
	
	private ApartmentType type;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<Date> datesForRenting =  new ArrayList<>();
	private ArrayList<Date> freeDates = new ArrayList<>();
	private String host;
	private ArrayList<ApartmentComment> comments = new ArrayList<>();
	private ArrayList<String> photoPath = new ArrayList<>();
	private UUID idOne = UUID.randomUUID();
	private float price;
	private Time checkInTime = new Time(2);
	private Time checkOutTime = new Time(10);
	private ApartmentStatus status;
	private ArrayList<Amenities> amenities = new ArrayList<>();
	private ArrayList<Reservation> reservations = new ArrayList<>();
	
	@PostConstruct
	public void init() {
		
		if( this.type.equals(ApartmentType.ROOM)) {
			this.numberOfRooms = 1;
		}
		
	}
	
	
	public Apartment(ApartmentType type, int numberOfRooms, int numberOfGuests, Location location,
			ArrayList<Date> datesForRenting, ArrayList<Date> freeDates, String host,
			ArrayList<ApartmentComment> comments, ArrayList<String> photoPath, UUID idOne, float price, Time checkInTime,
			Time checkOutTime, ApartmentStatus status, ArrayList<Amenities> amenities,
			ArrayList<Reservation> reservations) {
		super();
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.location = location;
		this.datesForRenting = datesForRenting;
		this.freeDates = freeDates;
		this.host = host;
		this.comments = comments;
		this.photoPath = photoPath;
		this.idOne = idOne;
		this.price = price;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.status = status;
		this.amenities = amenities;
		this.reservations = reservations;
	}
	
	public Apartment() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApartmentType getType() {
		return type;
	}

	public void setType(ApartmentType type) {
		this.type = type;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getNumberOfGuests() {
		return numberOfGuests;
	}

	public void setNumberOfGuests(int numberOfGuests) {
		this.numberOfGuests = numberOfGuests;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public ArrayList<Date> getDatesForRenting() {
		return datesForRenting;
	}

	public void setDatesForRenting(ArrayList<Date> datesForRenting) {
		this.datesForRenting = datesForRenting;
	}

	public ArrayList<Date> getFreeDates() {
		return freeDates;
	}

	public void setFreeDates(ArrayList<Date> freeDates) {
		this.freeDates = freeDates;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public ArrayList<ApartmentComment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<ApartmentComment> comments) {
		this.comments = comments;
	}

	public ArrayList<String> getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(ArrayList<String> photoPath) {
		this.photoPath = photoPath;
	}

	public UUID getIdOne() {
		return idOne;
	}

	public void setIdOne(UUID idOne) {
		this.idOne = idOne;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Time getCheckInTime() {
		return checkInTime;
	}

	public void setCheckInTime(Time checkInTime) {
		this.checkInTime = checkInTime;
	}

	public Time getCheckOutTime() {
		return checkOutTime;
	}

	public void setCheckOutTime(Time checkOutTime) {
		this.checkOutTime = checkOutTime;
	}

	public ApartmentStatus getStatus() {
		return status;
	}

	public void setStatus(ApartmentStatus status) {
		this.status = status;
	}

	public ArrayList<Amenities> getAmenities() {
		return amenities;
	}

	public void setAmenities(ArrayList<Amenities> amenities) {
		this.amenities = amenities;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
}