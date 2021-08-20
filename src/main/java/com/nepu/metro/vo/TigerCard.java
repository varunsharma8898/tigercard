package com.nepu.metro.vo;

public class TigerCard {

    private String cardNumber;

    private Contact contact;

    public TigerCard(String cardNumber, Contact contact) {
        this.cardNumber = cardNumber;
        this.contact = contact;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
