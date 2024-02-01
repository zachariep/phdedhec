package com.biserv.victorp.research.phdedhec;

public class BirthRecordResult {
    private Long year;
    private String state;
    private String city;
    private String county;
    private String raceOfChild;
    private String raceOfFather;
    private String raceOfMother;
    private String hispanicOriginOfFather;
    private String hispanicOriginOfMother;
    private Long count;

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getRaceOfChild() {
        return raceOfChild;
    }

    public void setRaceOfChild(String raceOfChild) {
        this.raceOfChild = raceOfChild;
    }

    public String getRaceOfFather() {
        return raceOfFather;
    }

    public void setRaceOfFather(String raceOfFather) {
        this.raceOfFather = raceOfFather;
    }

    public String getRaceOfMother() {
        return raceOfMother;
    }

    public void setRaceOfMother(String raceOfMother) {
        this.raceOfMother = raceOfMother;
    }

    public String getHispanicOriginOfFather() {
        return hispanicOriginOfFather;
    }

    public void setHispanicOriginOfFather(String hispanicOriginOfFather) {
        this.hispanicOriginOfFather = hispanicOriginOfFather;
    }

    public String getHispanicOriginOfMother() {
        return hispanicOriginOfMother;
    }

    public void setHispanicOriginOfMother(String hispanicOriginOfMother) {
        this.hispanicOriginOfMother = hispanicOriginOfMother;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
