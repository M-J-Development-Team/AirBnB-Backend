package beans;

import java.util.UUID;

public class Amenities {
	
	UUID idOne = UUID.randomUUID();
	private String name;
	private AmenityStatus amenityStatus;
	
	private Amenities() {
		
	}
	public Amenities(UUID idOne, String name, AmenityStatus amenityStatus) {
		super();
		this.idOne = idOne;
		this.name = name;
		this.amenityStatus = amenityStatus;
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
	public AmenityStatus getAmenityStatus() {
		return amenityStatus;
	}
	public void setAmenityStatus(AmenityStatus amenityStatus) {
		this.amenityStatus = amenityStatus;
	}

}

