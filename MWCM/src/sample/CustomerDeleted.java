package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CustomerDeleted{
        @FXML
        private Button button_ok;
        /*
           Closes the window when OK is pressed
           precondition: user has to click the "OK" button
           postcondition: closes the window
         */
        public void close(ActionEvent actionEvent){
            Stage stage = (Stage)button_ok.getScene().getWindow();
            stage.close();
        }
}
