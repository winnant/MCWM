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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class InventoryController implements Initializable {

    @FXML
    private TableView<AutoPart> tableView;
    @FXML
    private TableColumn<AutoPart, String> col_id;
    @FXML
    private TableColumn<AutoPart, String> col_name;
    @FXML
    private TableColumn<AutoPart, String> col_category;
    @FXML
    private TableColumn<AutoPart, String> col_qty;
    @FXML
    private TableColumn<AutoPart, String> col_price;

    @FXML
    private TextField text_id;
    @FXML
    private TextField text_name;
    @FXML
    private TextField text_qty;
    @FXML
    private TextField text_price;
    @FXML
    private ComboBox<CarService> carservice_cb;
    @FXML
    private TextField text_search;

    static ObservableList<AutoPart> parts = FXCollections.observableArrayList();
    private ObservableList<CarService> carservices = FXCollections.observableArrayList();
    //private String[] categories = {"Accessory","Tire","Engine"};




    public void initialize(URL location, ResourceBundle resources) {
        parts.clear();
        col_id.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("id"));
        col_name.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("name"));
        col_qty.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("quantity"));
        col_price.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("price"));
        col_category.setCellValueFactory(new PropertyValueFactory<AutoPart, String>("category"));

        tableView.setItems(getInventory());
        carservice_cb.setItems(getCarServices());

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
     * Read Car Services from the DB
     * @return
     */
    public ObservableList<CarService> getCarServices(){

        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from CarService");

            while (rs.next()) {
                CarService carservice = new CarService(
                        rs.getLong("Key"),
                        rs.getString("Service_ID"),
                        rs.getString("Service_Name"),
                        rs.getDouble("Price"));
                carservices.add(carservice);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return carservices;
    }
    /*
       Creates AutoPart object and adds it to database and refreshes the table with the new AutoPart added
       precondition: user clicks add button and has entered the information in the proper formats in the proper fields
       postcondition: new AutoPart is added to both the database and observable list which is used to display AutoPart
    */
    public void addPart(ActionEvent actionEvent) {
        String id = text_id.getText();
        String name = text_name.getText();
        String stringquantity = text_qty.getText();
        String stringprice = text_price.getText();

        CarService carservice = carservice_cb.getSelectionModel().getSelectedItem();
        long cs_key;
        String category;
        try {
            cs_key = carservice.getpKey();
            category = carservice.getService_ID();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            cs_key = 1;
            category = "Service was not selected.";
        }

        double price;
        try{
            price = Double.parseDouble(text_price.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            price = 0.0;
        }

        long quantity;
        try{
            quantity = Long.parseLong(text_qty.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            quantity = 0;
        }

        if (id.length() == 0 || name.length() == 0 || stringquantity.length() == 0 || stringprice.length() == 0
                || carservice == null)  {
            System.out.println("One or more fields encountered and error");
        } else {
            //AutoPart autoPart = new AutoPart(id, name, quantity, price, carservice);
            //parts.add(autoPart);
            String sql = "INSERT INTO Inventory(id,name,quantity,price,CarService_Key,Category)  VALUES(?,?,?,?,?,?)";
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setLong(3, quantity);
                preparedStatement.setDouble(4, price);
                preparedStatement.setLong(5, cs_key);
                preparedStatement.setString(6, category);
                preparedStatement.execute();
                System.out.println("Part successfully added");
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        refreshTable();
    }
    /*
   Updates the employee the user currently has selected on the table with the new information in the text fields
   precondition: a employee on the table has been selected and the user entered information in the proper format in the text fields
   postcondition: the selected employee is updated in both table and database
*/
    public void updatePart(ActionEvent actionEvent) {
        AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();
        int index = tableView.getSelectionModel().getSelectedIndex();
        long key = clickedPart.getpKey();
        String id = text_id.getText();
        String name = text_name.getText();
        String stringquantity = text_qty.getText();
        String stringprice = text_price.getText();

        CarService carservice = carservice_cb.getSelectionModel().getSelectedItem();
        long cs_key;
        String category;
        try {
            cs_key = carservice.getpKey();
            category = carservice.getService_ID();
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            cs_key = 1;
            category = "Service was not selected.";
        }

        double price;
        try{
            price = Double.parseDouble(text_price.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            price = 0.0;
        }

        long quantity;
        try{
            quantity = Long.parseLong(text_qty.getText());
        }
        catch (Exception e){
            System.err.println(e.getMessage());
            quantity = 0;
        }

        if (id.length() == 0 || name.length() == 0 || stringquantity.length() == 0 || stringprice.length() == 0
                || category.length() == 0)  {
            System.out.println("One or more fields encountered and error");
        } else {
            String sql = "UPDATE Inventory SET id=?, name=?, quantity=?, price=?, CarService_Key=?, category=? where Key =?";
            System.out.println(sql);
            try {
                Connection con = DBConnector.getConnection();
                PreparedStatement preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setLong(3, quantity);
                preparedStatement.setDouble(4, price);
                preparedStatement.setLong(5, cs_key);
                preparedStatement.setString(6, category);
                preparedStatement.setLong(7, key);
                preparedStatement.execute();
                System.out.println("Update Successful");
                parts.set(index, clickedPart);
                refreshTable();

            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
    }
    /*
   Sets the information of the AutoPart to the text boxes on the left menu when the user clicks a part in the list
   precondition: proper part has been stored and user double clicks on the employee
   postcondition: text fields on the left of the main window have been populated with the part's information
*/
    public void clickPart(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();

            text_id.setText(clickedPart.getId());
            text_name.setText(clickedPart.getName());
            text_qty.setText(Long.toString(clickedPart.getQuantity()));
            text_price.setText(Double.toString(clickedPart.getPrice()));
            for (int i = 0; i< carservices.size(); i++){
                if(clickedPart.getCarService_Key() == (carservices.get(i).getpKey())){
                    carservice_cb.getSelectionModel().select(i);
                }
            }
        }
    }
    /*
   Deletes the AutoPart the user has currently selected in both the database and the table
   precondition: proper AutoPart has been stored and user clicks on the AutoPart
   postcondition: the selected AutoPart object is deleted from both the table and the database
*/
    public void deletePart(ActionEvent actionEvent) {
        AutoPart clickedPart = tableView.getSelectionModel().getSelectedItem();
        long key = clickedPart.getpKey();
        String sql = "DELETE FROM Inventory where Key = ?";
        try {
            Connection con = DBConnector.getConnection();
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setLong(1, key);
            preparedStatement.execute();
            System.out.println("Delete Successful");
            refreshTable();
            text_id.clear();
            text_name.clear();
            text_qty.clear();
            text_price.clear();
            carservice_cb.valueProperty().set(null);


        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /*
   Refreshes the table by rereading from the database when called
   precondition: method has to be called by other methods like deletePart(), addPart(), or updatePart()
   postcondition: the table on the GUI is refreshed with the information about AutoParts read from the database
*/
    public void refreshTable() {
        try {
            parts.clear();
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Inventory");
            while (rs.next()) {
                AutoPart newPart = new AutoPart(
                        rs.getLong("Key"),
                        rs.getLong("CarService_Key"),
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getLong("quantity"),
                        rs.getDouble("price"),
                        rs.getString("Category"));
                parts.add(newPart);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    /*
   Filters the Inventory table based on what string input the user types into the bar (a search function)
   precondition: there must be objects for the search to filter through and the user must enter a proper string
   postcondition: the table will list only the employees that have strings matching what the user entered in the search bar
*/
    FilteredList filter = new FilteredList(parts, e->true);
    public void searchPart(KeyEvent event) {
        text_search.textProperty().addListener(((observable, oldValue, newValue) -> {
            filter.setPredicate((Predicate<? super AutoPart>) part -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                } else {
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (part.getId().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    } else if (Long.toString(part.getQuantity()).contains(lowerCaseFilter)) {
                        return true;
                    } else if (Double.toString(part.getPrice()).contains(lowerCaseFilter)) {
                        return true;
                    } else if (part.getCategory().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }
                    return false;
                }
            });
            SortedList<AutoPart> sortedDatabase = new SortedList<>(filter);
            sortedDatabase.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedDatabase);
        }));
    }

    public void carServiceWindow(MouseEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CarService.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Car Service");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }


}
