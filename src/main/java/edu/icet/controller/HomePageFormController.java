package edu.icet.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class HomePageFormController implements Initializable {
    public Label lblDate;
    public Label lblTime;
    private Parent parent;
    private Stage stage;
    private Scene scene;
    @FXML
    private PieChart pieChartSales;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayTime();
        ObservableList<PieChart.Data> pieObservableList= FXCollections.observableArrayList(
          new PieChart.Data("Gents",10),
          new PieChart.Data("Ladies",40),
          new PieChart.Data("Kids",30),
          new PieChart.Data("Others",20)
        );
        pieChartSales.setData(pieObservableList);
    }

//    public void btnCalculatorOnAction(ActionEvent actionEvent) throws IOException {
//        Runtime.getRuntime().exec("calc");
//    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {

        parent= FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        scene=new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void btnCalculatorOnAction(MouseEvent mouseEvent) {
        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void displayTime(){
        Timeline date=new Timeline(new KeyFrame(Duration.ZERO,
                e->lblDate.setText(LocalDate.now().
                        format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))),
                new KeyFrame((Duration.seconds(1))));
        date.setCycleCount(Animation.INDEFINITE);
        date.play();
        Timeline time=new Timeline(new KeyFrame(Duration.ZERO,
                e-> lblTime.setText(LocalDateTime.now().
                        format(DateTimeFormatter.ofPattern("HH:mm:ss")))),
                new KeyFrame(Duration.seconds(1)));

        time.setCycleCount(Animation.INDEFINITE);
        time.play();
    }

    public void btnOrderFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/place_order_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void btnItemFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/add_item_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnEmployerFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/employer_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSupplierFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/supplier_form.fxmll"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnOrderDetailFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/order_details_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSalesReturnFormOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/sales_return_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSalesReportOnAction(ActionEvent actionEvent) {
        try {
            parent= FXMLLoader.load(getClass().getResource("/view/sales_report_form.fxml"));
            stage= (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene=new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
