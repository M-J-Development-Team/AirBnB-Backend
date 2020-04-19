package beans;

import java.util.UUID;

public class Amenities {
	
	UUID idOne = UUID.randomUUID();
	private String name;
	
	private Amenities() {
		
	}
	public Amenities(UUID idOne, String name) {
		super();
		this.idOne = idOne;
		this.name = name;
	}
	public UUID getIdOne() {
		return idOne;
	}
	public void setIdOne(UUID idOne) {
		this.idOne = idOne;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}

