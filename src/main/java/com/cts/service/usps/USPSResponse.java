package com.cts.service.usps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TrackResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class USPSResponse {
	@XmlElement(name = "TrackInfo")
	TrackInfo  trackingInfo;

	public TrackInfo getTrackingInfo() {
		return trackingInfo;
	}

	public void setTrackingInfo(TrackInfo trackingInfo) {
		this.trackingInfo = trackingInfo;
	}
}