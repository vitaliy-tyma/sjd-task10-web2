package info.sjd;

import java.util.logging.Logger;
import info.sjd.model.Product;
import info.sjd.service.DataSaver;
import info.sjd.service.ProxyResolver;

public class AppRunner {
	private static final String URL = "https://www.amazon.com/LG-gram-Thin-Light-Laptop/dp/B078WRSHV4/ref=sr_1_82_sspa";
	private static final String SEP = System.getProperty("file.separator");
	private static final String DIR_NAME = "data" + SEP;
	private static final String FILE_NAME = DIR_NAME + "task10.xml";
	private static Boolean PROXY = Boolean.FALSE;

	public static void main(String[] args) {

		Logger logger = Logger.getLogger(AppRunner.class.getName());

		PROXY = ProxyResolver.ResolveProxy("OGPzamPC");
		logger.info("Proxy is " + PROXY.toString());

		Product product = null;

		try {
			product = DataSaver.getData(URL, PROXY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Data have been extracted.");

		if (product != null) {
			DataSaver.saveToFile(DataSaver.toXML(product), FILE_NAME);
		}
		logger.info("Data have been saved.");

	}

}
