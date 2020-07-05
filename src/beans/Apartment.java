package beans;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


public class Apartment {
	
	private ApartmentType type;
	private int numberOfRooms;
	private int numberOfGuests;
	private Location location;
	private ArrayList<RentPeriod> datesForRenting =  new ArrayList<>();
	private ArrayList<String> freeDates = new ArrayList<>();
	private ArrayList<String> rentedDates = new ArrayList<String>();
	private String host;
	private ArrayList<String> comments = new ArrayList<>();
	private String photoPath;
	private UUID idOne = UUID.randomUUID();
	private float price;
	private String checkInTime;
	private String checkOutTime;
	private ApartmentStatus status;
	private ArrayList<Amenities> amenities = new ArrayList<>();
	private ArrayList<String> reservations = new ArrayList<>();
	private String name;
	private double rating = 0;
	
	@PostConstruct
	public void init() {
		
		if( this.type.equals(ApartmentType.ROOM)) {
			this.numberOfRooms = 1;
		}
		
	}
	
	public Apartment(ApartmentType type, int numberOfRooms, int numberOfGuests, ArrayList<RentPeriod> datesForRenting,
			ArrayList<String> freeDates, ArrayList<String> rentedDates, String host, UUID idOne, float price,
			ApartmentStatus status, ArrayList<String> reservations, String name) {
		super();
		this.type = type;
		this.numberOfRooms = numberOfRooms;
		this.numberOfGuests = numberOfGuests;
		this.datesForRenting = datesForRenting;
		this.freeDates = freeDates;
		this.rentedDates = rentedDates;
		this.host = host;
		this.idOne = idOne;
		this.price = price;
		this.status = status;
		this.reservations = reservations;
		this.name = name;
	}

	public Apartment(ApartmentType type, int numberOfRooms, int numberOfGuests, Location location,
			ArrayList<RentPeriod> datesForRenting, ArrayList<String> freeDates, String host,
			ArrayList<String> comments, String photoPath, UUID idOne, float price, String checkInTime,
			String checkOutTime, ApartmentStatus status, ArrayList<Amenities> amenities,
			ArrayList<String> reservations, ArrayList<String> rentedDates) {
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
		this.rentedDates = rentedDates;
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

	public ArrayList<RentPeriod> getDatesForRenting() {
		return datesForRenting;
	}

	public void setDatesForRenting(ArrayList<RentPeriod> datesForRenting) {
		this.datesForRenting = datesForRenting;
	}

	public ArrayList<String> getFreeDates() {
		return freeDates;
	}

	public void setFreeDates(ArrayList<String> freeDates) {
		this.freeDates = freeDates;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	

	public ArrayList<String> getComments() {
		return comments;
	}

	public void setComments(ArrayList<String> comments) {
		this.comments = comments;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
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

	
	

	public String getCheckInTime() {
		return checkInTime;
	}


	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}


	public String getCheckOutTime() {
		return checkOutTime;
	}


	public void setCheckOutTime(String checkOutTime) {
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

	public ArrayList<String> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<String> reservations) {
		this.reservations = reservations;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<String> getRentedDates() {
		return rentedDates;
	}


	public void setRentedDates(ArrayList<String> rentedDates) {
		this.rentedDates = rentedDates;
	}
	
	
	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public boolean addNewRentDates(String from,String to) {
		LocalDate wantedStart = LocalDate.parse(from);
		LocalDate wantedEnd = LocalDate.parse(to);
		
		LocalDate today = LocalDate.now(); 
		if(wantedStart.isBefore(today)) {
			return false;
		}if(wantedStart.isAfter(wantedEnd)) {
			return false;
		}
		else {
			
			for(RentPeriod rentPeriod : this.datesForRenting) {
			
			LocalDate startPeriod = LocalDate.parse(rentPeriod.from);
			LocalDate endPeriod = LocalDate.parse(rentPeriod.to);
			
			if(wantedStart.isBefore(startPeriod) && wantedEnd.isBefore(endPeriod)) {
				continue;
			}
			if(wantedStart.isAfter(endPeriod) && wantedEnd.isAfter(endPeriod)) {
				continue;
			}
			if((wantedStart.isEqual(startPeriod) || (wantedStart.isAfter(startPeriod))) && (wantedEnd.isEqual(endPeriod) || wantedEnd.isBefore(endPeriod) || wantedEnd.isAfter(endPeriod))) {
				return false;
			}
		}
		
		RentPeriod rentPeriod = new RentPeriod();
		rentPeriod.setFrom(from);
		rentPeriod.setTo(to);
		this.datesForRenting.add(rentPeriod);
		return true;
		
	}
	
	}
	
	
}