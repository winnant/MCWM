package sample;

import database.DBConnector;
import javafx.beans.property.LongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ServiceController implements Initializable {

    @FXML
    private TableView<CarService> tableView;
    @FXML
    private TableColumn<CarService,String> col_id;
    @FXML
    private TableColumn<CarService,String> col_name;
    @FXML
    private TableColumn<CarService,Double> col_price;
    @FXML
    private TextField text_category;
    @FXML
    private TextField text_name;
    @FXML
    private TextField text_price;
    @FXML
    private TextField text_search;

    // Tracks Car Services
    private ObservableList<CarService> carServices = FXCollections.observableArrayList();

    @Override
    /*
       Initializes the car database and populates the table and dropdown menu for customer selection on startup
       precondition: can only be accessed from a button on the Customer page gui
       postcondition: both the customer dropdown and car table are populated with the respective objects from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        // Establish observable lists
        carServices.clear();

        // Table View
        col_id.setCellValueFactory(new PropertyValueFactory<CarService, String>("Service_ID"));
        col_name.setCellValueFactory(new PropertyValueFactory<CarService, String>("Service_Name"));
        col_price.setCellValueFactory(new PropertyValueFactory<CarService, Double>("Price"));
        tableView.setItems(getCarServiceList());
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

    /*
    Adds CarService to the database and GUI
     */
    public void addCS(ActionEvent actionEvent){
        if (text_category.getLength()>0 && text_name.getLength()>0 && text_price.getLength()>0){
            String Service_ID = text_category.getText();
            String Service_Name = text_name.getText();
            Double price = Double.valueOf(text_price.getText());
            String sql = "INSERT INTO CarService(Service_ID,Service_Name,Price)  VALUES(?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, Service_ID);
                preparedStatement.setString(2, Service_Name);
                preparedStatement.setDouble(3, price);
                preparedStatement.execute();
                //addedPopup();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            refreshTable();
            }
        }
    /*
    Updates the customer the user currently has selected on the table with the new information in the text fields
    precondition: a customer on the table has been selected and the user entered information in the proper format in the text fields
    postcondition: the selected customer is updated in both table and database
*/
    public void updatedCS(ActionEvent actionEvent) {
        CarService clickedCS = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        String Service_ID = text_category.getText();
        String Service_Name = text_name.getText();
        Double price = Double.valueOf(text_price.getText());
        long pKey  = clickedCS.getpKey();


        if (Service_ID.length() == 0 || Service_Name.length() == 0 || text_price.getLength()<0) {
            System.out.println("One or more fields encountered and error");
        } else {
            CarService newCS = new CarService(pKey,Service_ID,Service_Name,price);
            String sql = "UPDATE CarService SET Service_ID=?, Service_Name=?, Price=? where Key = ?";
            System.out.println(sql);
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, Service_ID);
                preparedStatement.setString(2, Service_Name);
                preparedStatement.setDouble(3, price);
                preparedStatement.setLong(4, pKey);
                preparedStatement.execute();
                System.out.println("Update Successful");
                carServices.set(index, newCS);
                refreshTable();
                //updatePopup();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /*
       Deletes the service the user has currently selected in both the database and the table
       precondition: proper service has been stored and user double clicks on the service
       postcondition: the selected service object is deleted from both the table and the database
    */
    public void deletedCS() {
        CarService clickedCS = tableView.getSelectionModel().getSelectedItem();
        //String sql = "DELETE FROM Customer where email = '" + clickedCustomer.getEmailAddress() + "'";
        String sql = "DELETE FROM CarService where Key = ?";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            long key = clickedCS.getpKey();
            preparedStatement.setLong(1, key);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_category.clear();
            text_name.clear();
            text_price.clear();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
   Refreshes the table by rereading from the database when called
   precondition: method has to be called by other methods like addCarService(), updateCarService(), or deleteCarService()
   postcondition: the table on the GUI is refreshed with the information about car services read from the database
*/
    public void refreshTable() {
        try {
            carServices.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarService");
            while (rs.next()) {
                CarService newCarService = new CarService(
                        rs.getLong("Key"),
                        rs.getString("Service_ID"),
                        rs.getString("Service_Name"),
                        rs.getDouble("Price"));
                carServices.add(newCarService);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    /*
   Sets the information of the car service to the text boxes on the left menu when the user double clicks a customer in the list
   precondition: proper car service has been stored and user double clicks on the customer
   postcondition: text fields on the left of the main window have been populated with the car service's information
    */
    public void clickedCS(MouseEvent actionEvent) {
        if (actionEvent.getClickCount() == 1 && !tableView.getSelectionModel().isEmpty()) {
            CarService clickedCS = tableView.getSelectionModel().getSelectedItem();

            text_category.setText(clickedCS.getService_ID());
            text_name.setText(clickedCS.getService_Name());
            text_price.setText(String.valueOf(clickedCS.getPrice()));

        }
    }

    /*
   Filters the CarService table based on what string input the user types into the bar (a search function)
   precondition: there must be objects for the search to filter through and the user must enter a proper string
   postcondition: the table will list only the customers that have strings matching what the user entered in the search bar
*/
    FilteredList filter = new FilteredList(carServices, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super CarService>) carService -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (carService.getService_ID().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (carService.getService_Name().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(carService.getPrice()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<CarService> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }
}
