package sample;

import database.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.Text;

import javax.swing.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CarController implements Initializable {

    @FXML
    private TableView<Car> carTableView;
    @FXML
    private TableColumn<Car, String> col_userId;
    @FXML
    private TableColumn<Car, String> col_make;
    @FXML
    private TableColumn<Car, String> col_model;
    @FXML
    private TableColumn<Car, String> col_year;
    @FXML
    private TableColumn<Car, String> col_comments;

    @FXML
    private ComboBox<Customer> customerList;
    @FXML
    private TextField text_make;
    @FXML
    private TextField text_model;
    @FXML
    private TextField text_year;
    @FXML
    private TextField text_comments;
    @FXML
    private TextField text_search;

    // Keeps track of all the cars in the database
    private ObservableList<Car> cars = FXCollections.observableArrayList();
    // Keeps track of all the customers in the database
    private ObservableList<Customer> customers = FXCollections.observableArrayList();
   // private ObservableList<String> custNames = FXCollections.observableArrayList();

    @Override
    /*
       Initializes the car database and populates the table and dropdown menu for customer selection on startup
       precondition: can only be accessed from a button on the Customer page gui
       postcondition: both the customer dropdown and car table are populated with the respective objects from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        customerList.setItems(getCustomerList());
        col_userId.setCellValueFactory(new PropertyValueFactory<Car,String>("userID"));
        col_make.setCellValueFactory(new PropertyValueFactory<Car,String>("make"));
        col_model.setCellValueFactory(new PropertyValueFactory<Car,String>("model"));
        col_year.setCellValueFactory(new PropertyValueFactory<Car,String>("year"));
        col_comments.setCellValueFactory(new PropertyValueFactory<Car,String>("comments"));
        carTableView.setItems(getCarList());
    }

    /*
       Reads customer objects from the database and adds them to the observable list of customers
       precondition: a proper list of customer objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of Customer objects
    */
    private ObservableList<Customer> getCustomerList() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Customer");

            while (rs.next()) {
                Customer newCustomer = new Customer(
                        rs.getLong("Key"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zipcode"),
                        rs.getString("state"));
                String customerNames =  newCustomer.getName();
                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return customers;
    }

    /*
       Reads car objects from the database and adds them to the observable list of car objects
       precondition: a proper list of car objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of car objects
    */
    private ObservableList<Car> getCarList(){
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Car");

            while (rs.next()){
                Car newCar = new Car(
                        rs.getLong("Key"),
                        rs.getLong("Customer_Key"),
                        rs.getString("userID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("comments"));
                System.out.println(newCar.getUserID());
                cars.add(newCar);

            }
        }catch (Exception e){
            System.err.println(e);
        }
        return cars;
    }

    /*
       Creates car object and adds it to database and refreshes the table with the new car added
       precondition: user clicks add button and has entered the information in the proper formats in the proper fields, no fields are empty
       postcondition: new car is added to both the database and observable list which is used to display cars
    */
    public void addCar(ActionEvent actionEvent){
        String make = text_make.getText();
        String model = text_model.getText();
        String year = text_year.getText();
        String comments = text_comments.getText();

        if(make.length() == 0 || model.length() == 0 || year.length() != 4){
            System.out.println("Invalid field");
        }else{
            Customer selectedCustomer = customerList.getSelectionModel().getSelectedItem();
            String customerUserID = selectedCustomer.getUserID();
            long cust_key = selectedCustomer.getpKey().getValue();
            //Car newCar = new Car(customerUserID,make,model,year,comments);
            //cars.add(newCar);
            String sql = "INSERT INTO Car(userID,make,model,year,comments,Customer_Key) VALUES(?,?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, customerUserID);
                preparedStatement.setString(2, make);
                preparedStatement.setString(3, model);
                preparedStatement.setString(4, year);
                preparedStatement.setString(5, comments);
                preparedStatement.setLong(6, cust_key);
                preparedStatement.execute();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            refreshTable();
        }
    }

    /*
       Deletes the car the user has currently selected in both the database and the table
       precondition: a proper car has been stored in the database and  the user has clicked on the desired car
       postcondition: the selected car object is deleted from both the table and the database
    */
    public void deleteCar(ActionEvent actionEvent){
        Car clickedCar = carTableView.getSelectionModel().getSelectedItem();
        //String sql = "DELETE FROM Car where userID = '" + clickedCar.getUserID() + "'";
        String sql = "DELETE FROM Car where Key = ?";
        long pkey = clickedCar.getpKey();
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, pkey);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_make.clear();
            text_model.clear();
            text_year.clear();
            text_comments.clear();
            customerList.valueProperty().set(null);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
       Refreshes the table by rereading from the database when called
       precondition: method has to be called by other methods like deletedCar(), addCar(), or updateCar()
       postcondition: the table on the GUI is refreshed with the information about car read from the database
    */
    public void refreshTable(){
        try{
            cars.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Car");

            while (rs.next()){
                Car newCar = new Car(
                        rs.getLong("Key"),
                        rs.getLong("Customer_Key"),
                        rs.getString("userID"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getString("year"),
                        rs.getString("comments"));
                System.out.println(newCar.getUserID());
                cars.add(newCar);
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }

    /*
       Sets the information of the car to the text boxes on the left menu when the user clicks a car in the list
       precondition: proper car has been stored and user double clicks on the car
       postcondition: text fields on the left of the main window have been populated with the car's information
    */
    public void clickCar(MouseEvent actionEvent){
        int index;
        if(actionEvent.getClickCount() == 1){
            if (carTableView.getSelectionModel().isEmpty()){

            }else {
                Car clickedCar = carTableView.getSelectionModel().getSelectedItem();
                text_make.setText(clickedCar.getMake());
                text_model.setText(clickedCar.getModel());
                text_year.setText(clickedCar.getYear());
                text_comments.setText(clickedCar.getComments());

                for (int i = 0; i < customers.size(); i++) {
                    Customer myCustomer = customers.get(i);
                    if (clickedCar.getUserID().equals(myCustomer.getUserID())) {
                        //System.out.println(clickedCar.getUserID() + " Customer userID: "+ myCustomer.getUserID());
                        index = i;
                        customerList.getSelectionModel().select(customers.get(index));
                    }
                }
            }
        }
    }

    /*
       Updates the car the user currently has selected on the table with the new information in the text fields
       precondition: a car on the table has been selected and the user entered information in the proper format in the text fields
       postcondition: the selected car is updated on both table and database
    */
    public void updateCar(ActionEvent actionEvent){
        Car clickedCar = carTableView.getSelectionModel().getSelectedItem();
        Customer selectedCustomer = customerList.getSelectionModel().getSelectedItem();
        int index = carTableView.getSelectionModel().getSelectedIndex();
        System.out.println("index: " +index);

        String customerID = selectedCustomer.getUserID();
        long cust_key = selectedCustomer.getpKey().getValue();
        System.out.println("Customer ID: " + customerID);

        String make = text_make.getText();
        String model = text_model.getText();
        String year = text_year.getText();
        String comments = text_comments.getText();
        if (make.length() == 0 || model.length() == 0 || year.length() != 4){
            System.out.println("Invalid field");
        }else{

            String sql = "UPDATE Car SET userID=?, make=?, model=?, year=?, comments=?, Customer_Key=? where Key=?";
            System.out.println(sql);
            try{
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, customerID);
                preparedStatement.setString(2, make);
                preparedStatement.setString(3, model);
                preparedStatement.setString(4, year);
                preparedStatement.setString(5, comments);
                preparedStatement.setLong(6, cust_key);
                preparedStatement.setLong(7, clickedCar.getpKey());
                preparedStatement.execute();
                System.out.println("Update Successful");
                cars.set(index, clickedCar);
                refreshTable();
            }catch (SQLException e){
                System.err.println(e.getMessage());
            }
        }
    }

    /*
       Filters the Customer table based on what string input the user types into the bar (a search function)
       precondition: there must be objects for the search to filter through and the user must enter a proper string
       postcondition: the table will list only the car that have strings matching what the user entered in the search bar
    */
    /*FilteredList filter = new FilteredList(cars, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Car>) car -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (car.getMake().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getModel().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getYear().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getUserID().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getComments().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<Car> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(carTableView.comparatorProperty());
            carTableView.setItems(sortedDatabase);
        }));
    }*/

    FilteredList filter = new FilteredList(cars, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Car>) car -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (car.getModel().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getMake().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getUserID().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (car.getYear().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } //else if (car.getComments().toLowerCase().contains(lowerCaseFilter)) {
                        //return true;
                    //}
                    return false;
                }
            });
            SortedList<Car> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(carTableView.comparatorProperty());
            carTableView.setItems(sortedDatabase);
        }));
    }

}