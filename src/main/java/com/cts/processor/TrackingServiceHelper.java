package com.cts.processor;

import java.util.List;

import com.cts.dal.ZipCodeLookUpDAO;
import com.cts.dal.ZipLocationBean;
import com.cts.processor.datamodel.Address;
import com.cts.processor.datamodel.ShipmentTrackingResponse;
import com.cts.processor.datamodel.TrackingEvent;

public class TrackingServiceHelper {

	private static TrackingServiceHelper m_self = new TrackingServiceHelper();

	// Private constructor in order to make singleton.
	private TrackingServiceHelper() {

	}

	public static TrackingServiceHelper getInstance() {
		return m_self;
	}

	public ShipmentTrackingResponse getDefaultResponse() {
		ShipmentTrackingResponse response = new ShipmentTrackingResponse();
		return response;
	}

	public void processForZipLocation(ShipmentTrackingResponse response) {
		if (response == null) {
			return;
		}
		Address destinationAddress = response.getDestinationAddress();
		if (destinationAddress != null) {
			updateLongitude(destinationAddress);
		}
		List<TrackingEvent> eventList = response.getTrackingEvents();

		if (eventList == null) {
			return;
		}

		for (TrackingEvent event : eventList) {
			if (event == null) {
				continue;
			}
			Address eventAddress = event.getAddress();
			if (eventAddress != null) {
				updateLongitude(eventAddress);
			}
		}
	}

	private void updateLongitude(Address address) {
		if (address == null) {
			return;
		}
		String postalCode = address.getPostalCode();

		if (postalCode != null && postalCode.length() > 0) {
			if (postalCode.length() > 5) {
				postalCode = postalCode.substring(0, 5);
			}
			ZipLocationBean bean = ZipCodeLookUpDAO.findByZipCode(postalCode);
			if (bean != null) {
				if (bean.getCity() != null && bean.getCity().length() > 0) {
					address.setCity(bean.getCity());
				}
				if (bean.getState() != null && bean.getState().length() > 0) {
					address.setStateOrProvinceCode(bean.getState());
				}
				address.setLatitude(bean.getLatitude());
				address.setLongitude(bean.getLongitude());
			}
		}
	}
}