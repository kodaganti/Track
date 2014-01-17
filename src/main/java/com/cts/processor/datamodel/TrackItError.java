package com.cts.processor.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TrackItError {

	private String errorCode;
	private int errorId;

	private TrackItError() {

	}

	private TrackItError(String errorCode, int errorId) {
		this.errorCode = errorCode;
		this.errorId = errorId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public int getErrorId() {
		return errorId;
	}

	// All list of error IDs
	public static TrackItError CARRIER_ID_INVALID = new TrackItError(
			"CARRIER_ID_INVALID", 1170);
}