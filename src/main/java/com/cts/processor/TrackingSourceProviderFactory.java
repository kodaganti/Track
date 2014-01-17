package com.cts.processor;

import java.util.regex.Pattern;

import com.cts.processor.client.FedexTrackingProvider;
import com.cts.processor.client.TrackingProvider;
import com.cts.processor.client.USPSTrackingProvider;
import com.cts.processor.datamodel.ShipmentTrackingRequest;

public class TrackingSourceProviderFactory {

//	private static TrackingSourceProviderFactory m_self = new TrackingSourceProviderFactory();

	private static Pattern s_fedexPattern1 = Pattern.compile("[0-9]{12}");
	private static Pattern s_fedexPattern2 = Pattern.compile("[0-9]{15}");

	// Private constructor in order to make singleton.
	private TrackingSourceProviderFactory() {

	}

	public static TrackingProvider getTrackingProvider(
			ShipmentTrackingRequest request) {
		if (s_fedexPattern1.matcher(request.getTrackingNumber()).matches()
				|| s_fedexPattern2.matcher(request.getTrackingNumber()).matches()) {
			return FedexTrackingProvider.getInstance();
		}
		return USPSTrackingProvider.getInstance();
	}
}