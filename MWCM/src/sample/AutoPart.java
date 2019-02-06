package sample;

import javafx.beans.property.*;

public class AutoPart {

    private String id;
    private String name;
    private LongProperty quantity;
    private DoubleProperty price;
    private LongProperty carService_Key;
    private LongProperty pKey;
    private String category;

    /**
     * Constructor for reading from the DB
     * @param pkey
     * @param carservicekey
     * @param id
     * @param name
     * @param quantity
     * @param price
     */
    public AutoPart(long pkey, long carservicekey, String id, String name, long quantity, double price, String category){
        this.id = id;
        this.name = name;
        this.pKey = new SimpleLongProperty(pkey);
        this.carService_Key = new SimpleLongProperty(carservicekey);
        this.quantity = new SimpleLongProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.category = category;
    }

    /**
     * Constructor for adding parts to the DB
     * @param carservicekey
     * @param id
     * @param name
     * @param quantity
     * @param price
     */
    public AutoPart(long carservicekey, String id, String name, long quantity, double price, String category){
        this.id = id;
        this.name = name;
        this.carService_Key = new SimpleLongProperty(carservicekey);
        this.quantity = new SimpleLongProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    // Overridden toString method used for displaying the customer's name in a dropdown menu
    public String toString() {
        return name + "\n$" + price.getValue();
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

    public long getQuantity() {
        return quantity.get();
    }

    public LongProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
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

    public long getCarService_Key() {
        return carService_Key.get();
    }

    public LongProperty carService_KeyProperty() {
        return carService_Key;
    }

    public void setCarService_Key(long carService_Key) {
        this.carService_Key.set(carService_Key);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
