package edu.icet.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import edu.icet.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddItemFormController implements Initializable {
    public JFXButton btnAdd;
    public JFXTextField txtAddQty;

    public JFXTextField txtItemType;
    public JFXTextField txtItemSize;
    public JFXComboBox cmdItemType;
    public JFXComboBox cmdItemSize;
    public JFXTextField txtItemCode;
    public JFXComboBox cmdSupplierID;
    public JFXComboBox cmdSupplierName;
    public JFXTextField txtBuyingPrice;
    public JFXTextField txtSellingPrice;
    public JFXTextField txtProfit;
    public JFXButton btnAddStock;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAdd.setDisable(true);
        txtAddQty.setDisable(true);
        txtItemSize.setDisable(true);
        txtItemType.setDisable(true);
        btnAddStock.setDisable(true);
        cmdItemSize.getItems().addAll("XS","S","M","L","XL","Custom");
        cmdItemType.getItems().addAll("Gents","Ladies","Kids","Custom");
        generateId();
        loadProfit();
        try {
            for (String id:getAllSupplierIDs() ) {
                cmdSupplierID.getItems().addAll(id);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } try {
            for (String name:getAllSupplierNames() ) {
                cmdSupplierName.getItems().addAll(name);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        cmdItemType.setOnAction(event -> {
            if (cmdItemType.getValue()!=null){
                if (!cmdItemType.getValue().equals("Custom") && !cmdItemType.getValue().toString().isEmpty()){


                    txtItemType.setDisable(true);
                    txtItemSize.clear();
                    txtItemSize.setDisable(true);
                }else if (cmdItemType.getValue().equals("Custom")){

                    txtItemType.setDisable(false);
//                    txtItemSize.setDisable(false);
                }
            }
        });
        cmdItemSize.setOnAction(event1 -> {
            if (cmdItemSize.getValue()!=null) {
                if (cmdItemSize.getValue().equals("Custom")) {
                    //txtType.setDisable(false);
                    txtItemSize.setDisable(false);
                } else {
                    //txtType.setDisable(true);
                    txtItemSize.clear();
                    txtItemSize.setDisable(true);
                }
            }else{
                txtItemSize.clear();
                txtItemSize.setDisable(true);
            }
        });

    }

    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT itemCode FROM item ORDER BY itemCode DESC LIMIT 1");
        return rst.next() ? rst.getString("itemCode") : null;
    }

    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("ITM-\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(4)) + 1;
                String newSupplierId = String.format("ITM-%04d", numericPart);
                txtItemCode.setText(newSupplierId);
            } else {
                txtItemCode.setText("ITM-0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getAllSupplierIDs() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("Select supplierId From supplier").executeQuery();
        ObservableList<String> list = FXCollections.observableArrayList();
        while (rst.next()) {
            list.add(rst.getString("supplierId"));
        }
        return list;
    }
    public static ObservableList<String> getAllSupplierNames() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBConnection.getInstance().getConnection().prepareStatement("Select supplierName From supplier").executeQuery();
        ObservableList<String> list = FXCollections.observableArrayList();
        while (rst.next()) {
            list.add(rst.getString("supplierName"));
        }
        return list;
    }
    public void btnAddOnAction(ActionEvent actionEvent) {
    }


    public void cmdSupplierIDOnAction(ActionEvent actionEvent) {
        try {
            String supId = cmdSupplierID.getSelectionModel().getSelectedItem().toString();
            cmdSupplierName.setValue(SupplierFormController.searchSupplierById(supId).getSupplierName());
        } catch (SQLException | ClassNotFoundException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }

    }

    public void cmdSupplierNameOnAction(ActionEvent actionEvent) {
        try {
            String supplierName = cmdSupplierName.getSelectionModel().getSelectedItem().toString();
            cmdSupplierID.setValue(SupplierFormController.searchSupplierByName(supplierName).getSupplierId());
        } catch (SQLException | ClassNotFoundException | NullPointerException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage());
        }
    }
    private void loadProfit() {
        txtSellingPrice.setOnKeyReleased(ke -> {
            if (txtSellingPrice.getText()!=null && txtSellingPrice.getText().matches("^-?\\d+(\\.\\d{2}+)?$")) {
                txtSellingPrice.setOnKeyTyped(actionEvent -> {
                    if (!txtSellingPrice.getText().isEmpty() && !txtBuyingPrice.getText().isEmpty()) {
                        String profit = String.format("%8.2f", Double.parseDouble(txtSellingPrice.getText()) - Double.parseDouble(txtBuyingPrice.getText()));
                        txtProfit.setText(profit);
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });
        txtBuyingPrice.setOnKeyReleased(ke -> {
            if (txtBuyingPrice.getText()!=null && txtBuyingPrice.getText().matches("^-?\\d+(\\.\\d{2}+)?$")) {
                txtBuyingPrice.setOnKeyTyped(actionEvent -> {
                    if (!txtSellingPrice.getText().isEmpty() && !txtBuyingPrice.getText().isEmpty()) {
                        String profit = String.format("%8.2f", Double.parseDouble(txtSellingPrice.getText()) - Double.parseDouble(txtBuyingPrice.getText()));
                        txtProfit.setText(profit);
                    }
                });
            } else {
                new Alert(Alert.AlertType.WARNING, "Please Enter only 0-9 values..!").show();
            }
        });
    }
}
