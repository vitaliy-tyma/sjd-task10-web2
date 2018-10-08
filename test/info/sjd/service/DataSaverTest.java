package info.sjd.service;

import static org.junit.Assert.*;
import org.junit.Test;
import info.sjd.model.Product;

public class DataSaverTest {

	@Test
	public void testToXML() {
		

		String name = "LG gram Thin and Light Laptop - 15.6\" Full HD IPS Display,"
					+ " Intel Core i5 (8th Gen), 8GB RAM, 256GB SSD, Back-lit Keyboard - Dark Silver – 15Z980-U.AAS5U1 ";
		
		
		String url = "https://www.amazon.com/LG-gram-Thin-Light-Laptop/dp/B078WRSHV4/ref=sr_1_82_sspa";
		String asin = "B078WRSHV4";
		String price = "$1,188.33";
		String availability = "In Stock.";
		String description = "15.6\" Full HD IPS LCD Screen\n" 
							+ "		Windows 10 Home. Lithium Battery Energy Content (in Watt Hours)-72Wh. Battery Cell Type-4 cell lithium ion(polymer)\n" 
							+ "		72Wh Lithium Battery (up to 19 hours* - MobileMark 2014 Standard)\n"
							+ "		Intel 8th Generation i5-8250U CPU\n" 
							+ "		8GB RAM & 256GB SSD";
		
		String xml_data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
						+ "<!DOCTYPE xml>\n"
						+ "<items>\n"
						+ "	<item>\n"
						+ "		<name>\n"
						+ "		LG gram Thin and Light Laptop - 15.6\" Full HD IPS Display, Intel Core i5 (8th Gen), 8GB RAM, 256GB SSD, Back-lit Keyboard - Dark Silver ֠15Z980-U.AAS5U1\n"
						+ "		</name>\n"
						+ "		<price>\n"
						+ "		$1,188.33\n"
						+ "		</price>\n"
						+ "		<availability>\n"
						+ "		In Stock.\n"
						+ "		</availability>\n"
						+ "		<url>\n"
						+ "		https://www.amazon.com/LG-gram-Thin-Light-Laptop/dp/B078WRSHV4/ref=sr_1_82_sspa\n"
						+ "		</url>\n"
						+ "		<asin>\n"
						+ "		B078WRSHV4\n"
						+ "		</asin>\n"
						+ "		<description>\n"
						+ "		15.6\" Full HD IPS LCD Screen\n"
						+ "		Windows 10 Home. Lithium Battery Energy Content (in Watt Hours)-72Wh. Battery Cell Type-4 cell lithium ion(polymer)\n"
						+ "		72Wh Lithium Battery (up to 19 hours* - MobileMark 2014 Standard)\n"
						+ "		Intel 8th Generation i5-8250U CPU\n"
						+ "		8GB RAM & 256GB SSD\n"
						+ "		</description>\n"
						+ "	</item>\n"
						+ "</items>";
		
		Product product = new Product (name, url, asin, price, availability, description);
				
		assertTrue(!DataSaver.toXML(product).isEmpty());
		
		//assertEquals(xml_data, DataSaver.toXML(product));
	}

}
