package com.cts.processor.client;

import com.cts.processor.datamodel.ShipmentTrackingRequest;
import com.cts.processor.datamodel.ShipmentTrackingResponse;

public abstract class TrackingProvider {
	
	public abstract ShipmentTrackingResponse getTracking(ShipmentTrackingRequest request);
}
