package sample;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;
import java.util.UUID;

public class Car {

    private String userID;
    private String make;
    private String model;
    private String year;
    private String comments = "";
    private LongProperty pKey;
    private LongProperty customer_key;

    /**
     * Constructor for adding Cars to the DB
     * @param userID
     * @param make
     * @param model
     * @param year
     * @param comments
     */
    public Car(long cust_key, String userID, String make, String model, String year, String comments) {
        this.userID = userID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.comments = comments;
        this.customer_key = new SimpleLongProperty(cust_key);
    }

    /**
     * Custructor for reading from the DB
     * @param userID
     * @param make
     * @param model
     * @param year
     * @param comments
     * @param cust_key
     */
    public Car(long pKey, long cust_key, String userID, String make, String model, String year, String comments) {
        this.userID = userID;
        this.make = make;
        this.model = model;
        this.year = year;
        this.comments = comments;
        this.pKey = new SimpleLongProperty(pKey);
        this.customer_key = new SimpleLongProperty(cust_key);
    }

    // Auto-generated getters and setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return make + " " + model + " " + year;
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

    public long getCustomer_key() {
        return customer_key.get();
    }

    public LongProperty customer_keyProperty() {
        return customer_key;
    }

    public void setCustomer_key(long customer_key) {
        this.customer_key.set(customer_key);
    }
}
