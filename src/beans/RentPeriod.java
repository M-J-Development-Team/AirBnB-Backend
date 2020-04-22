package beans;

import java.sql.Date;
import java.time.LocalDate;

public class RentPeriod {
	
	public String from;
	public String to;
	
	public RentPeriod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RentPeriod(String from, String to) {
		super();
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	
	
	

}
