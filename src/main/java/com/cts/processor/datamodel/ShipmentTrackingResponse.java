package com.cts.processor.datamodel;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ShipmentTrackingResponse {

	private String trackingNumber;
	private String carrier;
	private ShipmentStatus status;
	private List<TrackingEvent> trackingEvents;
	private Date estimatedDelivery;
	private Date deliveryDate;
	private Address destinationAddress;
	
	public Address getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(Address destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public Date getEstimatedDelivery() {
		return estimatedDelivery;
	}

	public void setEstimatedDelivery(Date estimatedDelivery) {
		this.estimatedDelivery = estimatedDelivery;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}

	public List<TrackingEvent> getTrackingEvents() {
		return trackingEvents;
	}

	public void setTrackingEvents(List<TrackingEvent> trackingEvents) {
		this.trackingEvents = trackingEvents;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
}