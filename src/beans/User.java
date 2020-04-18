package beans;

import java.util.ArrayList;

public class User {
	
	private String username;
	private String password;
	private String name;
	private String lastname;
	private Gender gender;
	private Role role;
	private String idOne;

	
	private ArrayList<Apartment> apartmentsToRent = new ArrayList<Apartment>();
	private ArrayList<Apartment> rentedApartments = new ArrayList<Apartment>();
	private ArrayList<Reservation> reservations = new ArrayList<Reservation>();
	
	public User(String username, String password, String name, String lastname, Gender gender, Role role,
			ArrayList<Apartment> apartments, ArrayList<Reservation> reservations) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.lastname = lastname;
		this.gender = gender;
		
		if(role.equals(Role.HOST)) {
			this.role = Role.HOST;
			this.apartmentsToRent = apartments;
			
		} else if(role.equals(Role.GUEST)) {
			this.role = Role.GUEST;
			this.rentedApartments = apartments;
			
		} else {
			this.role = Role.ADMIN;
		}
		this.reservations = reservations;
	}
	
	public User() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public ArrayList<Apartment> getApartmentsToRent() {
		return apartmentsToRent;
	}

	public void setApartmentsToRent(ArrayList<Apartment> apartmentsToRent) {
		this.apartmentsToRent = apartmentsToRent;
	}

	public ArrayList<Apartment> getRentedApartments() {
		return rentedApartments;
	}

	public void setRentedApartments(ArrayList<Apartment> rentedApartments) {
		this.rentedApartments = rentedApartments;
	}

	public ArrayList<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}

	public String getIdOne() {
		return idOne;
	}

	public void setIdOne(String idOne) {
		this.idOne = idOne;
	}
	
	
	
	
	
	
	
	
	
	
	
}
