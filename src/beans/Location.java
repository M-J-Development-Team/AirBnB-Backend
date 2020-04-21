package beans;

public class Location {
	
	private float gWidth;
	private float gHeight;
	private Address adress;
	
	public Location() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Location(float gWidth, float gHeight, Address adress) {
		super();
		this.gWidth = gWidth;
		this.gHeight = gHeight;
		this.adress = adress;
	}

	public float getgWidth() {
		return gWidth;
	}

	public void setgWidth(float gWidth) {
		this.gWidth = gWidth;
	}

	public float getgHeight() {
		return gHeight;
	}

	public void setgHeight(float gHeight) {
		this.gHeight = gHeight;
	}

	public Address getAdress() {
		return adress;
	}

	public void setAdress(Address adress) {
		this.adress = adress;
	}

	
	
	
	

}
