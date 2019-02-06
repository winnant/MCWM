package sample;


import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;

public class CarServiceOrder {

    private LongProperty pKey;
    private LongProperty Employee_Key;
    private LongProperty Customer_Key;
    private LongProperty Car_Key;
    private LongProperty CarService_Key;
    private LongProperty Inventory_Key;
    private String Customer_ID;
    private String Car_ID;
    private String Service_ID;
    private String Part_ID;
    private double Price;
    private String Comments;
    private String date;
    private int part_quantity;

    /**
     * Constructor for adding CSOs to the DB
     * @param emp_key
     * @param cust_key
     * @param car_key
     * @param cs_key
     * @param inv_key
     * @param cust_id
     * @param car_id
     * @param service_id
     * @param part_ID
     * @param price
     * @param comments
     * @param date
     * @param part_quantity
     */
    public CarServiceOrder(long emp_key, long cust_key, long car_key, long cs_key, long inv_key, String cust_id,
                           String car_id, String service_id, String part_ID, double price, String comments,String date, int part_quantity){
        this.Employee_Key = new SimpleLongProperty(emp_key);
        this.Customer_Key = new SimpleLongProperty(cust_key);
        this.Car_Key = new SimpleLongProperty(car_key);
        this.CarService_Key = new SimpleLongProperty(cs_key);
        this.Inventory_Key = new SimpleLongProperty(inv_key);
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
        this.date = date;
        this.part_quantity = part_quantity;
    }

    /**
     * Constructor for reading from DB
     * @param pkey
     * @param emp_key
     * @param cust_key
     * @param car_key
     * @param cs_key
     * @param inv_key
     * @param cust_id
     * @param car_id
     * @param service_id
     * @param part_ID
     * @param price
     * @param comments
     */
    public CarServiceOrder(long pkey, long emp_key, long cust_key, long car_key, long cs_key, long inv_key, String cust_id,
                           String car_id, String service_id, String part_ID, double price, String comments,String date, int part_quantity){
        this.pKey = new SimpleLongProperty(pkey);
        this.Employee_Key = new SimpleLongProperty(emp_key);
        this.Customer_Key = new SimpleLongProperty(cust_key);
        this.Car_Key = new SimpleLongProperty(car_key);
        this.CarService_Key = new SimpleLongProperty(cs_key);
        this.Inventory_Key = new SimpleLongProperty(inv_key);
        this.Customer_ID = cust_id;
        this.Car_ID = car_id;
        this.Service_ID = service_id;
        this.Part_ID = part_ID;
        this.Price = price;
        this.Comments = comments;
        this.date = date;
        this.part_quantity = part_quantity;
    }

    public final long getpKey() {
        return pKey.get();
    }

    public final void setpKey(long pKey) {
        this.pKey.set(pKey);
    }

    public LongProperty pKeyProperty(){
        return pKey;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCar_ID() {
        return Car_ID;
    }

    public void setCar_ID(String car_ID) {
        Car_ID = car_ID;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }

    public String getPart_ID() {
        return Part_ID;
    }

    public void setPart_ID(String part_ID) {
        Part_ID = part_ID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public final long getEmployee_Key() {
        return Employee_Key.get();
    }

    public LongProperty employee_KeyProperty() {
        return Employee_Key;
    }

    public final void setEmployee_Key(long employee_Key) {
        this.Employee_Key.set(employee_Key);
    }

    public long getCustomer_Key() {
        return Customer_Key.get();
    }

    public LongProperty customer_KeyProperty() {
        return Customer_Key;
    }

    public void setCustomer_Key(long customer_Key) {
        this.Customer_Key.set(customer_Key);
    }

    public long getCarService_Key() {
        return CarService_Key.get();
    }

    public LongProperty carService_KeyProperty() {
        return CarService_Key;
    }

    public void setCarService_Key(long carService_Key) {
        this.CarService_Key.set(carService_Key);
    }

    public long getInventory_Key() {
        return Inventory_Key.get();
    }

    public LongProperty inventory_KeyProperty() {
        return Inventory_Key;
    }

    public void setInventory_Key(long inventory_Key) {
        this.Inventory_Key.set(inventory_Key);
    }

    public long getCar_Key() {
        return Car_Key.get();
    }

    public LongProperty car_KeyProperty() {
        return Car_Key;
    }

    public void setCar_Key(long car_Key) {
        this.Car_Key.set(car_Key);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPart_quantity() {
        return part_quantity;
    }

    public void setPart_quantity(int part_quantity) {
        this.part_quantity = part_quantity;
    }
}