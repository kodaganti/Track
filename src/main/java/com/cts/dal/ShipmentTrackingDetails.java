package com.cts.dal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cts.processor.datamodel.ShipmentTrackingResponse;

@Entity
@Table(name = "Tracking_Details", schema = "DEMO@cassandra_pu2")
public class ShipmentTrackingDetails {

	@Id
	@Column(name = "TRACKING_NUMBER")
	private String trackingNumber;
	@Column(name = "DETAILS")
	private String details;
	
	public ShipmentTrackingResponse getResponse() {
		return response;
	}

	public void setResponse(ShipmentTrackingResponse response) {
		this.response = response;
	}

	private ShipmentTrackingResponse response; 

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
}