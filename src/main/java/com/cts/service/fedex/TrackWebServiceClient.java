package com.cts.service.fedex;
import com.fedex.track.stub.*;

/** 
 * Demo of using the Track service with Axis 
 * to track a shipment.
 * <p>
 * com.fedex.track.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.track.stub http://www.fedex.com/...../TrackService?wsdl
 * </pre>
 * 
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */
public class TrackWebServiceClient {
	//
	public static void main(String[] args) throws Exception {   

		//
	    TrackRequest request = new TrackRequest();

        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setCustomerTransactionId("java sample - Tracking Request"); //This is a reference field for the customer.  Any value can be used and will be provided in the response.
        request.setTransactionDetail(transactionDetail);
 
        //
        VersionId versionId = new VersionId("trck", 6, 0, 0);
        request.setVersion(versionId);
        //
        TrackPackageIdentifier packageIdentifier = new TrackPackageIdentifier();
//        packageIdentifier.setValue(getSystemProperty("TrackingNumber")); // tracking number
        
        packageIdentifier.setValue("502369599566"); 
        packageIdentifier.setType(TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG); // Track identifier types are TRACKING_NUMBER_OR_DOORTAG, TRACKING_CONTROL_NUMBER, ....
        request.setPackageIdentifier(packageIdentifier);
        request.setIncludeDetailedScans(new Boolean(true));

	    //
		try {
			// Initializing the service
			TrackServiceLocator service;
			TrackPortType port;
			//
			service = new TrackServiceLocator();
			updateEndPoint(service);
			port = service.getTrackServicePort();
		    //
			TrackReply reply = port.track(request); // This is the call to the web service passing in a request object and returning a reply object
			//
			if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
			{
				System.out.println("Tracking detail\n");
				TrackDetail td[] = reply.getTrackDetails();
				for (int i=0; i< td.length; i++) { // package detail information
					System.out.println("Package # : " + td[i].getPackageSequenceNumber() 
								+ " and Package count: " + td[i].getPackageCount());
					System.out.println("Tracking number: " + td[i].getTrackingNumber() 
								+ " and Tracking number unique identifier: " + td[i].getTrackingNumberUniqueIdentifier());
					System.out.println("Status code: " + td[i].getStatusCode()); 
					System.out.println("Status description: " + td[i].getStatusDescription());
					if(td[i].getOtherIdentifiers() != null)
					{
						TrackPackageIdentifier[] tpi = td[i].getOtherIdentifiers();
						for (int j=0; j< tpi.length; j++) {
							System.out.println(tpi[j].getType() + " " + tpi[j].getValue());
						}
					}
					print("Packaging", td[i].getPackaging());
					printWeight("Package weight", td[i].getPackageWeight());
					printWeight("Shipment weight", td[i].getShipmentWeight());
				
					print("Ship date & time", td[i].getShipTimestamp().getTime());
					//System.out.println("Destination: " + td[i].getDestinationAddress().getCity() 
					//		+ " " + td[i].getDestinationAddress().getPostalCode()
					//		+ " " + td[i].getDestinationAddress().getCountryCode());
					

					TrackEvent[] trackEvents = td[i].getEvents();
					if (trackEvents != null) {
						System.out.println("Events:");
						for (int k = 0; k < trackEvents.length; k++) {
							System.out.println("  Event no.: " + (k+1));
							TrackEvent trackEvent = trackEvents[k];
							if (trackEvent != null) {
								print("    Timestamp", trackEvent.getTimestamp().getTime());
								print("    Description", trackEvent.getEventDescription());
								Address address = trackEvent.getAddress();
								if (address != null) {
									System.out.println(address.getCity());
									print("    City", address.getCity());
									print("    State", address.getStateOrProvinceCode());
								}
							}
						}
					}
				}
			}
			System.out.println("");

			printNotifications(reply.getNotifications());

			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
			notificationSeverityType.equals(NotificationSeverityType.NOTE)    ||
			notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
 		return false;
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
	
	private static void printNotifications(Notification[] notifications) {
		System.out.println("Notifications:");
		if (notifications == null || notifications.length == 0) {
			System.out.println("  No notifications returned");
		}
		for (int i=0; i < notifications.length; i++){
			Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				System.out.println("null");
				continue;
			} else {
				System.out.println("");
			}
			NotificationSeverityType nst = n.getSeverity();

			System.out.println("    Severity: " + (nst == null ? "null" : nst.getValue()));
			System.out.println("    Code: " + n.getCode());
			System.out.println("    Message: " + n.getMessage());
			System.out.println("    Source: " + n.getSource());
		}
	}
	
	private static void updateEndPoint(TrackServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setTrackServicePortEndpointAddress(endPoint);
		}
	}

	private static void print(String msg, Object obj) {
		if (msg == null || obj == null) {
			return;
		}
		System.out.println(msg + ": " + obj.toString());
	}
	
	
	private static void printWeight(String msg, Weight weight) {
		if (msg == null || weight == null) {
			return;
		}
		System.out.println(msg + ": " + weight.getValue() + " " + weight.getUnits());
	}

	private static String getSystemProperty(String property){
		String returnProperty = System.getProperty(property);
		if (returnProperty == null){
			return "XXX";
		}
		return returnProperty;
	}

}