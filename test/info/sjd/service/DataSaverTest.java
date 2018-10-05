package info.sjd.service;

import static org.junit.Assert.*;
import org.junit.Test;
import info.sjd.model.Product;

public class DataSaverTest {

	@Test
	public void testToXML() {
		/*
		String[] quals = {
				"Make sure this fits by entering your model number.",
				"Make sure this fits by entering your model number.",
				"by entering your model number.",
				"15.6\" Full HD IPS LCD Screen ",
				"72Wh Lithium Battery (up to 19 hours* - MobileMark 2014 Standard)",
				"Intel 8th Generation i5-8250U CPU",
				"8GB RAM & 256GB SSD",
				"›"};

		String name = 
				"LG gram Thin and Light Laptop - 15.6\" Full HD IPS Display," +
				" Intel Core i5 (8th Gen), 8GB RAM, 256GB SSD, Back-lit Keyboard - Dark Silver – 15Z980-U.AAS5U1 ";
		
		String xml_data = 
"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
"		<!DOCTYPE xml>"+
"		<items>"+
"			<item>"+
"				<name>"+
"				LG gram Thin and Light Laptop - 15.6\" Full HD IPS Display, Intel Core i5 (8th Gen), 8GB RAM, 256GB SSD, Back-lit Keyboard - Dark Silver – 15Z980-U.AAS5U1"+
"				</name>"+
"				<description>"+
"				Make sure this fits by entering your model number."+
"				</description>"+
"				<description>"+
"				Make sure this fits by entering your model number."+
"				</description>"+
"				<description>"+
"				by entering your model number."+
"				</description>"+
"				<description>"+
"				15.6\" Full HD IPS LCD Screen"+
"				</description>"+
"				<description>"+
"				Windows 10 Home. Lithium Battery Energy Content (in Watt Hours)-72Wh. Battery Cell Type-4 cell lithium ion(polymer)"+
"				</description>"+
"				<description>"+
"				72Wh Lithium Battery (up to 19 hours* - MobileMark 2014 Standard)"+
"				</description>"+
"				<description>"+
"				Intel 8th Generation i5-8250U CPU"+
"				</description>"+
"				<description>"+
"				8GB RAM & 256GB SSD"+
"				</description>"+
"				<description>"+
"				›"+
"				</description>"+
"			</item>"+
"		</items>";
		
		Product product = new Product (name, quals);
				
		assertTrue(!DataSaver.toXML(product).isEmpty());
		//assertEquals(xml_data, DataSaver.toXML(product));*/
	}

}
