package sample;

import database.DBConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class InventoryLoginController implements Initializable {

    @FXML
    private PasswordField password;
    @FXML
    private Label status;

    static ObservableList<Login> logins = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        getLogins();
    }


    private ObservableList<Login> getLogins(){
        logins.clear();
        try {
            Connection con = DBConnector.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from Login");

            while (rs.next()) {
                Login newLogin = new Login(rs.getString("password"));
                logins.add(newLogin);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return logins;
    }

    public void loginTest(KeyEvent keyEvent){
        password.setOnKeyPressed(e ->{
            if(e.getCode() == KeyCode.ENTER){
                verifyLogin();
            }}
        );
    }

    public void loginClicked(ActionEvent actionEvent){
        verifyLogin();
    }

    public void verifyLogin(){
        System.out.println(logins.size());
        String pw = password.getText();
        for (int i = 0; i < logins.size(); i++){
            if (pw.equals(logins.get(i).getPassword())){
                System.out.println("Login Successful");
                status.setText("Login Successful!");
                status.setTextFill(Color.GREEN);
                Stage stage1 = (Stage) status.getScene().getWindow();
                stage1.close();

                loadInventory();


            }else {
                System.out.println("Incorrect Password");
                status.setText("Incorrect Password!");
                status.setTextFill(Color.RED);
            }
        }
    }

    public void loadInventory(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inventory.fxml"));
            Parent root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Inventory");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e){
            System.err.println(e);
        }
    }
}

