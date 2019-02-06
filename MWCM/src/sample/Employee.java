package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;

public class Employee {
    private String firstName;
    private String lastName;
    private String number;
    private String emailAddress;
    private String address;
    private String city;
    private String zipcode;
    private String state;
    private String userID;
    private LongProperty pKey;
    private DoubleProperty payrate;

    /**
     * Constructor for reading from the DB
     * @param pkey
     * @param firstName
     * @param lastName
     * @param number
     * @param emailAddress
     * @param address
     * @param city
     * @param zipcode
     * @param state
     * @param payrate
     */
    public Employee(long pkey, String firstName, String lastName, String number, String emailAddress, String address,
                    String city, String zipcode, String state, double payrate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.emailAddress = emailAddress;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.payrate = new SimpleDoubleProperty(payrate);
        this.pKey = new SimpleLongProperty(pkey);
        this.userID = (firstName + lastName + this.pKey).toLowerCase();
    }

    /**
     * Constructor for adding employee to the DB
     * @param firstName
     * @param lastName
     * @param number
     * @param emailAddress
     * @param address
     * @param city
     * @param zipcode
     * @param state
     * @param payrate
     */
    public Employee(String firstName, String lastName, String number, String emailAddress, String address,
                    String city, String zipcode, String state, double payrate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.emailAddress = emailAddress;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.state = state;
        this.payrate = new SimpleDoubleProperty(payrate);
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
        Employee employee = (Employee) o;
        return Objects.equals(getFirstName(), employee.getFirstName()) &&
                Objects.equals(getLastName(), employee.getLastName()) &&
                Objects.equals(getNumber(), employee.getNumber()) &&
                Objects.equals(getEmailAddress(), employee.getEmailAddress()) &&
                Objects.equals(getAddress(), employee.getAddress());
    }

    public long getpKey() {
        return pKey.get();
    }

    public LongProperty pKeyProperty() {
        return pKey;
    }

    public void setpKey(long pKey) {
        this.pKey.set(pKey);
    }

    public double getPayrate() {
        return payrate.get();
    }

    public DoubleProperty payrateProperty() {
        return payrate;
    }

    public void setPayrate(double payrate) {
        this.payrate.set(payrate);
    }
}
