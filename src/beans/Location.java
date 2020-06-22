package beans;

public class Location {
	
	private String gWidth;
	private String gHeight;
	private Address address;
	
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Location(String gWidth, String gHeight, Address address) {
		super();
		this.gWidth = gWidth;
		this.gHeight = gHeight;
		this.address = address;
	}

	public String getgWidth() {
		return gWidth;
	}

	public void setgWidth(String gWidth) {
		this.gWidth = gWidth;
	}

	public String getgHeight() {
		return gHeight;
	}

	public void setgHeight(String gHeight) {
		this.gHeight = gHeight;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address adress) {
		this.address = adress;
	}

	
	
	
	

}
