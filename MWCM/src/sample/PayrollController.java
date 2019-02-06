package sample;

import database.DBConnector;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PayrollController implements Initializable {
    @FXML
    private TableView<Payroll> tableView;
    @FXML
    private TextField text_hours;
    @FXML
    private DatePicker start_date;
    @FXML
    private DatePicker end_date;
    @FXML
    private ComboBox<Employee> employeeList;
    @FXML
    private TextField text_search;
    @FXML
    private TextField text_BonusPay;

    @FXML
    private TableColumn<Payroll,String> col_employee;
    @FXML
    private TableColumn<Payroll,String> col_start;
    @FXML
    private TableColumn<Payroll,String> col_end;
    @FXML
    private TableColumn<Payroll,Integer> col_hours;
    @FXML
    private TableColumn<Payroll, Double> col_payrate;
    @FXML
    private TableColumn<Payroll,Double> col_amount;

    //Keeps track of all employees in the database
    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    // Keeps track of all the payrolls in the database
    private ObservableList<Payroll> payrolls = FXCollections.observableArrayList();


    /*
       Initializes the payroll database and populates the table and dropdown menu for employee selection on startup
       precondition: can only be accessed from a button on the Employee page gui
       postcondition: both the employee dropdown and payroll table are populated with the respective objects from the database
    */
    public void initialize(URL location, ResourceBundle resources) {
        // Establish observable lists
        employees.clear();
        payrolls.clear();

        // Table View
        col_employee.setCellValueFactory(new PropertyValueFactory<Payroll, String>("employee_name"));
        col_start.setCellValueFactory(new PropertyValueFactory<Payroll, String>("startDate"));
        col_end.setCellValueFactory(new PropertyValueFactory<Payroll, String>("endDate"));
        col_hours.setCellValueFactory(new PropertyValueFactory<Payroll, Integer>("hours"));
        col_payrate.setCellValueFactory(new PropertyValueFactory<Payroll, Double>("payrate"));
        col_amount.setCellValueFactory(new PropertyValueFactory<Payroll, Double>("payment"));

        employeeList.setItems(getEmployee());
        tableView.setItems(getPayroll());


    }
    /*
       Reads payroll objects from the database and adds them to the observable list of payroll
       precondition: a proper list of payroll objects must be declared and method must be called through initialize
       postcondition: returns an ObservableList of Payroll objects
    */
    private ObservableList<Payroll> getPayroll() {
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Payroll");

            while (rs.next()) {
                Payroll newPayment = new Payroll(
                        rs.getLong("emp_key"),
                        rs.getString("employee_name"),
                        rs.getInt("hours"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getDouble("payment"),
                        rs.getLong("Key"),
                        rs.getDouble("payrate"),
                        rs.getDouble("bonus"));
                payrolls.add(newPayment);
            }
            rs.close();
            statement.close();
            con.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return payrolls;
    }

    /*
 Connecting to database and reading Employee objects to populate ObservableList
 precondition: called by intialize and a list of employees has been declared
 postcondition: returns an ObservableList of Employee Objects
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
          Creates Payroll object and adds it to database and refreshes the table with the new Payroll
          added.
          precondition: user clicks add button and has entered the information in the proper formats in the
          proper fields
          postcondition: new Payroll is added to both the database and observable list which is used
          to display Payroll
       */
    public void addPayroll(ActionEvent actionEvent){
        Employee selectedEmployee =  employeeList.getSelectionModel().getSelectedItem();
        int hours = Integer.valueOf(text_hours.getText());
        String startdate = start_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String enddate = end_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        double payment = calculatePayment(hours, selectedEmployee.getPayrate());
        double bonusPay = text_BonusPay.getText().isEmpty() ? 0.0 : Double.valueOf(text_BonusPay.getText());
        payment = payment + bonusPay;
        long emp_key;

        if(hours == 0 || payment == 0 || startdate.isEmpty()){
            System.out.println("Invalid field");
        }else{
            String employeeName = selectedEmployee.toString();
            emp_key = selectedEmployee.getpKey();
            //System.out.println("emp_key: " + emp_key);
            Payroll newPayroll = new Payroll (emp_key,employeeName,hours,startdate,enddate,payment,
                    selectedEmployee.getPayrate(),bonusPay);
            payrolls.add(newPayroll);
            String sql = "INSERT INTO Payroll(emp_key,employee_name,hours,startDate,endDate,payment,payrate,bonus) VALUES(?,?,?,?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, emp_key);
                preparedStatement.setString(2, employeeName);
                preparedStatement.setInt(3, hours);
                preparedStatement.setString(4, startdate);
                preparedStatement.setString(5, enddate);
                preparedStatement.setDouble(6, payment);
                preparedStatement.setDouble(7, selectedEmployee.getPayrate());
                preparedStatement.setDouble(8,bonusPay);
                preparedStatement.execute();
                System.out.println("Add Successful");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        refreshTable();
        }
    }

    /**
     *  Calculate the pay of an employee
     * @param hours
     * @param payrate
     * @return double type pay
     */
    public double calculatePayment(int hours, double payrate){
        double pay = 0;
        if (hours <= 40){
            pay = hours * payrate;
        }
        else if (hours > 40 && hours <= 80){     // 1.5 pay
            pay = (40 * payrate) + ((hours-40) * payrate * 1.5);
        }
        else if (hours > 80){  // double pay
            pay = (40 * payrate) + (40 * payrate * 1.5) + ((hours-80) * payrate * 2.0);
        }
        return pay;
    }
    /*
       Updates the payroll the user currently has selected on the table with the new information in the fields
       precondition: a payroll on the table has been selected and the user entered information in the proper format in the fields
       postcondition: the selected payroll is updated on both table and database
    */
    public void updatePayroll(ActionEvent actionEvent){
        Payroll clickedPayroll = tableView.getSelectionModel().getSelectedItem();
        Employee selectedEmployee = employeeList.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        int hours = Integer.valueOf(text_hours.getText());
        String startdate = start_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        String enddate = end_date.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yy"));
        double payrate = clickedPayroll.getPayrate();
        double payment = calculatePayment(hours, payrate);
        double bonusPay = text_BonusPay.getText().isEmpty() ? 0.0 : Double.valueOf(text_BonusPay.getText());
        payment = payment + bonusPay;

        if(hours == 0 || payment == 0 || startdate.isEmpty()){
            System.out.println("Invalid field");
        }else{
            String employeeName = selectedEmployee.toString();
            long emp_key = selectedEmployee.getpKey();
            long key = clickedPayroll.getpKey();
            String sql = "UPDATE Payroll SET emp_key=? ,employee_name=? ,hours=?,startDate=? ,endDate=?,payment=?, bonus=? where Key=?";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setLong(1, emp_key);
                preparedStatement.setString(2, employeeName);
                preparedStatement.setInt(3, hours);
                preparedStatement.setString(4, startdate);
                preparedStatement.setString(5, enddate);
                preparedStatement.setDouble(6, payment);
                preparedStatement.setDouble(7,bonusPay);
                preparedStatement.setLong(8, key);
                preparedStatement.execute();
                payrolls.set(index,clickedPayroll);
                System.out.println("Update Successful");
                refreshTable();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    /*
        Deletes the payroll the user has currently selected in both the database and the table
        precondition: a proper payroll has been stored in the database and  the user has clicked on the desired payroll
        postcondition: the selected payroll object is deleted from both the table and the database
    */
    public void deletePayroll(ActionEvent actionEvent){
        Payroll clickedPayroll = tableView.getSelectionModel().getSelectedItem();
        String sql = "DELETE FROM Payroll where Key = ?";

        try {
            long key = clickedPayroll.getpKey();
            System.out.println(key);
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, key);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_hours.clear();
            start_date.setValue(null);
            end_date.setValue(null);
            text_BonusPay.clear();
            employeeList.valueProperty().set(null);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

    }

     /*
       Sets the information of the payroll to the fields on the left menu when the user clicks a payroll in the list
       precondition: proper payroll has been stored and user double clicks on the payroll
       postcondition: fields on the left of the main window have been populated with the payroll's information
    */
    public void clickedPayroll(MouseEvent actionEvent){
        int index;
        if(actionEvent.getClickCount() == 1){
            if (tableView.getSelectionModel().isEmpty()){

            }else {
                Payroll clickedPayroll = tableView.getSelectionModel().getSelectedItem();
                //System.out.println(clickedPayroll.getpKey());
                String startDate = clickedPayroll.getStartDate();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MM/dd/yy");
                LocalDate date = LocalDate.parse(startDate,df);
                start_date.setValue(date);
                text_hours.setText(String.valueOf(clickedPayroll.getHours()));
                text_BonusPay.setText(String.valueOf(clickedPayroll.getBonus()));

                for (int i = 0; i < employees.size(); i++) {
                    Employee myEmployee = employees.get(i);
                    if (clickedPayroll.getEmp_key() == myEmployee.getpKey()) {
                        index = i;
                        employeeList.getSelectionModel().select(employees.get(index));
                    }
                }
            }
        }
    }

    /*
       Refreshes the table by rereading from the database when called
       precondition: method has to be called by other methods like addPayroll(), deletePayroll(), or updatePayroll()
       postcondition: the table on the GUI is refreshed with the information about payroll read from the database
    */
    public void refreshTable(){
        try{
            payrolls.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Payroll");

            while (rs.next()){
                Payroll newPayroll = new Payroll(
                        rs.getLong("emp_key"),
                        rs.getString("employee_name"),
                        rs.getInt("hours"),
                        rs.getString("startDate"),
                        rs.getString("endDate"),
                        rs.getDouble("payment"),
                        rs.getLong("Key"),
                        rs.getDouble("payrate"),
                        rs.getDouble("bonus"));
                payrolls.add(newPayroll);
            }
        }catch (Exception e){
            System.err.println(e);
        }
    }

    /*
   Filters the Payroll table based on what string input the user types into the bar (a search function)
   precondition: there must be objects for the search to filter through and the user must enter a proper string
   postcondition: the table will list only the payrolls that have strings matching what the user entered in the search bar
*/
    FilteredList filter = new FilteredList(payrolls, e->true);
    public void search(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super Payroll>) payroll -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (payroll.getEmployee_name().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(payroll.getHours()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (payroll.getStartDate().contains(lowerCaseFilter)) {
                        return true;
                    } else if (payroll.getEndDate().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (String.valueOf(payroll.getPayment()).toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<Payroll> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }

    /*
        Automatically assigns end date for the payment period by adding 7 days to the start date
        precondition: method has to be called, and start date has to be picked by the user and not null
        postcondition: end date box will be updated with value of start date plus 7 days
     */
    public void setEndDate(ActionEvent actionEvent){
        if(start_date.getValue() != null) {
            end_date.setValue(start_date.getValue().plusDays(7));
        }
    }
}
