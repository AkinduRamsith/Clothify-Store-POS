package edu.icet.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;

import java.net.URL;
import java.util.ResourceBundle;

public class HomePageFormController implements Initializable {
    @FXML
    private PieChart pieChartSales;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieObservableList= FXCollections.observableArrayList(
          new PieChart.Data("Gents",10),
          new PieChart.Data("Ladies",40),
          new PieChart.Data("Kids",30),
          new PieChart.Data("Others",20)
        );
        pieChartSales.setData(pieObservableList);
    }
}
