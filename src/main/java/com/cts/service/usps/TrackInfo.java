package com.cts.service.usps;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TrackInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class TrackInfo {

	@XmlAttribute(name = "ID")
	String trackingNumber;
	@XmlElement(name = "TrackSummary")
	String trackingSummary;
	@XmlElement(name = "TrackDetail")
	List<String> trackingEvents;

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getTrackingSummary() {
		return trackingSummary;
	}

	public void setTrackingSummary(String trackingSummary) {
		this.trackingSummary = trackingSummary;
	}

	public List<String> getTrackingEvents() {
		return trackingEvents;
	}

	public void setTrackingEvents(List<String> trackingEvents) {
		this.trackingEvents = trackingEvents;
	}
}
