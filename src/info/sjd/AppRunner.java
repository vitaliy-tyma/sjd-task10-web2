package info.sjd;



import java.util.logging.Level;
import java.util.logging.Logger;
import info.sjd.model.Product;
import info.sjd.service.DataSaver;
import info.sjd.service.ProductCreationException;
import info.sjd.service.ProxyResolver;

public class AppRunner {
	private static final String URL = "https://www.amazon.com/LG-gram-Thin-Light-Laptop/dp/B078WRSHV4/ref=sr_1_82_sspa";
	private static final String URL2 = "https://www.amazon.com/dp/B078WSX4QL/ref=sspa_dk_detail_0?psc=1&pd_rd_i=B078WSX4QL&pf_rd_m=ATVPDKIKX0DER&pf_rd_p=f52e26da-1287-4616-824b-efc564ff75a4&pf_rd_r=1JQGZNK6HEFEB2TFJBMW&pd_rd_wg=xrFK4&pf_rd_s=desktop-dp-sims&pf_rd_t=40701&pd_rd_w=36QIU&pf_rd_i=desktop-dp-sims&pd_rd_r=32f029ca-cb37-11e8-9093-f12a4236caa2";

	private static final String SEP = System.getProperty("file.separator");
	private static final String DIR_NAME = "data" + SEP;
	private static final String FILE_NAME = DIR_NAME + "task10.xml";
	private static Boolean PROXY = Boolean.FALSE;

	public static void main(String[] args) throws ProductCreationException {

		Logger logger = Logger.getLogger(AppRunner.class.getName());

		PROXY = ProxyResolver.ResolveProxy("OGPzamPC");
		logger.log(Level.CONFIG,
				"CONFIG MESSAGE: " + (PROXY ? "Working through proxy." : "Working with direct connection."));

		Product product = null;

		try {
			product = DataSaver.getData(URL, PROXY);
			logger.info("Data #1 has been extracted.");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception while creating product." + SEP + e.getMessage());
		}

		if (product != null) {
			DataSaver.saveToFile(DataSaver.toXML(product), FILE_NAME);
			logger.info("Data #1 has been saved to the new file.");
		}


		/** 
		 * Append XML file.
		 * */
		try {
			product = DataSaver.getData(URL2, PROXY);
			logger.info("Data #2 has been extracted.");
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception while creating product." + SEP + e.getMessage());
		}

		if (product != null) {
			DataSaver.appendToFile(product, FILE_NAME);
			logger.info("Data #2 has been appended to the existing file.");
		}
		

	}

}
