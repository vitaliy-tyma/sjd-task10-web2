package info.sjd.service;

import info.sjd.model.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;
import org.jsoup.Connection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

			/** Data from title. */
			try {
				Element getDivTitle = doc.getElementById("titleSection");
				product_name = getDivTitle.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** Price from html. */
			try {
				Element getDivPrice = doc.getElementById("priceblock_ourprice");
				product_price = getDivPrice.text().replaceAll("[^0-9]", "");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			/** Availability from html. */
			try {
				Element getDivAvailability = doc.getElementById("availability");
				product_availability = getDivAvailability.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** ASIN from URL. */
			String[] url_parts = url.split("/");
			int i = 0;
			for (String part : url_parts) {
				i++;
				if (part.equals("dp")) {
					break;
				}
			}
			product_asin = url_parts[i];

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
				e.printStackTrace();
			}

			/** Create new product. */
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
			writer.write(StartXML());
			writer.write(xml_data);
			writer.write(FinishXML());
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
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
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
		} catch (IOException e1) {
			e1.printStackTrace();
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
			e.printStackTrace();
		}
	}

}
