package sample;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;
import java.util.UUID;

public class Customer {
    private LongProperty pKey;
    private String firstName;
    private String lastName;
    private String number;
    private String emailAddress;
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String userID;

    /**
     * Constructor for reading from DB
     * @param pKey
     * @param firstName
     * @param lastName
     * @param number
     * @param emailAddress
     * @param address
     * @param city
     * @param zipcode
     * @param state
     */
    public Customer(long pKey, String firstName, String lastName, String number,
                    String emailAddress, String address, String city, String zipcode, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.emailAddress = emailAddress;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.pKey = new SimpleLongProperty(pKey);
        this.userID = (this.firstName + this.lastName + this.pKey.getValue()).toLowerCase();
    }

    /**
     * Constructor for adding a new Customer to the DB
     * @param firstName
     * @param lastName
     * @param number
     * @param emailAddress
     * @param address
     * @param city
     * @param zipcode
     * @param state
     */
    public Customer(String firstName, String lastName, String number,
                    String emailAddress, String address, String city, String zipcode, String state) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.emailAddress = emailAddress;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
    }

    // Auto-generated getters and setters
    public final LongProperty getpKey() {
        return pKey;
    }

    public final void setpKey(long pKey) {
        this.pKey.set(pKey);
    }

    public LongProperty pKeyProperty(){
        return pKey;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Returns the userID which is made based on the customer's name and phone number
    public  String getUserID() {
        return userID;
    }

    public String getName(){
        String fullname = firstName+" "+lastName;
        return fullname;
    }

    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getFirstName(), customer.getFirstName()) &&
                Objects.equals(getLastName(), customer.getLastName()) &&
                Objects.equals(getNumber(), customer.getNumber()) &&
                Objects.equals(getEmailAddress(), customer.getEmailAddress()) &&
                Objects.equals(getAddress(), customer.getAddress());
    }
}
