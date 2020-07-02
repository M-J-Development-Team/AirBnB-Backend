package beans;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RentPeriod {
	
	public String from;
	public String to;
	public List<String> dates = new ArrayList<>();
	
	public RentPeriod() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RentPeriod(String from, String to) {
		super();
		this.from = from;
		this.to = to;
		
		LocalDate start = LocalDate.parse(from);
	    LocalDate end = LocalDate.parse(to);
	    List<String> totalDates = new ArrayList<>();

	    while (!start.isAfter(end)) {
	      totalDates.add(start.toString());
	      start = start.plusDays(1);
	    }


	    this.dates = totalDates;
	    System.out.println(getDates());
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

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}
	
	
	
	
	
	

}
