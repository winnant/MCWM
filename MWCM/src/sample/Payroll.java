package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class Payroll {

    private LongProperty pKey;
    private LongProperty emp_key;
    private String employee_name;
    private int hours;
    private String startDate;
    private String endDate;
    private DoubleProperty payment;
    private DoubleProperty payrate;
    private DoubleProperty bonus;

    /**
     * Constructor for adding Payroll to database
     * @param emp_key
     * @param employee_name
     * @param hours
     * @param startDate
     * @param endDate
     * @param payment
     * @param payrate
     */
    public Payroll(long emp_key, String employee_name, int hours, String startDate, String endDate, double payment,
                   double payrate,double bonus) {
        this.emp_key = new SimpleLongProperty(emp_key);
        this.employee_name = employee_name;
        this.hours = hours;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = new SimpleDoubleProperty(payment);
        this.payrate = new SimpleDoubleProperty(payrate);
        this.bonus = new SimpleDoubleProperty(bonus);
    }

    /**
     * Constructor for reading Payroll from database
     * @param emp_key
     * @param employee_name
     * @param hours
     * @param startDate
     * @param endDate
     * @param payment
     * @param pKey
     * @param payrate
     */
    public Payroll(long emp_key, String employee_name, int hours, String startDate, String endDate, double payment,
                   long pKey, double payrate,double bonus) {
        this.emp_key = new SimpleLongProperty(emp_key);
        this.employee_name = employee_name;
        this.hours = hours;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = new SimpleDoubleProperty(payment);
        this.pKey = new SimpleLongProperty(pKey);
        this.payrate = new SimpleDoubleProperty(payrate);
        this.bonus = new SimpleDoubleProperty(bonus);
    }

    public long getpKey() {
        return pKey.get();
    }

    public LongProperty pKeyProperty() {
        return pKey;
    }

    public long getEmp_key() {
        return emp_key.get();
    }

    public LongProperty emp_keyProperty() {
        return emp_key;
    }

    public void setpKey(long pKey) {
        this.pKey.set(pKey);
    }

    public void setEmp_key(long emp_key) {
        this.emp_key.set(emp_key);
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getPayment() {
        return payment.get();
    }

    public DoubleProperty paymentProperty() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment.set(payment);
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

    public double getBonus() {
        return bonus.get();
    }

    public DoubleProperty bonusProperty() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus.set(bonus);
    }
}
