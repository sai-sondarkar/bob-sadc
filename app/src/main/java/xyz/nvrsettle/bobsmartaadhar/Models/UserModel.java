package xyz.nvrsettle.bobsmartaadhar.Models;

/**
 * Created by sai on 11/11/17.
 */

public class UserModel {


    public String aadhar_number;
    public String name;
    public String address;
    public String contact;
    public String image_url;
    public float currentAmountInWallet;
    public String pin;


    public UserModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCurrentAmountInWallet() {
        return currentAmountInWallet;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setCurrentAmountInWallet(float currentAmountInWallet) {
        this.currentAmountInWallet = currentAmountInWallet;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getAadhar_number() {
        return aadhar_number;
    }

    public void setAadhar_number(String aadhar_number) {
        this.aadhar_number = aadhar_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}

