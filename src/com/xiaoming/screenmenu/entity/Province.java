package com.xiaoming.screenmenu.entity;

import java.util.List;

public class Province {

	private String name;
	private List<City> citys;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCitys() {
		return citys;
	}

	public void setCitys(List<City> citys) {
		this.citys = citys;
	}

}
