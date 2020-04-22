package beans;

import java.sql.Date;
import java.time.LocalDate;

public class Reservation {

	private String apartment;
	private String reservedFrom;
	private String reservedTill;
	private int numberOfNights;
	private String message;
	private String guest;
	private float price;
	private ReservationStatus reservationStatus;
	
	public Reservation(String apartment, String reservedFrom, String reservedTill, int numberOfNights, String message,
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

	public String getReservedFrom() {
		return reservedFrom;
	}

	public void setReservedFrom(String reservedFrom) {
		this.reservedFrom = reservedFrom;
	}

	public String getReservedTill() {
		return reservedTill;
	}

	public void setReservedTill(String reservedTill) {
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
