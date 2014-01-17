package com.cts.processor.datamodel;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TrackingEvent {

	private ShipmentStatus status;
	private Address address;
	private String eventDescription;
	private String statusExceptionDescription;
	private Date eventDate;

	public String getStatusExceptionDescription() {
		return statusExceptionDescription;
	}

	public void setStatusExceptionDescription(String statusExceptionDescription) {
		this.statusExceptionDescription = statusExceptionDescription;
	}

	public ShipmentStatus getStatus() {
		return status;
	}

	public void setStatus(ShipmentStatus status) {
		this.status = status;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
}