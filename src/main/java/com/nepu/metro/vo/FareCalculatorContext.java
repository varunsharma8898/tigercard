package com.nepu.metro.vo;

public class FareCalculatorContext {

    private TigerCard tigerCard;

    private Journey journey;

    private Fare fare;

    private int applicableDailyLimit;

    private int applicableWeeklyLimit;

    private int totalDailyFareAmount;

    private int totalWeeklyFareAmount;

    public FareCalculatorContext(TigerCard tigerCard, Journey journey) {
        this.tigerCard = tigerCard;
        this.journey = journey;
    }

    public TigerCard getTigerCard() {
        return tigerCard;
    }

    public void setTigerCard(TigerCard tigerCard) {
        this.tigerCard = tigerCard;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Fare getFare() {
        return fare;
    }

    public void setFare(Fare fare) {
        this.fare = fare;
    }

    public int getApplicableDailyLimit() {
        return applicableDailyLimit;
    }

    public void setApplicableDailyLimit(int applicableDailyLimit) {
        this.applicableDailyLimit = applicableDailyLimit;
    }

    public int getApplicableWeeklyLimit() {
        return applicableWeeklyLimit;
    }

    public void setApplicableWeeklyLimit(int applicableWeeklyLimit) {
        this.applicableWeeklyLimit = applicableWeeklyLimit;
    }

    public int getTotalDailyFareAmount() {
        return totalDailyFareAmount;
    }

    public void setTotalDailyFareAmount(int totalDailyFareAmount) {
        this.totalDailyFareAmount = totalDailyFareAmount;
    }

    public int getTotalWeeklyFareAmount() {
        return totalWeeklyFareAmount;
    }

    public void setTotalWeeklyFareAmount(int totalWeeklyFareAmount) {
        this.totalWeeklyFareAmount = totalWeeklyFareAmount;
    }
}
