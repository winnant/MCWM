package sample;

import database.DBConnector;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ReportController implements Initializable {

    @FXML
    private DatePicker start_date;
    @FXML
    private DatePicker end_date;
    @FXML
    private TableView<Report> tableView;
    @FXML
    private TableColumn<Report,String> col_start;
    @FXML
    private TableColumn<Report,String> col_end;
    @FXML
    private TableColumn<Report,Double> col_costs;
    @FXML
    private TableColumn<Report,Double> col_service_profit;
    @FXML
    private TableColumn<Report,Double> col_taxes;
    @FXML
    private TableColumn<Report,Double> col_profit;
    @FXML
    private TableColumn<Report,Double> col_net_profit;
    @FXML
    private TextField text_search;
    @FXML
    private TextArea text_inventory_report;


    // Tracks Car Service Orders
    private ObservableList<CarServiceOrder> carServiceOrders = FXCollections.observableArrayList();
    // Tracks reports
    private ObservableList<Report> reports = FXCollections.observableArrayList();
    // Tracks Car Services
    private ObservableList<CarService> carServices = FXCollections.observableArrayList();
    // Tracks Inventory
    private ObservableList<AutoPart> parts = FXCollections.observableArrayList();

    /*
   Initializes the payroll database and populates the table and dropdown menu for employee selection on startup
   precondition: can only be accessed from a button on the Employee page gui
   postcondition: both the employee dropdown and payroll table are populated with the respective objects from the database
*/
    public void initialize(URL location, ResourceBundle resources) {
        // Establish observable lists
        carServiceOrders.clear();
        reports.clear();
        carServices.clear();
        parts.clear();
        text_inventory_report.setEditable(false);

        // Table View
        col_start.setCellValueFactory(new PropertyValueFactory<Report, String>("startDate"));
        col_end.setCellValueFactory(new PropertyValueFactory<Report, String>("endDate"));
        col_costs.setCellValueFactory(new PropertyValueFactory<Report, Double>("goodsCost"));
        col_service_profit.setCellValueFactory(new PropertyValueFactory<Report, Double>("serviceProfit"));
        col_taxes.setCellValueFactory(new PropertyValueFactory<Report, Double>("taxes"));
        col_profit.setCellValueFactory(new PropertyValueFactory<Report, Double>("profit"));
        col_net_profit.setCellValueFactory(new PropertyValueFactory<Report, Double>("netProfit"));

        tableView.setItems(getReports());
        getCarServiceOrderList();
        getCarServiceList();
        getInventory();


    }


    /*
       Reads report objects from the database and adds them to the observable list of payroll
       precondition: a proper list of report objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of Report objects
    */
    private ObservableList<Report> getReports() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Report");

            while (rs.next()) {
                Report newReport = new Report(
                        rs.getLong("Key"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getDouble("goodsCost"),
                        rs.getDouble("serviceProfit"),
                        rs.getDouble("taxes"),
                        rs.getDouble("profit"),
                        rs.getDouble("netProfit"),
                        rs.getString("inventoryReport"));
                reports.add(newReport);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return reports;
    }

    /*
   Reads CarServiceOrder objects from the database and adds them to the observable list of CarServiceOrder
   precondition: a proper list of CarServiceOrder objects must be declared and method must be called through
   initialize
   postcondition: returns an ObservableList of CarServiceOrder objects
*/
    private ObservableList<CarServiceOrder> getCarServiceOrderList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarServiceOrder");

            while (rs.next()) {
                CarServiceOrder newOrder = new CarServiceOrder(
                        rs.getLong("Key"),
                        rs.getLong("Employee_Key"),
                        rs.getLong("Customer_Key"),
                        rs.getLong("Car_Key"),
                        rs.getLong("CarService_Key"),
                        rs.getLong("Inventory_Key"),
                        rs.getString("Customer_ID"),
                        rs.getString("Car_ID"),
                        rs.getString("Service_ID"),
                        rs.getString("Part_ID"),
                        rs.getDouble("Price"),
                        rs.getString("Comments"),
                        rs.getString("date"),
                        rs.getInt("part_quantity"));
                carServiceOrders.add(newOrder);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return carServiceOrders;
    }

    /*
   Reads CarService objects from the database and adds them to the observable list of CarService
   precondition: a proper list of CarService objects must be declared and method must be called through
   initialize
   postcondition: returns an ObservableList of CarService objects
    */
    private ObservableList<CarService> getCarServiceList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarService");

            while (rs.next()) {
                CarService newService = new CarService(
                        rs.getLong("Key"),
                        rs.getString("Service_ID"),
                        rs.getString("Service_Name"),
                        rs.getDouble("Price"));
                carServices.add(newService);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return carServices;
    }

    /**
     * Read Autoparts from the DB.
     * @return
     */
    public ObservableList<AutoPart> getInventory(){
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Inventory");

            while (rs.next()) {
                AutoPart autoPart = new AutoPart(
                        rs.getLong("Key"),
                        rs.getLong("CarService_Key"),
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getLong("quantity"),
                        rs.getDouble("price"),
                        rs.getString("Category"));
                parts.add(autoPart);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return parts;
    }

    /**
     *
     */
    public void generateCurrentInventoryReport(){
        String report = getInventoryReport();
        System.out.println(report);
        text_inventory_report.setStyle("-fx-font-family: monospace");
        text_inventory_report.setText(report);
    }

    /**
     * Generate a report based on the selected dates
     * @param actionEvent
     */
    public void generateReport(ActionEvent actionEvent) {
        String startdate = start_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String enddate = end_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String inventoryReport = getInventoryReport(startdate,enddate);
        double goodsCost = 0.0;
        double serviceProfit = 0.0;
        double taxes = 0.0;
        ObservableList<CarServiceOrder> tempList = getCSO(startdate, enddate);
        if (tempList.size() > 0) {
            //System.out.println(tempList.size());
            for (int i = 0; i < tempList.size(); i++) {
                CarServiceOrder selectedCSO = tempList.get(i);
                long cs_key = selectedCSO.getCarService_Key();
                long inventory_key = selectedCSO.getInventory_Key();
                if (inventory_key > 0) {
                    for (int j = 0; j < parts.size(); j++) {
                        // if the service order has inventory associated with it
                        // calculate the goodsCost by taking price of the part * quantity
                        // add the price of the service to service profit
                        goodsCost = goodsCost + (parts.get(j).getPrice() * selectedCSO.getPart_quantity());
                    }
                    for (int k = 0; k < carServices.size(); k++) {
                        if (cs_key == carServices.get(k).getpKey()) {
                            System.out.println(" inventory service profit = " + carServices.get(k).getPrice());
                            serviceProfit = serviceProfit + carServices.get(k).getPrice();
                        }
                    }
                } else {
                    // if the service order has no inventory associated with it
                    // add the price of the service to service profit
                    for (int j = 0; j < carServices.size(); j++) {
                        if (cs_key == carServices.get(j).getpKey() && inventory_key == 0) {
                            System.out.println("service profit = " + carServices.get(j).getPrice());
                            serviceProfit = serviceProfit + carServices.get(j).getPrice();
                        }
                    }
                }
                taxes =  taxes + ((goodsCost + serviceProfit) * 0.07);
            }
                double profit = serviceProfit + goodsCost;
                double net_profit = profit - taxes;


                DecimalFormat nf = new DecimalFormat("#.00");
                double nfgoodsCost = Double.valueOf(nf.format(goodsCost));
                double nfserviceProfit = Double.valueOf(nf.format(serviceProfit));
                double nftaxes = Double.valueOf(nf.format(taxes));
                double nfprofit = Double.valueOf(nf.format(profit));
                double nf_net_profit = Double.valueOf(nf.format(net_profit));

               // System.out.println("goodsCost = " + goodsCost);
              //  System.out.println("serviceProfit = " + serviceProfit);
              //  System.out.println("taxes = " + taxes);
              //  System.out.println("profit = " + profit);
              //  System.out.println("net_proft = " + net_profit);
            String sql = "INSERT INTO Report(startDate,endDate,goodsCost,serviceProfit,taxes,profit,netProfit,inventoryReport)  VALUES(?,?,?,?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, startdate);
                preparedStatement.setString(2, enddate);
                preparedStatement.setDouble(3, nfgoodsCost);
                preparedStatement.setDouble(4, nfserviceProfit);
                preparedStatement.setDouble(5, nftaxes);
                preparedStatement.setDouble(6, nfprofit);
                preparedStatement.setDouble(7, nf_net_profit);
                preparedStatement.setString(8, inventoryReport);
                preparedStatement.execute();
                System.out.println("Report generated successfully");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
         }
         refreshTable();

    }

    /*
        Retuns an ObservableList of Car Service Orders based on the start date and end date passed from generateReport()
        precondition: must be called by generateReport(), startdate and enddate are not null and are valid dates selected by the user
        postcondition: a list of Car Service Orders is returned
     */
    public ObservableList<CarServiceOrder> getCSO (String startdate,String enddate){
        ObservableList<CarServiceOrder> tempList = FXCollections.observableArrayList();

        DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate start_date = LocalDate.parse(startdate,df);
        LocalDate end_date = LocalDate.parse(enddate,df);

        for (int i = 0; i< carServiceOrders.size(); i++){
            LocalDate csoDate = LocalDate.parse(carServiceOrders.get(i).getDate(),df);
            if(csoDate.equals(start_date) || csoDate.equals(end_date)){
                tempList.add(carServiceOrders.get(i));
                //System.out.println("equals");
            }else if(csoDate.isAfter(start_date) && csoDate.isBefore(end_date)){
                tempList.add(carServiceOrders.get(i));
                //System.out.println("Within range");
            }
        }
        return tempList;
    }


    /*
   Refreshes the table by rereading from the database when called
   precondition: method has to be called by other methods like generateReport()
   postcondition: the table on the GUI is refreshed with the information about Reports read from the database
*/
    public void refreshTable() {
        try {
            reports.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Report");
            while (rs.next()) {
                Report newReport = new Report(
                        rs.getLong("Key"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getDouble("goodsCost"),
                        rs.getDouble("serviceProfit"),
                        rs.getDouble("taxes"),
                        rs.getDouble("profit"),
                        rs.getDouble("netProfit"),
                        rs.getString("inventoryReport"));
                reports.add(newReport);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Get the inventory parts used by the CarServiceOrders within the Start and End Dates
     * @param startdate
     * @param enddate
     * @return
     */
    public String getInventoryReport(String startdate,String enddate){
        startdate = start_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        enddate = end_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String header1 = "Inventory Part Name";
        String header2 = "Quantity Used";
        String divider = "------------------------------------------------------\n";
        String partName = String.format("%-40s|%-10s %n",header1,header2);
        partName = partName + divider;
        ObservableList<CarServiceOrder> tempList = getCSO(startdate, enddate);
        for (int i = 0; i < tempList.size(); i++) {
            CarServiceOrder selectedCSO = tempList.get(i);
            long inventory_key = selectedCSO.getInventory_Key();
            if (inventory_key > 0) {
                for (int j = 0; j<parts.size(); j++){
                    if (inventory_key == parts.get(j).getpKey()){
                         String temp = String.format("%-40s|%10s %n",  parts.get(j).getName(),tempList.get(i).getPart_quantity());
                         partName = partName + temp;
                    }
                }
            }
        } System.out.println(partName);
        return partName;
    }

    /**
     * Used by the InventoryReport_Button to generate the current inventory based on the database.
     * @return String value formatted to represent all current stock of Autoparts in the database.
     */
    public String getInventoryReport(){
        //startdate = start_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        //enddate = end_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String header1 = "Inventory Part Name";
        String header2 = "Quantity Used";
        String divider = "------------------------------------------------------\n";
        String partName = String.format("%-40s|%-10s %n",header1,header2);
        partName = partName + divider;
        for (int i = 0; i < parts.size(); i++){
            String temp = String.format("%-40s|%10s %n",  parts.get(i).getName(), parts.get(i).getQuantity());
            partName = partName + temp;
        }

        System.out.println(partName);
        return partName;
    }
    // comment to make a a change to merge to master
    /*
    Filters the Employee table based on what string input the user types into the bar (a search function)
    precondition: there must be objects for the search to filter through and the user must enter a proper string
    postcondition: the table will list only the employees that have strings matching what the user entered in the search bar
    */
    FilteredList filter = new FilteredList(reports, e->true);
    public void searchPart(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Report>) report -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (report.getStartDate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (report.getEndDate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Double.toString(report.getGoodsCost()).contains(lowerCaseFilter)) {
                        return true;
                    } else if (Double.toString(report.getServiceProfit()).contains(lowerCaseFilter)) {
                        return true;
                    } else if (Double.toString(report.getTaxes()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (Double.toString(report.getProfit()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (Double.toString(report.getNetProfit()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<Report> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }

    public void clickedReport (MouseEvent actionEvent){
        if (tableView.getSelectionModel().isEmpty()){

        }else {
            String inventoryReport = tableView.getSelectionModel().getSelectedItem().getInventoryReport();
            System.out.println(inventoryReport);
            text_inventory_report.setStyle("-fx-font-family: monospace");
            text_inventory_report.setText(inventoryReport);
        }
    }

}
