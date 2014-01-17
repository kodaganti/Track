package com.cts.service;

import java.util.ArrayList;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class MessageNotifier {

	public static void notifier(String deviceList) {

		try {

			Sender sender = new Sender(
					"AIzaSyB_WGzEPoMqQFtdfMXU_ATna9odChYCfu8");

			ArrayList<String> devicesList = new ArrayList<String>();
			devicesList
					.add(deviceList);
			// Use this line to send message without payload data
			// Message message = new Message.Builder().build();

			// use this line to send message with payload data
			Message message = new Message.Builder()
					.collapseKey("1")
					.timeToLive(3)
					.delayWhileIdle(true)
					.addData("message",
							"this text will be seen in notification bar!!")
					.build();

			// Use this code to send to a single device
			// Result result = sender
			// .send(message,
			// "APA91bGiRaramjyohc2lKjAgFGpzBwtEmI8tJC30O89C2b3IjP1CuMeU1h9LMjKhmWuZwcXZjy1eqC4cE0tWBNt61Kx_SuMF6awzIt8WNq_4AfwflaVPHQ0wYHG_UX3snjp_U-5kJkmysdRlN6T8xChB1n3DtIq98w",
			// 1);

			// Use this for multicast messages
			MulticastResult result = sender.send(message, devicesList, 1);
			sender.send(message, devicesList, 1);

			System.out.println(result.toString());
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {
				}
			} else {
				int error = result.getFailure();
				System.out.println(error);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
