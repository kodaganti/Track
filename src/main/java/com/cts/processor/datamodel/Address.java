package com.cts.processor.datamodel;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Address {
    /* Name of city, town, etc. */
    private java.lang.String city;

    /* Identifying abbreviation for US state, Canada province, etc.
     * Format and presence of this field will vary, depending on country. */
    private java.lang.String stateOrProvinceCode;

    /* Identification of a region (usually small) for mail/package
     * delivery. Format and presence of this field will vary, depending on
     * country. */
    private java.lang.String postalCode;

    /* Relevant only to addresses in Puerto Rico. */
    private java.lang.String urbanizationCode;

    /* The two-letter code used to identify a country. */
    private java.lang.String countryCode;

    /* Indicates whether this address residential (as opposed to commercial). */
    private java.lang.Boolean residential;
    
    private double longitude;
    
    private double latitude;

	public java.lang.String getCity() {
		return city;
	}

	public void setCity(java.lang.String city) {
		this.city = city;
	}

	public java.lang.String getStateOrProvinceCode() {
		return stateOrProvinceCode;
	}

	public void setStateOrProvinceCode(java.lang.String stateOrProvinceCode) {
		this.stateOrProvinceCode = stateOrProvinceCode;
	}

	public java.lang.String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(java.lang.String postalCode) {
		this.postalCode = postalCode;
	}

	public java.lang.String getUrbanizationCode() {
		return urbanizationCode;
	}

	public void setUrbanizationCode(java.lang.String urbanizationCode) {
		this.urbanizationCode = urbanizationCode;
	}

	public java.lang.String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(java.lang.String countryCode) {
		this.countryCode = countryCode;
	}

	public java.lang.Boolean getResidential() {
		return residential;
	}

	public void setResidential(java.lang.Boolean residential) {
		this.residential = residential;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}