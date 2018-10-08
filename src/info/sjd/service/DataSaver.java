package info.sjd.service;

import info.sjd.model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import java.io.FileWriter;
import java.io.IOException;

public class DataSaver {

	private final static String SEP = System.getProperty("line.separator");

	/**
	 * @param url
	 * @param proxy
	 * @return
	 */
	public static Product getData(String url, Boolean proxy) {
		Product product = null;
		Document doc = null;
		String product_name = "UNDEFINED";
		String product_url = url;
		String product_asin = "UNDEFINED";
		String product_price = "UNDEFINED";
		String product_availability = "UNDEFINED";
		String product_description = "UNDEFINED";

		try {
			if (proxy) {
				System.setProperty("http.proxyHost", "127.0.0.1");
				System.setProperty("http.proxyPort", "22222");

				System.setProperty("https.proxyHost", "127.0.0.1");
				System.setProperty("https.proxyPort", "22222");
			}

			Connection connection = Jsoup.connect(url);
			connection.userAgent("Mozilla/5.0");
			connection.referrer("https://amason.com");
			connection.timeout(30 * 1000);
			doc = connection.get();

			/** Data from title */
			try {
				Element getDivTitle = doc.getElementById("titleSection");
				// String[] product_title_quals = getDivTitle.text().split(",");
				product_name = getDivTitle.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Element getDivPrice = doc.getElementById("priceblock_ourprice");
				product_price = getDivPrice.text();
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			try {
				Element getDivAvailability = doc.getElementById("availability");
				product_availability = getDivAvailability.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** Get ASIN from the URL */
			String[] url_parts = url.split("/");
			int i = 0;
			for (String part : url_parts) {
				i++;
				if (part.equals("dp")) {
					break;
				}
			}
			product_asin = url_parts[i];

			/** Data from list */
			Element getDivQuals = doc.getElementById("feature-bullets");
			Elements quals = getDivQuals.getElementsByClass("a-list-item");

			/** Elements to array */
			/* HOW TO ELIMINATE THIS TAG?!
			 * <span class="a-list-item"> <span id="replacementPartsFitmentBulletInner"> <a
			 * class="a-link-normal hsx-rpp-fitment-focus" href="#">Make sure this fits</a>
			 * <span>by entering your model number.</span> </span> </span>
			 * qual.text() = Make sure this fits by entering your model number.
			 */
			
			StringBuilder product_description_sb = new StringBuilder();
			for (Element qual : quals) {
					product_description_sb.append(qual.text() + SEP);
			}
			product_description = product_description_sb.toString();



			product = new Product(product_name, product_url, product_asin, product_price, product_availability,
					product_description);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return product;
	}

	/** saveToFile */
	public static void saveToFile(String xml_data, String file_name) {

		try (FileWriter writer = new FileWriter(file_name)) {
			writer.write(xml_data);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Surround XML */
	private static String surroundWithXML(String tagName, String value) {
		StringBuilder result = new StringBuilder();
		result.append("		<" + tagName + ">" + SEP);
		result.append("		" + value);
		result.append(SEP);
		result.append("		</" + tagName + ">" + SEP);
		return result.toString();
	}

	/** Parse to XML. */
	public static String toXML(Product product) {
		StringBuilder result = new StringBuilder();

		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + SEP);
		result.append("<!DOCTYPE xml>" + SEP);

		result.append("<items>" + SEP);
		result.append("	<item>" + SEP);

		result.append(surroundWithXML("name", product.getName()));
		result.append(surroundWithXML("price", product.getPrice()));
		result.append(surroundWithXML("availability", product.getAvailability()));
		result.append(surroundWithXML("url", product.getUrl()));
		result.append(surroundWithXML("asin", product.getAsin()));
		result.append(surroundWithXML("description", product.getDescription()));

		/*
		 * for (String qual : product.quals) { if (!qual.isEmpty()) {
		 * result.append("		<description>" + SEP); result.append("		" + qual);
		 * result.append(SEP); result.append("		</description>" + SEP); } }
		 */
		result.append("	</item>" + SEP);
		result.append("</items>");

		return result.toString();
	}

}
