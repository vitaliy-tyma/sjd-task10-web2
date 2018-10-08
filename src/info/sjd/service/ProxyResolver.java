package info.sjd.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ProxyResolver {

	public static boolean ResolveProxy(String check) {
		String computer_name = null;
		try {
			computer_name = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (computer_name.equals(check)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}
}
