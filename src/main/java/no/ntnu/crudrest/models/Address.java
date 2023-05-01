package no.ntnu.crudrest.models;

import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String streetAddress;
    private String postalCode;
    private String city;

    private String firstName;

    private String lastName;

    private String phoneNumber;
    public Address(){

    }

    public Address(String streetAddress, String postalCode, String city, String firstName, String lastName, String phoneNumber) {
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setFirstName(String name){this.firstName = name;}

    public String getFirstName(){return firstName;}
    public void setLastName(String name){this.lastName = name;}
    public String getLastName(){return lastName;}

    public void setPhoneNumber(String number){this.phoneNumber = number;}
    public String getPhoneNumber(){return phoneNumber;}

}
