package beans;

import java.util.UUID;

public class ApartmentComment {
	
	private String guest;
	private UUID apartmentId;
	private String text;
	private float rating;
	private UUID idOne = UUID.randomUUID();
	private CommentStatus status = CommentStatus.HIDDEN;
	
	
	public ApartmentComment(String guest, UUID apartmentId, String text, float rating) {
		super();
		this.guest = guest;
		this.apartmentId = this.apartmentId;
		this.text = text;
		this.rating = rating;
	}


	public ApartmentComment() {
		super();
		// TODO Auto-generated constructor stub
	}


	public String getGuest() {
		return guest;
	}


	public void setGuest(String guest) {
		this.guest = guest;
	}



	public UUID getApartmentId() {
		return apartmentId;
	}


	public void setApartmentId(UUID apartmentId) {
		this.apartmentId = apartmentId;
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


	public CommentStatus getStatus() {
		return status;
	}


	public void setStatus(CommentStatus status) {
		this.status = status;
	}
	

}
