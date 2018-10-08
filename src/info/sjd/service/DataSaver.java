package info.sjd.service;

import info.sjd.model.Product;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.jsoup.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

			/** Data from title.*/
			try {
				Element getDivTitle = doc.getElementById("titleSection");
				product_name = getDivTitle.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** Price from html.*/
			try {
				Element getDivPrice = doc.getElementById("priceblock_ourprice");
				product_price = getDivPrice.text().replaceAll("[^0-9]", "");
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			/** Availability from html.*/
			try {
				Element getDivAvailability = doc.getElementById("availability");
				product_availability = getDivAvailability.text();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/** ASIN from URL.*/
			String[] url_parts = url.split("/");
			int i = 0;
			for (String part : url_parts) {
				i++;
				if (part.equals("dp")) {
					break;
				}
			}
			product_asin = url_parts[i];

			/** Description from list excluding hidden elements.*/
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

		result.append("	</item>" + SEP);
		result.append("</items>");

		return result.toString();
	}

	
	
	
	/** appendToFile 
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException */
	public static void appendToFile(Product product, String file_name) throws TransformerException, ParserConfigurationException, SAXException, IOException {

		
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(file_name);
        doc.normalize();
        Node item = doc.getFirstChild();

		
		//doc.appendChild(root);
        item.appendChild(doc.createTextNode("Some"));
		//root.appendChild(doc.createTextNode(" "));
		//root.appendChild(doc.createTextNode("text"));
		
        Transformer transformer=TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(), new StreamResult(new FileOutputStream(file_name)));
		
		/*Document doc = null;
		try {
			DocumentBuilderFactory factory  = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = (Document) builder.parse(new File(file_name));


		
		
		
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		//DOMSource domSource = new DOMSource();
		//StreamResult sr = new StreamResult(new File(file_name));
		//transformer.transform(domSource, sr);
		/////////////////////////////////////////////////////
        //Transformer transformer = TransformerFactory.newInstance().newTransformer();
        //transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //+initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource();
        transformer.transform(source, result);

        String xmlString = result.getWriter().toString();
        //System.out.println(xmlString);

        PrintWriter output = new PrintWriter(file_name);
		///////////////
		
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}*/
		
		
		////////////////////////////////////////////////////////////
/*		try (FileWriter writer = new FileWriter(file_name)) {
			writer.write("xml_data");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}

}
