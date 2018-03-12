package com.viewscenes.bean.dicManager;
/**
 * 方位角信息Bean
 */
import com.viewscenes.bean.BaseBean;

public class AzimuthBean extends BaseBean {

	public String id;
	public String station_name;
    public String city_name;
    public String circle_distance;
    public String azimuth;
    public String longitude;
    public String latitude;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStation_name() {
		return station_name;
	}
	public void setStation_name(String station_name) {
		this.station_name = station_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	public String getCircle_distance() {
		return circle_distance;
	}
	public void setCircle_distance(String circle_distance) {
		this.circle_distance = circle_distance;
	}
	public String getAzimuth() {
		return azimuth;
	}
	public void setAzimuth(String azimuth) {
		this.azimuth = azimuth;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
    
}
