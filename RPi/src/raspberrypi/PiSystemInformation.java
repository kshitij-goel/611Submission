package raspberrypi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/* Pi4J imports */

import com.pi4j.system.SystemInfo;

public class PiSystemInformation {
	public static String getHardwareID() {
		String serialnumber = "";
		try {

			serialnumber = SystemInfo.getSerial();
		} catch (UnsupportedOperationException ex) {
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return serialnumber;

	}

	public static String getMacAddress() {
		StringBuilder sb = new StringBuilder();
		try {

			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			NetworkInterface network = networks.nextElement();
			byte[] mac = network.getHardwareAddress();
			if (mac != null) {
				for (int i = 0; i < mac.length; i++) {
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
				}
				System.out.println(sb.toString());
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

}
