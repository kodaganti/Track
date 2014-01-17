package com.cts.processor;

import com.cts.dal.ShipmentTrackingDetailsDAO;
import com.cts.processor.client.TrackingProvider;
import com.cts.processor.datamodel.ShipmentStatus;
import com.cts.processor.datamodel.ShipmentTrackingRequest;
import com.cts.processor.datamodel.ShipmentTrackingResponse;

public class TrackingServiceProcessor {

	private static TrackingServiceProcessor m_self = new TrackingServiceProcessor();

	// Private constructor in order to make singleton.
	private TrackingServiceProcessor() {

	}

	public static TrackingServiceProcessor getInstance() {
		return m_self;
	}

	public ShipmentTrackingResponse getStatus(ShipmentTrackingRequest request) {
		ShipmentTrackingResponse dbResponse = checkDB(request);
		if(dbResponse != null){
			return dbResponse;
		}
		TrackingProvider provide = TrackingSourceProviderFactory
				.getTrackingProvider(request);
		ShipmentTrackingResponse response = provide.getTracking(request);
		if(response.getStatus() == ShipmentStatus.DL){
			ShipmentTrackingDetailsDAO.insert(response);
		}
		TrackingServiceHelper.getInstance().processForZipLocation(response);
		return response;
	}

	private ShipmentTrackingResponse checkDB(ShipmentTrackingRequest request) {
		ShipmentTrackingDetailsDAO.findByTrackingNumber(request.getTrackingNumber());
		return null;
	}
}