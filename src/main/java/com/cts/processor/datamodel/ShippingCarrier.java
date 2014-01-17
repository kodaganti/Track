package com.cts.processor.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum ShippingCarrier {

	FEDEX,
	USPS,
	UPS;
}
