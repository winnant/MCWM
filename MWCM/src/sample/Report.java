package sample;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;

public class Report {

    private LongProperty pKey;
    private String startDate;
    private String endDate;
    private DoubleProperty goodsCost;
    private DoubleProperty serviceProfit;
    private DoubleProperty taxes;
    private DoubleProperty profit;
    private DoubleProperty netProfit;
    private String inventoryReport;

    /*
        Constructor for adding Report to database
     */
    public Report(String startDate, String endDate, double goodsCost, double serviceProfit, double taxes, double profit, double netProfit, String inventoryReport) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.goodsCost = new SimpleDoubleProperty(goodsCost);
        this.serviceProfit = new SimpleDoubleProperty(serviceProfit);
        this.taxes = new SimpleDoubleProperty(taxes);
        this.profit = new SimpleDoubleProperty(profit);
        this.netProfit = new SimpleDoubleProperty(netProfit);
        this.inventoryReport = inventoryReport;
    }

    /*
        Constructor for reading Report from database
     */
    public Report(long pKey, String startDate, String endDate, double goodsCost, double serviceProfit, double taxes, double profit, double netProfit, String inventoryReport) {
        this.pKey = new SimpleLongProperty(pKey);
        this.startDate = startDate;
        this.endDate = endDate;
        this.goodsCost = new SimpleDoubleProperty(goodsCost);
        this.serviceProfit = new SimpleDoubleProperty(serviceProfit);
        this.taxes = new SimpleDoubleProperty(taxes);
        this.profit = new SimpleDoubleProperty(profit);
        this.netProfit = new SimpleDoubleProperty(netProfit);
        this.inventoryReport = inventoryReport;
    }

    public long getpKey() {
        return pKey.get();
    }

    public LongProperty pKeyProperty() {
        return pKey;
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

    public double getGoodsCost() {
        return goodsCost.get();
    }

    public DoubleProperty goodsCostProperty() {
        return goodsCost;
    }

    public void setGoodsCost(double goodsCost) {
        this.goodsCost.set(goodsCost);
    }

    public double getServiceProfit() {
        return serviceProfit.get();
    }

    public DoubleProperty serviceProfitProperty() {
        return serviceProfit;
    }

    public void setServiceProfit(double serviceProfit) {
        this.serviceProfit.set(serviceProfit);
    }

    public double getTaxes() {
        return taxes.get();
    }

    public DoubleProperty taxesProperty() {
        return taxes;
    }

    public void setTaxes(double taxes) {
        this.taxes.set(taxes);
    }

    public double getProfit() {
        return profit.get();
    }

    public DoubleProperty profitProperty() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit.set(profit);
    }

    public double getNetProfit() {
        return netProfit.get();
    }

    public DoubleProperty netProfitProperty() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit.set(netProfit);
    }

    public String getInventoryReport() {
        return inventoryReport;
    }

    public void setInventoryReport(String inventoryReport) {
        this.inventoryReport = inventoryReport;
    }
}
