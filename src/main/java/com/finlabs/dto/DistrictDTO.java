package com.finlabs.dto;

public class DistrictDTO {
	
	private String districtName;
    private String districtNameHindi;
    
    public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	
	public String getDistrictNameHindi() {
		return districtNameHindi;
	}
	public void setDistrictNameHindi(String districtNameHindi) {
		this.districtNameHindi = districtNameHindi;
	}
	
	@Override
	public String toString() {
		return "DistrictDTO [districtName=" + districtName + ", districtNameHindi=" + districtNameHindi + "]";
	}
    
}
