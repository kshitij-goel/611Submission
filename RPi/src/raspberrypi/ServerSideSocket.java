package raspberrypi;

import java.net.*;
import java.util.Scanner;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.platform.PlatformManager;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

import java.io.*;

public class ServerSideSocket {

	@SuppressWarnings("deprecation")
	public static void run() throws InterruptedException {

		ServerSideSocket.initialize();
		System.out.println("waiting to connect");

		try {

			int serverPort = 9999;
			ServerSocket serverSocket = new ServerSocket(serverPort);
			Socket server = null;
			Scanner in = new Scanner(System.in);
			while (true) {

				try {

					server = serverSocket.accept();

				} catch (Exception e) {
					System.out.println("ServerSideScoket hub not alive  " + e);
					Main.startBackgroundTask = true;
				}
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());

				String readLine = "";
				try {

					readLine = (String) inputStream.readObject();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("readLine   " + readLine);
				if (readLine != null) {

					String[] commands = readLine.split("#");

					if (commands[0].equalsIgnoreCase("initial")) {

						StringBuilder sb = new StringBuilder();
						sb.append("pi1");
						sb.append("#");
						sb.append("initial");
						sb.append("#");

						String macaddress = PiSystemInformation.getMacAddress();
						sb.append(macaddress); // mac value
						sb.append("#");

						String hardwareId = PiSystemInformation.getHardwareID();
						sb.append(hardwareId);

						sb.append("#");
						sb.append("out");
						sb.append("#");
						sb.append("led");
						sb.append("#");
						sb.append("3");
						sb.append("#");
						sb.append("in");
						sb.append("#");
						sb.append("UltraSonic Sensor");
						sb.append("#");
						sb.append("2"); // ultasonic min value
						sb.append("#");
						sb.append("400"); // ultrasonic sensor max value
						sb.append("#");
						sb.append("cm");

						System.out.println("sending INITIAL response to hub " + sb.toString());

						byte[] ipAddr = new byte[] { (byte) 192, (byte) 168, (byte) 1, (byte) 187 };
						InetAddress hos = InetAddress.getByAddress(ipAddr);
						Socket sock = new Socket(hos, 50000);

						ObjectOutputStream outputStream = new ObjectOutputStream(sock.getOutputStream());
						outputStream.writeObject(sb.toString());
						outputStream.flush();
						outputStream.close();

						if (Main.startBackgroundTask) {

							// Thread.sleep(2000);
							clientHelper myRunnable = new clientHelper();
							Thread t = new Thread((Runnable) myRunnable);
							t.start();
							System.out.println("thread started");

							Main.startBackgroundTask = false;

						}

					}

					else if (commands[0].equalsIgnoreCase("request")) {
						System.out.println(" request override  " + readLine);

						if (commands[2].equals("1")) {
							Main.globalOverride = "1";
						} else {
							Main.globalOverride = "0";
						}
						// change led status
						ledController.setPinvalues(readLine);
						System.out.println("led Values changed ");

					}

				}

			}
		} catch (UnknownHostException ex) {
			System.out.println(" SerevrSideSock unknown hostexception");
			Main.startBackgroundTask = true;
		} catch (IOException e) {
			System.out.println(" SerevrSideSock IOEXCEPTION");
			Main.startBackgroundTask = true;
		}

	}

	public static void initialize() {
		GpioController gController = GpioFactory.getInstance();

		ledController.redpin = gController.provisionDigitalOutputPin(RaspiPin.GPIO_24, "redLed", PinState.LOW);

		ledController.yellowpin = gController.provisionDigitalOutputPin(RaspiPin.GPIO_23, "redLed", PinState.LOW);

		ledController.greenpin = gController.provisionDigitalOutputPin(RaspiPin.GPIO_25, "redLed", PinState.LOW);
	}
}