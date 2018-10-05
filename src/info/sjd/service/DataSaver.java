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

	public static Product getData(String url, Boolean proxy) {
		Product product = null;
		Document doc = null;
		String product_name = "UNDEFINED";
		String product_url = url;
		String product_artID = "UNDEFINED";
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
			connection.referrer("http://amason.com");
			connection.timeout(30 * 1000);
			doc = connection.get();

			/** Data from title */
			Element getDivTitle = doc.getElementById("titleSection");
			// String[] product_title_quals = getDivTitle.text().split(",");
			product_name = getDivTitle.text();

			
			try {
			Element getDivPrice = doc.getElementById("priceblock_ourprice");
			//product_price = getDivPrice.text();
			}
			catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			
			
			Element getDivAvailability = doc.getElementById("availability");
			product_availability = getDivAvailability.text();
			
			
			///////////////////////////////////
			/** Data from list */
/*			Element getDivQuals = doc.getElementById("feature-bullets");
			Elements quals = getDivQuals.select("span");
*/
			/** Elements to array */
/*			int i = 0;
			String product_quals[] = new String[quals.size()];
			for (Element qual : quals) {
				product_quals[i++] = qual.text();
			}*/
			/////////////////////////////////
			
			
			
			product = new Product(product_name, product_url, product_artID, product_price, product_availability, product_description);
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

	/** Parse to XML.*/
	public static String toXML(Product product) {
		StringBuilder result = new StringBuilder();

		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + SEP);
		result.append("<!DOCTYPE xml>" + SEP);

		result.append("<items>" + SEP);
		result.append("	<item>" + SEP);
		
		result.append("		<name>" + SEP);
		result.append("		" + product.getName());
		result.append(SEP);
		result.append("		</name>" + SEP);

		result.append("		<price>" + SEP);
		result.append("		" + product.getPrice());
		result.append(SEP);
		result.append("		</price>" + SEP);
		
		result.append("		<availability>" + SEP);
		result.append("		" + product.getAvailability());
		result.append(SEP);
		result.append("		</availability>" + SEP);
/*		for (String qual : product.quals) {
			if (!qual.isEmpty()) {
				result.append("		<description>" + SEP);
				result.append("		" + qual);
				result.append(SEP);
				result.append("		</description>" + SEP);
			}
		}
*/
		result.append("	</item>" + SEP);
		result.append("</items>");

		return result.toString();
	}

}
