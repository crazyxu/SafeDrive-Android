package me.xucan.safedrive.bean;

/**
 * Created on 2016/5/30.
 */
public class Location {
    //省
    private String province;
    //市
    private String city;
    //区
    private String district;
    //街道
    private String street;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
