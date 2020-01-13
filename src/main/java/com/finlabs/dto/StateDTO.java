package com.finlabs.dto;

public class StateDTO {
	
	private int id;
	private String stateName;
	private String stateNameHindi;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getStateName() {
		return stateName;
	}
	public void setStateName(String stateName) {
		this.stateName = stateName;
	}
	
	public String getStateNameHindi() {
		return stateNameHindi;
	}
	public void setStateNameHindi(String stateNameHindi) {
		this.stateNameHindi = stateNameHindi;
	}
	
	@Override
	public String toString() {
		return "StateDTO [id=" + id + ", stateName=" + stateName + ", stateNameHindi=" + stateNameHindi + "]";
	}

}
