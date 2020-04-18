package beans;

import java.sql.Date;

public class Reservation {

	private String apartment;
	private Date reservedFrom;
	private Date reservedTill;
	private int numberOfNights;
	private String message;
	private String guest;
	private float price;
	private ReservationStatus reservationStatus;
	
	public Reservation(String apartment, Date reservedFrom, Date reservedTill, int numberOfNights, String message,
			String guest, float price, ReservationStatus reservationStatus) {
		super();
		this.apartment = apartment;
		this.reservedFrom = reservedFrom;
		this.reservedTill = reservedTill;
		this.numberOfNights = numberOfNights;
		this.message = message;
		this.guest = guest;
		this.price = price;
		this.reservationStatus = reservationStatus;
	}

	public String getApartment() {
		return apartment;
	}

	public void setApartment(String apartment) {
		this.apartment = apartment;
	}

	public Date getReservedFrom() {
		return reservedFrom;
	}

	public void setReservedFrom(Date reservedFrom) {
		this.reservedFrom = reservedFrom;
	}

	public Date getReservedTill() {
		return reservedTill;
	}

	public void setReservedTill(Date reservedTill) {
		this.reservedTill = reservedTill;
	}

	public int getNumberOfNights() {
		return numberOfNights;
	}

	public void setNumberOfNights(int numberOfNights) {
		this.numberOfNights = numberOfNights;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public ReservationStatus getReservationStatus() {
		return reservationStatus;
	}

	public void setReservationStatus(ReservationStatus reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	
	
	
}
