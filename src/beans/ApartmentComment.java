package beans;

import java.util.UUID;

public class ApartmentComment {
	
	private User guest;
	private Apartment apartment;
	private String text;
	private float rating;
	private UUID idOne = UUID.randomUUID();
	
	
	public ApartmentComment(User guest, Apartment apartment, String text, float rating) {
		super();
		this.guest = guest;
		this.apartment = apartment;
		this.text = text;
		this.rating = rating;
	}


	public ApartmentComment() {
		super();
		// TODO Auto-generated constructor stub
	}


	public User getGuest() {
		return guest;
	}


	public void setGuest(User guest) {
		this.guest = guest;
	}


	public Apartment getApartment() {
		return apartment;
	}


	public void setApartment(Apartment apartment) {
		this.apartment = apartment;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public float getRating() {
		return rating;
	}


	public void setRating(float rating) {
		this.rating = rating;
	}


	public UUID getIdOne() {
		return idOne;
	}


	public void setIdOne(UUID idOne) {
		this.idOne = idOne;
	}
	
	
	
	

}
