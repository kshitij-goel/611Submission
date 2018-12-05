package raspberrypi;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
	public static String globalOverride = "0";
	public static boolean startBackgroundTask = true;
	public static boolean isTriggerInitialized = true;
	public static Thread t_global;

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		ServerSideSocket.run();
	}

}
