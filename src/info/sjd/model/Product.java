package info.sjd.model;

public class Product {
	public String name;
	public String[] quals;
	
	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String[] getQuals() {
		return quals;
	}


	public void setQuals(String[] quals) {
		this.quals = quals;
	}


	public Product(String name, String[] quals) {
		super();
		this.name = name;
		this.quals = quals;
	}
	
}
