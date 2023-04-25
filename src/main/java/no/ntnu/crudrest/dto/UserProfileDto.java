package no.ntnu.crudrest.dto;
/**
 * Data transfer object (DTO) for submitting changes to user profile data
 */

public class UserProfileDto {
    private final String firstName;
    private final String lastName;
    private final int phoneNumber;
    private final String address;
    private final int postalCode;
    private final String city;



    public UserProfileDto(String firstName, String lastName, int phoneNumber, String address, int postalCode, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.city = city;

    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }
}
