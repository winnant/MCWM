package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.Objects;

public class CarService {

    private String service_ID;
    private String service_Name;
    private DoubleProperty price;
    private LongProperty pKey;

    /**
     * Constructor for reading from the DB
     * @param pkey
     * @param serviceID
     * @param serviceName
     * @param price
     */
    public CarService(long pkey, String serviceID, String serviceName, double price){
        this.service_ID = serviceID;
        this.service_Name = serviceName;
        this.price = new SimpleDoubleProperty(price);
        this.pKey = new SimpleLongProperty(pkey);
    }

    /**
     * Constructor for adding CarService to the DB
     * @param serviceID
     * @param serviceName
     * @param price
     */
    public CarService(String serviceID, String serviceName, double price){
        this.service_ID = serviceID;
        this.service_Name = serviceName;
        this.price = new SimpleDoubleProperty(price);
    }

    public String getService_ID() {
        return service_ID;
    }

    public void setService_ID(String service_ID) {
        this.service_ID = service_ID;
    }

    public String getService_Name() {
        return service_Name;
    }

    public void setService_Name(String service_Name) {
        this.service_Name = service_Name;
    }



    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return service_Name + " \n$" + price.getValue();
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        CarService carService = (CarService) o;
//        return Objects.equals(getService_ID(), carService.getService_ID()) &&
//                Objects.equals(getService_Name(), carService.getService_Name()) &&
//                Objects.equals(getPrice(), carService.getPrice());
//    }
}
