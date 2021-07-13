package it.unibo.parkingmanagerservicetest;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import it.unibo.ctxParkingservice.MainCtxParkingserviceKt;
import it.unibo.parkingmanagerservice.test.utils.*;

public class parkingmanagerserviceTest {
	
	Tester tester = Tester.INSTANCE;
	static Thread t	  = null;
	static Thread a	  = null;
	
	@BeforeClass
	public static void init() throws InterruptedException, UnknownHostException, IOException {

			t = new Thread(new Runnable() {
			@Override
			public void run() {
				MainCtxParkingserviceKt.main();				
			}
		});
		t.start();
		//assertTrue(obs.nextChange().equals("work")); //Wait until system is started
	}

	
	@Test
	public void carStdEnter() throws IOException, InterruptedException {
		tester.setSlot(1); 
		a = new Thread(new Runnable() {
			@Override
			public void run() {
				boolean end = false;
				while(true) {
					if(tester.getchanged()) {
						System.out.println(tester.getclientstate().toString());
						tester.setchanged(false);
						if(tester.getclientstate().equals(ClientState.pickupCar)) {
							end = true;
						}
					}
					assertTrue(end);
				}			
			}
		});
		a.start();
		t.join();
	}
		
}
	
