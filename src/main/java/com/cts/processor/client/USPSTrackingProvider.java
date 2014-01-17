package com.cts.processor.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.cts.processor.TrackingServiceHelper;
import com.cts.processor.datamodel.Address;
import com.cts.processor.datamodel.ShipmentStatus;
import com.cts.processor.datamodel.ShipmentTrackingRequest;
import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.cts.processor.datamodel.TrackingEvent;
import com.cts.service.usps.TrackInfo;
import com.cts.service.usps.USPSResponse;

public class USPSTrackingProvider extends TrackingProvider {

	private static USPSTrackingProvider m_self = new USPSTrackingProvider();
	private static String REPLACE_STRING = "#TRACKNUM#";
	private static String END_POINT_URL = "http://production.shippingapis.com/ShippingAPI.dll?API=TrackV2&XML=<TrackRequest USERID=\"040KODAG0707\"><TrackID ID=\""
			+ REPLACE_STRING + "\"></TrackID></TrackRequest>";
	/*
	 * private static String END_POINT_URL =
	 * "http://production.shippingapis.com/ShippingAPITest.dll?API=TrackV2&XML=<TrackRequest USERID=\"040KODAG0707\"><TrackID ID=\""
	 * + REPLACE_STRING + "\"></TrackID></TrackRequest>";
	 */
	// Private constructor in order to make singleton.
	String summary = "Your item was delivered at 10:36 am on May 8, 2013 in BIRMINGHAM, AL 35244.";
	String event = "Out for Delivery, May 8, 2013, 8:53 am, BIRMINGHAM, AL 35244";

	private USPSTrackingProvider() {

	}

	public static USPSTrackingProvider getInstance() {
		return m_self;
	}

	@Override
	public ShipmentTrackingResponse getTracking(
			ShipmentTrackingRequest trackingRequest) {

		ShipmentTrackingResponse response = TrackingServiceHelper.getInstance()
				.getDefaultResponse();

		try {
			USPSResponse uspsResponse = getResponse(trackingRequest.getTrackingNumber());
			mapResponse(response, uspsResponse);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return response;
	}

	private void mapResponse(ShipmentTrackingResponse response,
			USPSResponse uspsResponse) {
		
		if(uspsResponse == null){
			return;
		}
		
		TrackInfo trackInfo = uspsResponse.getTrackingInfo();
		if(trackInfo == null){
			return;
		}
		response.setTrackingNumber(trackInfo.getTrackingNumber());
		List<String> events = trackInfo.getTrackingEvents();
		if(events!= null && events.size() >0){
			processEvents(events, response);
			response.setCarrier("USPS");
			processSummary(trackInfo.getTrackingSummary(), response);
		}else{
			response.setCarrier("Unknown");
		}
		
	}

	private void processEvents(List<String> events,
			ShipmentTrackingResponse response) {
		if(events == null){
			return;
		}
		if(events.size() <=0){
			return;
		}
		List<TrackingEvent> trackingEvents = new ArrayList<TrackingEvent>();
		response.setTrackingEvents(trackingEvents);
		for(String event : events){
			if(event == null){
				continue;
			}
			StringTokenizer tokens = new StringTokenizer(event, ",");
			
			if(tokens.countTokens() != 6){
				continue;
			}
			TrackingEvent trackingEvent = new TrackingEvent();
			trackingEvents.add(trackingEvent);
			
			trackingEvent.setEventDescription(tokens.nextToken());
			String date = (tokens.nextToken()+""+tokens.nextToken() +""+tokens.nextToken().toUpperCase());
			SimpleDateFormat dateFormater = new SimpleDateFormat(" MMM d yyyy h:mm a", Locale.US);
			try { 
				trackingEvent.setEventDate(dateFormater.parse(date));
			} catch (ParseException e) {
				System.out.println(e+"");
			}
			
			Address address = new Address();
			trackingEvent.setAddress(address);
			address.setCity(tokens.nextToken());
			StringTokenizer zip = new StringTokenizer(tokens.nextToken());
			address.setStateOrProvinceCode(zip.nextToken());
			address.setPostalCode(zip.nextToken());
		}
	}

	private void processSummary(String trackingSummary,
			ShipmentTrackingResponse response) {
		if(trackingSummary == null){
			return;
		}
		
		if(trackingSummary.contains("delivered")){
			response.setStatus(ShipmentStatus.DL);
		}else{
			response.setStatus(ShipmentStatus.IT);
		}
		
		String[] tokens = trackingSummary.split(",");
		
		if(tokens.length != 3){
			return;
		}
		String first = tokens[0];
		
		String[] firstTokens = first.split("at");
		String summary = firstTokens[0];
		String[] dateTokens = firstTokens[1].split("on");
		if(firstTokens != null && firstTokens.length == 3){
			summary = firstTokens[0]+firstTokens[1];
			dateTokens = firstTokens[2].split("on");
		}
		String timeString = dateTokens[0].toUpperCase();
		String monthString = dateTokens[1];
		
		String second = tokens[1];
		String[] secondTokens = second.split("in");
		String year = secondTokens[0];
		String city = secondTokens[1];
		
		SimpleDateFormat dateFormater = new SimpleDateFormat("MMM d yyyy  h:mm a", Locale.US);
		String dateString = (monthString+year+timeString).trim();
		
		try { 
			response.setDeliveryDate(dateFormater.parse(dateString));
		} catch (ParseException e) {
			System.out.println(e+"");
		}
		
		Address address = new Address();
		response.setDestinationAddress(address);
		
		String third = tokens[2];
		String[] thirdTokens = third.split(" ");
		
		String sate = thirdTokens[1];
		String zip = thirdTokens[2];
		
		address.setCity(city);
		address.setPostalCode(zip);
		address.setStateOrProvinceCode(sate);
		
	}

	private static USPSResponse getResponse(String trackingNumber) throws IOException,
			URISyntaxException, JAXBException {
		String endpoint = END_POINT_URL.replaceFirst(REPLACE_STRING,
				trackingNumber);
		URL url = new URL(endpoint);
		URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
				url.getPort(), url.getPath(), url.getQuery(), url.getRef());
		url = uri.toURL();
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/xml");

		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

		StringBuffer output = new StringBuffer();
		String line;
		while ((line = br.readLine()) != null) {
			output.append(line);
		}
		conn.disconnect();
		
		JAXBContext jaxbContext = JAXBContext.newInstance(USPSResponse.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(output.toString());
		USPSResponse response = (USPSResponse) unmarshaller.unmarshal(reader);
		return response;
	}

	public static void main(String args[]) throws IOException,
			URISyntaxException, JAXBException {
		ShipmentTrackingRequest request = new ShipmentTrackingRequest();
//		request.setTrackingNumber("9400109699937690653490");
		request.setTrackingNumber("9400109699939988973754");
		m_self.getTracking(request);
		System.out.println("----------------------------");
		request.setTrackingNumber("9400109699937690653490");
		m_self.getTracking(request);
		System.out.println("----------------------------");
		getResponse("9400109699939870990326");
	}
}