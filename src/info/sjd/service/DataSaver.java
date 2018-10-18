package info.sjd.service;

import info.sjd.model.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSaver {

	private final static String SEP = System.getProperty("line.separator");
	private static Logger logger = Logger.getLogger(DataSaver.class.getName());
	/**
	 * @param url
	 * @param proxy
	 * @return
	 * @throws ProductCreationException 
	 */
	public static Product getData(String url, Boolean proxy) throws ProductCreationException {

		
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
				System.setProperty("http.proxyPort", "8080");

				System.setProperty("https.proxyHost", "127.0.0.1");
				System.setProperty("https.proxyPort", "8080");
			}

			Connection connection = Jsoup.connect(url);
			connection.userAgent("Mozilla/5.0");
			connection.referrer("https://amason.com");
			connection.timeout(30 * 1000);
			doc = connection.get();

			/** Data from title. */
			try {
				Element getDivTitle = doc.getElementById("titleSection");
				product_name = getDivTitle.text();
			} catch (Exception e) {
				logger.log(Level.FINER, "Cannot parse product title." + SEP + e.getMessage() );
			}

			/** Price from html. */
			try {
				Element getDivPrice = doc.getElementById("priceblock_ourprice");
				product_price = getDivPrice.text().replaceAll("[^0-9]", "");
			} catch (Exception e) {
				logger.log(Level.FINER, "Cannot parse product price." + SEP + e.getMessage() );
			}

			/** Availability from html. */
			try {
				Element getDivAvailability = doc.getElementById("availability");
				product_availability = getDivAvailability.text();
			} catch (Exception e) {
				logger.log(Level.FINER, "Cannot parse product availability." + SEP + e.getMessage() );
			}

			/** ASIN from URL. */
			int i;
			try {
				String[] url_parts = url.split("/");
				i = 0;
				for (String part : url_parts) {
					i++;
					if (part.equals("dp")) {
						break;
					}
				}
				product_asin = url_parts[i];
			} catch (Exception e) {
				logger.log(Level.FINER, "Cannot parse product ASIN." + SEP + e.getMessage() );
			}

			/** Description from list excluding hidden elements. */
			try {
				Element getDivQuals = doc.getElementById("feature-bullets");
				Elements quals = getDivQuals.getElementsByClass("a-list-item");

				i = 0;
				StringBuilder product_description_sb = new StringBuilder();
				for (Element qual : quals) {
					i++;
					if (!qual.html().contains("replacementPartsFitmentBulletInner")) {
						product_description_sb.append(qual.text());
						if (i < quals.size()) {
							product_description_sb.append(SEP + "		");
						}
					}

				}
				product_description = product_description_sb.toString();
			} catch (Exception e) {
				logger.log(Level.FINER, "Cannot parse product description." + SEP + e.getMessage() );
			}

			/** Create new product. */
			try {
				product = new Product(product_name, product_url, product_asin, product_price, product_availability,
						product_description);
			} catch (Exception e) {
				logger.log(Level.FINE, "Cannot create new product." + SEP + e.getMessage());
			}
			
		} catch (IOException e) {
			logger.log(Level.WARNING, "Cannot establish connection." + SEP + e.getMessage());
			throw new ProductCreationException();
		}
		return product;
	}

	/** saveToFile */
	public static void saveToFile(String xml_data, String file_name) {


		try (FileWriter writer = new FileWriter(file_name)) {
			writer.write(StartXML());
			writer.write(xml_data);
			writer.write(FinishXML());
			writer.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot write file." + SEP + e.getMessage());
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

	/** Start XML. */
	public static String StartXML() {
		StringBuilder result = new StringBuilder();
		result.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + SEP);
		result.append("<!DOCTYPE xml>" + SEP);
		result.append("<items>" + SEP);
		return result.toString();
	}

	/** Finish XML. */
	public static String FinishXML() {
		StringBuilder result = new StringBuilder();
		result.append("</items>");
		return result.toString();
	}

	/** Parse to XML. */
	public static String toXML(Product product) {
		StringBuilder result = new StringBuilder();
		result.append("	<item>" + SEP);
		result.append(surroundWithXML("name", product.getName()));
		result.append(surroundWithXML("price", product.getPrice()));
		result.append(surroundWithXML("availability", product.getAvailability()));
		result.append(surroundWithXML("url", product.getUrl()));
		result.append(surroundWithXML("asin", product.getAsin()));
		result.append(surroundWithXML("description", product.getDescription()));
		result.append("	</item>" + SEP);
		return result.toString();
	}

	/**
	 * appendToFile
	 * 
	 */
	public static void appendToFile(Product product, String file_name) {


		List<String> st_list = new LinkedList<String>();
		String st;

		try {
			File file = new File(file_name);
			BufferedReader reader = new BufferedReader(new FileReader(file));

			while ((st = reader.readLine()) != null) {
				st_list.add(st);
			}
			reader.close();
			
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot read file." + SEP + e.getMessage());
		}

		StringBuilder stb = new StringBuilder();
		for (int i = 0; i < st_list.size() - 1; i++) {
			stb.append(st_list.get((i)));
			stb.append(SEP);
		}

		stb.append(toXML(product));
		stb.append(FinishXML());

		try (FileWriter writer = new FileWriter(file_name)) {
			writer.write(stb.toString());
			writer.flush();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot write file." + SEP + e.getMessage());
		}
	}

}
