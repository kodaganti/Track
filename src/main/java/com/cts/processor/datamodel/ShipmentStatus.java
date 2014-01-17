package com.cts.processor.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public enum ShipmentStatus {

/*	ELECTRINIC_INFO_RECEIVED("ELECTRINIC_INFO_RECEIVED"), 
	MANIFEST("MANIFEST"), 
	FIRST_SCAN("FIRST_SCAN"), 
	DELIVERED("DELIVERED"), 
	In_TRANSIT("In_TRANSIT"), 
	DELIVERY_EXCEPTION("DELIVERY_EXCEPTION"), 
	OUT_FOR_DELIVERY("OUT_FOR_DELIVERY"),
	
	AT_AIRPORT("AT_AIRPORT"),
	AT_DELIVEY("AT_DELIVERY"),
	At_FedEx_Facility("At_FedEx_Facility"),
	At_Pickup("At_Pickup"),
	Shipment_Canceled("Shipment_Canceled"),
	Location_Changed("Location_Changed"),
	Delivery_Exception("Delivery_Exception"),
	Delivered("Delivered"),
	Departed_FedEx_Location("Departed_FedEx_Location"),
	Vehicle_Furnished("Vehicle_Furnished"),
	Vehicle_Dispatched("Vehicle_dispatched"),
	Delay("Delay"),
	Enroute_To_Airport_Delay("Enroute_To_Airport_Delay"),
	;*/
	
	AA ("AA", "At Airport"), 
	AR("AR", "At local FedEx facility"),
	EO ("EO","Enroute to Origin Airport"),
	AD ("AD","At Delivery"), 
	EP ("EP","Enroute to Pickup"),
	AF ("AF","At FedEx Facility"), 
	FD ("FD","At FedEx Destination"),
	AP ("AP","At Pickup") ,
	HL ("HL","Hold at Location"),
	CA ("CA","Shipment Canceled"), 
	IT ("IT","In Transit"),
	CH ("CH","Location Changed"), 
	LO ("LO","Left Origin"),
	DE ("DE","Delivery Exception"), 
	OC ("OC","Order Created"),
	DL ("DL","Delivered"), 
	OD ("OD","Out for Delivery"),
	DP ("DP","Departed FedEx Location"), 
	PF ("PF","Plane in Flight"),
	DR ("DR","Vehicle Furnished, Not Used"), 
	PL ("PL","Plane Landed"),
	DS ("DS","Vehicle dispatched"), 
	PU ("PU","Picked Up"),
	DY ("DY","Delay"), 
	RS ("RS","Return to Shipper"),
	EA ("EA","Enroute to Airport delay"), 
	SE ("SE","Shipment Exception"),
	ED ("ED","Enroute to Delivery"), 
	SF ("SF","At Sort Facility"),
	SP ("SP", "Split status - multiple statuses"),
	TR ("TR","Transfer");

	String code;
	String desc;

	ShipmentStatus(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
}