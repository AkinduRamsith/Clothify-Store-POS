package edu.icet.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SalesReturnFormController {
    private Parent parent;
    private Stage stage;
    private Scene scene;
    public void btnBackOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/home_page_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
