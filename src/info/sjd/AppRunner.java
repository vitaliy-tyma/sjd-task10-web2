package info.sjd;

import info.sjd.model.Product;
import info.sjd.service.DataSaver;

public class AppRunner {
	private static final String URL = "https://www.amazon.com/LG-gram-Thin-Light-Laptop/dp/B078WRSHV4/ref=sr_1_82_sspa";
	private static final String SEP = System.getProperty("file.separator");
	private static final String DIR_NAME = "data" + SEP;
	private static final String FILE_NAME = DIR_NAME + "task10.xml";
	private static Boolean PROXY = Boolean.FALSE;

	public static void main(String[] args) {


			// USE PROXY (switch off before commit)
			PROXY = Boolean.TRUE;

			Product product = DataSaver.getData(URL, PROXY);

			if (product != null) {
				DataSaver.saveToFile(DataSaver.toXML(product), FILE_NAME);
			}

	}

}
