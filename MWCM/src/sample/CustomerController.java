package sample;
import database.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class CustomerController implements Initializable {

    @FXML
    private TableView<Customer> tableView;
    @FXML
    private TableColumn<Customer, String> col_first;
    @FXML
    private TableColumn<Customer, String> col_last;
    @FXML
    private TableColumn<Customer, String> col_number;
    @FXML
    private TableColumn<Customer, String> col_email;
    @FXML
    private TableColumn<Customer, String> col_city;
    @FXML
    private TableColumn<Customer, String> col_zip;
    @FXML
    private TableColumn<Customer, String> col_state;

    @FXML
    private TextField text_first;
    @FXML
    private TextField text_last;
    @FXML
    private TextField text_number;
    @FXML
    private TextField text_email;
    @FXML
    private TextField text_address;
    @FXML
    private TextField text_city;
    @FXML
    private TextField text_zipcode;
    @FXML
    private ComboBox<String> states_cb;
    @FXML
    private TextField text_search;
    @FXML
    private Button cars_button;
    @FXML
    private Button employee_button;
    @FXML
    private Button inventory_button;
    @FXML
    private Button CarServiceOrder_btn;
    @FXML
    private Button report_button;

    static ObservableList<Customer> customers = FXCollections.observableArrayList();
    private ObservableList<String> state_list = FXCollections.observableArrayList();
    private String[] states =  { "AK","AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID","IL",
            "IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH",
            "OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY"};


    @Override
    /* Populating customer table on program startup
       precondition: program must be run
       postcondition: tableView is populated with customers from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        col_first.setCellValueFactory(new PropertyValueFactory<Customer, String>("firstName"));
        col_last.setCellValueFactory(new PropertyValueFactory<Customer, String>("lastName"));
        col_number.setCellValueFactory(new PropertyValueFactory<Customer, String>("number"));
        col_email.setCellValueFactory(new PropertyValueFactory<Customer, String>("emailAddress"));
        col_city.setCellValueFactory(new PropertyValueFactory<Customer, String>("city"));
        col_zip.setCellValueFactory(new PropertyValueFactory<Customer, String>("zipcode"));
        col_state.setCellValueFactory(new PropertyValueFactory<Customer, String>("state"));

        tableView.setItems(getCustomer());
        states_cb.setItems(getStates());

        // COMMENT OUT THE IMAGE READING LINES OUT IF NOT EXPORTING TO A JAR
        /*Image imageCar = new Image(getClass().getResourceAsStream("/sports-car.png"));
        cars_button.setGraphic(new ImageView(imageCar));

        Image imageEmployee = new Image(getClass().getResourceAsStream("/employee.png"));
        employee_button.setGraphic(new ImageView(imageEmployee));

        Image imageInventory = new Image(getClass().getResourceAsStream("/inventory.png"));
        inventory_button.setGraphic(new ImageView(imageInventory));

        Image carService = new Image(getClass().getResourceAsStream("/carservices.png"));
        CarServiceOrder_btn.setGraphic(new ImageView(carService));

        Image reports = new Image(getClass().getResourceAsStream("/reports.png"));
        report_button.setGraphic(new ImageView(reports));*/
    }
    /*
        Connecting to database and reading Customer objects to populate ObservableList
        precondition: called by intialize
        postcondition:
     */
    private ObservableList<Customer> getCustomer() {
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
                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return customers;
    }
    /*
       Reads the states string array to an observable list to display in the states combobox
       precondition: called by initialize
       postcondition: states_list is populated with states
    */
    private ObservableList<String> getStates(){
        for (int i = 0; i<states.length; i++){
            state_list.add(states[i]);
        }
        return state_list;
    }
    /*
       Creates customer object and adds it to database and refreshes the table with the new customer added
       precondition: user clicks add button and has entered the information in the proper formats in the proper fields
       postcondition: new customer is added to both the database and observable list which is used to display customers
    */
    public void addCustomer(ActionEvent actionEvent) {
        String firstName = text_first.getText();
        String lastName = text_last.getText();
        String number = text_number.getText();
        String email = text_email.getText();
        String address = text_address.getText();
        String city = text_city.getText();
        String zipcode = text_zipcode.getText();
        String state = states_cb.getSelectionModel().getSelectedItem();
        if(email.length() == 0 ){
            email = "";
        }
        if (firstName.length() == 0 || lastName.length() == 0 || number.length() == 0 ||
                address.length() == 0 || city.length() == 0 || zipcode.length() < 5 || state.length() == 0) {
            if (number.length() < 10) {
                System.out.println("Invalid number");
            }

            System.out.println("One or more fields encountered and error");
        } else {
            String sql = "INSERT INTO Customer(firstName,lastName,number,email,address,city,zipcode,state)  VALUES(?,?,?,?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, number);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, city);
                preparedStatement.setString(7, zipcode);
                preparedStatement.setString(8, state);
                preparedStatement.execute();
                addedPopup();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            refreshTable();
        }
    }
    /*
       Sets the information of the customer to the text boxes on the left menu when the user double clicks a customer in the list
       precondition: proper customer has been stored and user double clicks on the customer
       postcondition: text fields on the left of the main window have been populated with the customer's information
    */
    public void clickCustomer(MouseEvent actionEvent) {
        if (actionEvent.getClickCount() == 1 && !tableView.getSelectionModel().isEmpty()) {
            Customer clickedCustomer = tableView.getSelectionModel().getSelectedItem();

            text_first.setText(clickedCustomer.getFirstName());
            text_last.setText(clickedCustomer.getLastName());
            text_number.setText(clickedCustomer.getNumber());
            text_email.setText(clickedCustomer.getEmailAddress());
            text_address.setText(clickedCustomer.getAddress());
            text_city.setText(clickedCustomer.getCity());
            text_zipcode.setText(clickedCustomer.getZipcode());
            for (int i = 0; i<state_list.size(); i++){
                if(clickedCustomer.getState().equals(state_list.get(i))){
                    states_cb.getSelectionModel().select(states[i]);
                }
            }

        }
    }
    /*
       Updates the customer the user currently has selected on the table with the new information in the text fields
       precondition: a customer on the table has been selected and the user entered information in the proper format in the text fields
       postcondition: the selected customer is updated in both table and database
    */
    public void updateCustomer(ActionEvent actionEvent) {
        Customer clickedCustomer = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        String firstName = text_first.getText();
        String lastName = text_last.getText();
        String number = text_number.getText();
        String email = text_email.getText();
        String address = text_address.getText();
        String city = text_city.getText();
        String zipcode = text_zipcode.getText();
        String state = states_cb.getSelectionModel().getSelectedItem();
        long key = clickedCustomer.getpKey().getValue();

        if(email.length() == 0){
            email = "";
        }

        if (firstName.length() == 0 || lastName.length() == 0 || number.length() == 0 || address.length() == 0 || city.length() == 0 || zipcode.length() < 5 || state.length() == 0) {
            if (number.length() < 10) {
                System.out.println("Invalid number");
            }
            System.out.println("One or more fields encountered and error");
        } else {
            String sql = "UPDATE Customer SET firstName=?, lastName=?, number=?, email=?, address=?, city=?, zipcode=?, state=? where Key = ?";
            System.out.println(sql);
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, number);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, address);
                preparedStatement.setString(6, city);
                preparedStatement.setString(7, zipcode);
                preparedStatement.setString(8, state);
                preparedStatement.setLong(9, key);
                preparedStatement.execute();
                System.out.println("Update Successful");
                customers.set(index, clickedCustomer);
                refreshTable();
                updatePopup();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /*
       Deletes the customer the user has currently selected in both the database and the table
       precondition: proper customer has been stored and user double clicks on the customer
       postcondition: the selected customer object is deleted from both the table and the database
    */
    public void deleteCustomer() {
        Customer clickedCustomer = tableView.getSelectionModel().getSelectedItem();
        //String sql = "DELETE FROM Customer where email = '" + clickedCustomer.getEmailAddress() + "'";
        String sql = "DELETE FROM Customer where Key = ?";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            long key = clickedCustomer.getpKey().getValue();
            preparedStatement.setLong(1, key);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_first.clear();
            text_last.clear();
            text_number.clear();
            text_email.clear();
            text_address.clear();
            text_city.clear();
            text_zipcode.clear();
            states_cb.valueProperty().set(null);

            deletePopup();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
       Refreshes the table by rereading from the database when called
       precondition: method has to be called by other methods like deleteCustomer(), addCustomer(), or updateCustomer()
       postcondition: the table on the GUI is refreshed with the information about customers read from the database
    */
    public void refreshTable() {
        try {
            customers.clear();
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
                customers.add(newCustomer);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
       Filters the Customer table based on what string input the user types into the bar (a search function)
       precondition: there must be objects for the search to filter through and the user must enter a proper string
       postcondition: the table will list only the customers that have strings matching what the user entered in the search bar
    */
    FilteredList filter = new FilteredList(customers, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Customer>) customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (customer.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (customer.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (customer.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (customer.getEmailAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (customer.getEmailAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (customer.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<Customer> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }
    /*
       Opens the car window which displays all the cars in the system's database when the user clicks the Car button
       precondition: the customer page must be running and displaying properly
       postcondition: a new window is opened with methods and information pertaining to Car objects and the CarController
    */
    public void carWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Car.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Cars");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void employeeWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void inventoryWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InventoryLogin.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory Login");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    /*
       Opens the Car Service Order window which displays all the Orders in the system's database when the user
       clicks the Car Service Order button
       precondition: the customer page must be running and displaying properly
       postcondition: a new window is opened with methods and information pertaining to Car Service Orders objects
       and the CarServiceOrderController
    */
    public void carServiceOrderWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CarServiceOrder.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Car Service Orders");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }


    /*
        Opens the Report window which displays all the Orders in the system's database when the user
        clicks the Report button
        precondition: the customer page must be running and displaying properly
        postcondition: a new window is opened with methods and information pertaining to Report objects
        and the ReportController
    */
    public void reportWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Report.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Reports");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void updatePopup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("UpdatePopup.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Update Successful!");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void addedPopup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerAdded.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Customer Added");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void deletePopup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerDeleted.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Customer Deleted");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }
}

