package com.cts.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.cts.processor.TrackingServiceProcessor;
import com.cts.processor.datamodel.ShipmentTrackingRequest;
import com.cts.processor.datamodel.ShipmentTrackingResponse;

@Path("/trackIt")
public class TrackItResource {

	public static String regIds ="";
	@GET
	@Path("/{trackingNumber}")
	@Produces("application/json")
	public ShipmentTrackingResponse trackPackage(
			@PathParam("trackingNumber") String trackingNumber) {
		ShipmentTrackingRequest request = new ShipmentTrackingRequest();
		request.setTrackingNumber(trackingNumber);
		return TrackingServiceProcessor.getInstance().getStatus(request);
	}
	@GET
	@Path("/{trackingNumber}/{regId}")
	@Produces("application/json")
	public ShipmentTrackingResponse trackPackage(
			@PathParam("regId") String trackingNumber, @PathParam("trackingNumber") String regId) {
		ShipmentTrackingRequest request = new ShipmentTrackingRequest();
		regIds = regId;
		System.out.println(regId);
		request.setTrackingNumber(trackingNumber);
		return TrackingServiceProcessor.getInstance().getStatus(request);
	}

	@GET
	@Path("/{trackingNumber}/{carrier}/{regId}")
	@Produces("application/json")
	public ShipmentTrackingResponse trackPackage(
			@PathParam("trackingNumber") String trackingNumber,
			@PathParam("carrier") String carrier, @PathParam("regId") String regId) {
		ShipmentTrackingRequest request = new ShipmentTrackingRequest();
		request.setTrackingNumber(trackingNumber);
		regIds = regId;
		request.setCarrier(carrier);
		return TrackingServiceProcessor.getInstance().getStatus(request);
	}
	
	@GET
	@Path("/sendMessage/{message}")
	@Produces("application/json")
	public String sendResponse(@PathParam("message") String message){
		MessageNotifier.notifier(regIds);
		return "Message Sucessfully Sent";
	}
}