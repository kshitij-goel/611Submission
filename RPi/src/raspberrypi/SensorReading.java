package raspberrypi;

import java.io.IOException;

/* Pi4J imports */
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.system.SystemInfo;

public class SensorReading {

	public static void testRaspberrypi() {

	}

	public static String CurrentReading() throws InterruptedException {

		// Thread.sleep(2000);

		clientHelper.triggerPin.high(); // Make trigger pin HIGH
		Thread.sleep((long) 0.01);// Delay for 10 microseconds
		clientHelper.triggerPin.low();

		while (clientHelper.echoPin.isLow()) {

		}
		long startTime = System.nanoTime();
		while (clientHelper.echoPin.isHigh()) {

		}
		long endTime = System.nanoTime();
		double distance = (((endTime - startTime) / 1e3) / 2) / 29.1;

		return String.valueOf(distance);
	}

}
