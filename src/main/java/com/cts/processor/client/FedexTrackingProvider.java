package com.cts.processor.client;

import java.util.ArrayList;
import java.util.List;

import com.cts.processor.TrackingServiceHelper;
import com.cts.processor.datamodel.Address;
import com.cts.processor.datamodel.ShipmentStatus;
import com.cts.processor.datamodel.ShipmentTrackingRequest;
import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.cts.processor.datamodel.ShippingCarrier;
import com.cts.processor.datamodel.TrackingEvent;
import com.fedex.track.stub.ClientDetail;
import com.fedex.track.stub.TrackDetail;
import com.fedex.track.stub.TrackEvent;
import com.fedex.track.stub.TrackIdentifierType;
import com.fedex.track.stub.TrackPackageIdentifier;
import com.fedex.track.stub.TrackPortType;
import com.fedex.track.stub.TrackReply;
import com.fedex.track.stub.TrackRequest;
import com.fedex.track.stub.TrackServiceLocator;
import com.fedex.track.stub.TransactionDetail;
import com.fedex.track.stub.VersionId;
import com.fedex.track.stub.WebAuthenticationCredential;
import com.fedex.track.stub.WebAuthenticationDetail;

public class FedexTrackingProvider extends TrackingProvider {

	private static FedexTrackingProvider m_self = new FedexTrackingProvider();

	// Private constructor in order to make singleton.
	private FedexTrackingProvider() {

	}

	public static FedexTrackingProvider getInstance() {
		return m_self;
	}

	@Override
	public ShipmentTrackingResponse getTracking(
			ShipmentTrackingRequest trackingRequest) {
		
		ShipmentTrackingResponse response = TrackingServiceHelper.getInstance()
				.getDefaultResponse();
		TrackRequest request = new TrackRequest();

		request.setClientDetail(createClientDetail());
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		//
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail
				.setCustomerTransactionId("java sample - Tracking Request"); 
		// This is a reference field for the customer. Any value can be used and will be provided in the response.
		request.setTransactionDetail(transactionDetail);

		VersionId versionId = new VersionId("trck", 6, 0, 0);
		request.setVersion(versionId);

		TrackPackageIdentifier packageIdentifier = new TrackPackageIdentifier();
		// packageIdentifier.setValue(getSystemProperty("TrackingNumber"));
		packageIdentifier.setValue(trackingRequest.getTrackingNumber());
		// packageIdentifier.setValue("502369599566");
		packageIdentifier
				.setType(TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG); 
		// Track identifier types are TRACKING_NUMBER_OR_DOORTAG, TRACKING_CONTROL_NUMBER, ....
		request.setPackageIdentifier(packageIdentifier);
		request.setIncludeDetailedScans(new Boolean(true));

		try {
			// Initializing the service
			TrackServiceLocator service;
			service = new TrackServiceLocator();
			updateEndPoint(service);
			TrackPortType port = service.getTrackServicePort();
			TrackReply reply = port.track(request);
			response = convertToResponse(reply);
		} catch (Exception e) {

		}

		return response;
	}

	private ShipmentTrackingResponse convertToResponse(TrackReply reply) {
		ShipmentTrackingResponse response = TrackingServiceHelper.getInstance()
				.getDefaultResponse();
		TrackDetail[] trackingDetails = reply.getTrackDetails();
		
		if(trackingDetails != null && trackingDetails.length > 0){
			TrackDetail details = trackingDetails[0];
			if(details == null){
				return response;
			}
			response.setStatus( ShipmentStatus.valueOf(details.getStatusCode()))  ;
			com.fedex.track.stub.Address destination = details.getDestinationAddress();
			if(destination != null){
				Address destinationAddress = new Address();
				destinationAddress.setCity(destination.getCity());
				destinationAddress.setPostalCode(destination.getPostalCode());
				destinationAddress.setResidential(destination.getResidential() == null ? false : destination.getResidential());
				destinationAddress.setStateOrProvinceCode(destination.getStateOrProvinceCode());
				destinationAddress.setUrbanizationCode(destination.getUrbanizationCode());
				destinationAddress.setCountryCode(destination.getCountryCode());
				response.setDestinationAddress(destinationAddress);
			}
			java.util.Calendar estimatedDelivery = details.getEstimatedDeliveryTimestamp();
			if(estimatedDelivery != null){
				response.setEstimatedDelivery(estimatedDelivery.getTime());
			}
			java.util.Calendar actualDelivery = details.getActualDeliveryTimestamp();
			if(actualDelivery != null){
				response.setDeliveryDate(actualDelivery.getTime());
			}
			response.setCarrier("FedEx");
			response.setTrackingNumber(details.getTrackingNumber());
			TrackEvent[] events = details.getEvents();
			if(events == null){
				return response;
			}
			List<TrackingEvent> trackingEventList = new ArrayList<TrackingEvent>();
			response.setTrackingEvents(trackingEventList);
			for(int i =0 ; i < events.length ; i ++){
				TrackEvent event = events[i];
				if(event == null){
					continue;
				}
				TrackingEvent trackingEvent = new TrackingEvent();
				trackingEvent.setEventDescription(event.getEventDescription());
				java.util.Calendar calander = event.getTimestamp();
				if(calander != null){
					trackingEvent.setEventDate(calander.getTime());
				}
				trackingEventList.add(trackingEvent);
//				trackingEvent.setStatus(status);TODO
				trackingEvent.setStatusExceptionDescription(event.getStatusExceptionDescription());
				com.fedex.track.stub.Address address = event.getAddress();
				if(address != null){
					Address eventAddress = new Address();
					eventAddress.setCity(address.getCity());
					eventAddress.setCountryCode(address.getCountryCode());
					eventAddress.setPostalCode(address.getPostalCode());
					eventAddress.setResidential(address.getResidential() == null? false : address.getResidential());
					eventAddress.setStateOrProvinceCode(address.getStateOrProvinceCode());
					eventAddress.setUrbanizationCode(address.getUrbanizationCode());
					trackingEvent.setAddress(eventAddress);
				}
				event.getEventType();//--> Event code ShippingStatusEnum
				if(event.getTimestamp() != null){
					trackingEvent.setEventDate(event.getTimestamp().getTime());
				}
				try{
					trackingEvent.setStatus(ShipmentStatus.valueOf(event.getEventType()));
				}catch(Exception e){
				}
				event.getStatusExceptionCode();
			}
		}
		return response;
	}

	private static void updateEndPoint(TrackServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setTrackServicePortEndpointAddress(endPoint);
		}
	}

	private static ClientDetail createClientDetail() {
        ClientDetail clientDetail = new ClientDetail();
        String accountNumber = System.getProperty("accountNumber");
        String meterNumber = System.getProperty("meterNumber");
        
        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (accountNumber == null) {
//        	accountNumber = "510087585"; // Replace "XXX" with clients account number
        	accountNumber = "364908776";
        }
        if (meterNumber == null) {
//        	meterNumber = "118571578"; // Replace "XXX" with clients meter number
        	meterNumber = "105144842";
        }
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
	}
	
	private static WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
        String key = System.getProperty("key");
        String password = System.getProperty("password");
        
        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (key == null) {
//        	key = "irjOg2upIlw1zpmq"; // Replace "XXX" with clients key
        	key = "icXMly15PEVRik9t";
        }
        if (password == null) {
//        	password = "tumvPyfGT1AfxfbcZV1H5C66L"; // Replace "XXX" with clients password
        	password = "TsLAm4V11p3giquC3FSgS9R0S";
        }
        wac.setKey(key);
        wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}
}