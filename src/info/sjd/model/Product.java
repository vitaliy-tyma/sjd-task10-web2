package info.sjd.model;

public class Product {
	public String name;
	public String url;
	public String asin;
	public String price;
	public String availability;
	public String description;

	public Product(String name, String url, String asin, String price, String availability, String description) {
		super();
		this.name = name;
		this.url = url;
		this.asin = asin;
		this.price = price;
		this.availability = availability;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAsin() {
		return asin;
	}

	public void setAsin(String asin) {
		this.asin = asin;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAvailability() {
		return availability;
	}

	public void setAvailability(String availability) {
		this.availability = availability;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
