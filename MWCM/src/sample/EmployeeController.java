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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployeeController implements Initializable {

    @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> col_first;
    @FXML
    private TableColumn<Employee, String> col_last;
    @FXML
    private TableColumn<Employee, String> col_number;
    @FXML
    private TableColumn<Employee, String> col_email;
    @FXML
    private TableColumn<Employee, String> col_city;
    @FXML
    private TableColumn<Employee, String> col_zip;
    @FXML
    private TableColumn<Employee, String> col_state;
    @FXML
    private TableColumn<Employee, String> col_payrate;
    @FXML
    private Button payroll_button;


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
    private TextField text_payrate;
    @FXML
    private ComboBox<String> states_cb;
    @FXML
    private TextField text_search;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private ObservableList<String> state_list = FXCollections.observableArrayList();
    private String[] states =  { "AK","AL","AR","AS","AZ","CA","CO","CT","DC","DE","FL","GA","GU","HI","IA","ID","IL","IN","KS","KY","LA","MA","MD","ME","MI","MN","MO","MS","MT","NC","ND","NE","NH","NJ","NM","NV","NY","OH","OK","OR","PA","PR","RI","SC","SD","TN","TX","UT","VA","VI","VT","WA","WI","WV","WY"};

    @Override
    /* Populating employee table on program startup
       precondition: program must be run
       postcondition: tableView is populated with employees from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        col_first.setCellValueFactory(new PropertyValueFactory<Employee, String>("firstName"));
        col_last.setCellValueFactory(new PropertyValueFactory<Employee, String>("lastName"));
        col_number.setCellValueFactory(new PropertyValueFactory<Employee, String>("number"));
        col_email.setCellValueFactory(new PropertyValueFactory<Employee, String>("emailAddress"));
        col_city.setCellValueFactory(new PropertyValueFactory<Employee, String>("city"));
        col_zip.setCellValueFactory(new PropertyValueFactory<Employee, String>("zipcode"));
        col_state.setCellValueFactory(new PropertyValueFactory<Employee, String>("state"));
        col_payrate.setCellValueFactory(new PropertyValueFactory<Employee, String>("payrate"));

        employeeTableView.setItems(getEmployee());
        states_cb.setItems(getStates());
    }

    /*
        Connecting to database and reading Employee objects to populate ObservableList
        precondition: called by intialize
        postcondition:
     */
    private ObservableList<Employee> getEmployee() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Employee");

            while (rs.next()) {
                Employee newEmployee = new Employee(
                        rs.getLong("Key"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zipcode"),
                        rs.getString("state"),
                        rs.getDouble("payrate"));
                employees.add(newEmployee);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return employees;
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
       Creates Employee object and adds it to database and refreshes the table with the new Employee added
       precondition: user clicks add button and has entered the information in the proper formats in the proper fields
       postcondition: new Employee is added to both the database and observable list which is used to display employees
    */
    public void addEmployee(ActionEvent actionEvent) {
        String firstName = text_first.getText();
        String lastName = text_last.getText();
        String number = text_number.getText();
        String email = text_email.getText();
        String address = text_address.getText();
        String city = text_city.getText();
        String zipcode = text_zipcode.getText();
        String state = states_cb.getSelectionModel().getSelectedItem();
        String stringPayrate = text_payrate.getText();
        // attempt to parse payrate into a double
        double payrate;
        try{
            payrate = Double.parseDouble(text_payrate.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            payrate = 0.0;
        }

        if (firstName.length() == 0 || lastName.length() == 0 || number.length() == 0
                || address.length() == 0 || city.length() == 0 || zipcode.length() < 5 || state.length() == 0
                || stringPayrate.length() == 0) {
            if (number.length() < 10) {
                System.out.println("Invalid number");
            }
            System.out.println("One or more fields encountered and error");
        } else {
            if(email.length() == 0){
                email = "";
            }
            //Employee emp = new Employee(firstName, lastName, number, email, address, city, zipcode, state, payrate);
            //employees.add(emp);
            String sql = "INSERT INTO Employee(firstName,lastName,number,email,address,city,zipcode,state,payrate)  VALUES(?,?,?,?,?,?,?,?,?)";
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
                preparedStatement.setDouble(9, payrate);
                preparedStatement.execute();
                addedPopup();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        refreshTable();
    }

    /*
       Sets the information of the employee to the text boxes on the left menu when the user double clicks a employee in the list
       precondition: proper employee has been stored and user double clicks on the employee
       postcondition: text fields on the left of the main window have been populated with the employee's information
    */
    public void clickEmployee(MouseEvent actionEvent) {
        if (actionEvent.getClickCount() == 1) {
            Employee clickedEmployee = employeeTableView.getSelectionModel().getSelectedItem();

            text_first.setText(clickedEmployee.getFirstName());
            text_last.setText(clickedEmployee.getLastName());
            text_number.setText(clickedEmployee.getNumber());
            text_email.setText(clickedEmployee.getEmailAddress());
            text_address.setText(clickedEmployee.getAddress());
            text_city.setText(clickedEmployee.getCity());
            text_zipcode.setText(clickedEmployee.getZipcode());
            for (int i = 0; i<state_list.size(); i++){
                if(clickedEmployee.getState().equals(state_list.get(i))){
                    states_cb.getSelectionModel().select(states[i]);
                }
            }
            text_payrate.setText(Double.toString(clickedEmployee.getPayrate()));
        }
    }

    /*
       Updates the employee the user currently has selected on the table with the new information in the text fields
       precondition: an employee on the table has been selected and the user entered information in the proper format in the text fields
       postcondition: the selected employee is updated in both table and database
    */
    public void updateEmployee(ActionEvent actionEvent) {
        Employee clickedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        int index = employeeTableView.getSelectionModel().getSelectedIndex();
        String firstName = text_first.getText();
        String lastName = text_last.getText();
        String number = text_number.getText();
        String email = text_email.getText();
        String address = text_address.getText();
        String city = text_city.getText();
        String zipcode = text_zipcode.getText();
        String state = states_cb.getSelectionModel().getSelectedItem();
        String stringPayrate = text_payrate.getText();
        if(email.length() == 0){
            email = "";
        }

        // attempt to parse payrate into a double
        double payrate;
        try{
            payrate = Double.parseDouble(text_payrate.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            payrate = 0.0;
        }
        long key = clickedEmployee.getpKey();

        if (firstName.length() == 0 || lastName.length() == 0 || number.length() == 0
                || address.length() == 0 || city.length() == 0 || zipcode.length() < 5 || state.length() == 0
                || stringPayrate.length() == 0) {
            if (number.length() < 10) {
                System.out.println("Invalid number");
            }
            System.out.println("One or more fields encountered and error");
        } else {
            String sql = "UPDATE Employee SET firstName=?, lastName=?, number=?, email=?, address=?, city=?, zipcode=?," +
                    " state=?, payrate=? where Key=?";
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
                preparedStatement.setDouble(9, payrate);
                preparedStatement.setLong(10, key);
                preparedStatement.execute();
                System.out.println("Update Successful");
                employees.set(index, clickedEmployee);
                refreshTable();
                updatePopup();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    /*
       Deletes the employee the user has currently selected in both the database and the table
       precondition: proper employee has been stored and user double clicks on the employee
       postcondition: the selected employee object is deleted from both the table and the database
    */
    public void deleteEmployee() {
        Employee clickedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        //String sql = "DELETE FROM Employee where email = '" + clickedEmployee.getEmailAddress() + "'";
        String sql = "DELETE FROM Employee where Key=?";
        long key = clickedEmployee.getpKey();
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
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
            text_payrate.clear();
            deletePopup();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
       Refreshes the table by rereading from the database when called
       precondition: method has to be called by other methods like deleteEmployee(), addEmployee(), or updateEmployee()
       postcondition: the table on the GUI is refreshed with the information about employees read from the database
    */
    public void refreshTable() {
        try {
            employees.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Employee");
            while (rs.next()) {
                Employee newEmployee = new Employee(
                        rs.getLong("Key"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("number"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("zipcode"),
                        rs.getString("state"),
                        rs.getDouble("payrate"));
                employees.add(newEmployee);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
       Filters the Employee table based on what string input the user types into the bar (a search function)
       precondition: there must be objects for the search to filter through and the user must enter a proper string
       postcondition: the table will list only the employees that have strings matching what the user entered in the search bar
    */
    FilteredList filter = new FilteredList(employees, e->true);
    public void searchEmployee(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Employee>) employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (employee.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getNumber().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getEmailAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getCity().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (employee.getZipcode().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (employee.getState().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }else if (Double.toString(employee.getPayrate()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<Employee> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(employeeTableView.comparatorProperty());
            employeeTableView.setItems(sortedDatabase);
        }));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeAdded.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Employee Added");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }

    public void deletePopup(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample/EmployeeDeleted.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Employee Deleted");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }


    public void payrollWindow(MouseEvent mouseEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Payroll.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Payroll");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }
}