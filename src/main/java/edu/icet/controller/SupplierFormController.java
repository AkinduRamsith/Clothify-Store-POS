package edu.icet.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.icet.db.DBConnection;
import edu.icet.model.Employer;
import edu.icet.model.Supplier;
import edu.icet.model.tm.EmployerTm;
import edu.icet.model.tm.SupplierTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class SupplierFormController implements Initializable {
    public JFXTextField txtSupplierID;
    public JFXTextField txtSupplierContact;

    public JFXComboBox cmdSupplierTitle;
    public JFXTextField txtSupplierSearch;
    public JFXTextField txtSupplierName;
    public JFXTextField txtSupplierEmail;
    public JFXTextField txtSupplierCompany;
    @FXML
    private TreeTableColumn colSupplierCompany;

    @FXML
    private TreeTableColumn colSupplierContact;

    @FXML
    private TreeTableColumn colSupplierID;

    @FXML
    private TreeTableColumn colSupplierName;

    @FXML
    private TreeTableColumn colSupplierOption;

    @FXML
    private TreeTableColumn colSupplierTitle;
    @FXML
    private TreeTableColumn colSupplierEmail;

    @FXML
    private JFXTreeTableView<SupplierTm> tblSupplier;
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

    public void btnSupplierPrintOnAction(ActionEvent actionEvent) {
    }

    public void btnSupplierCleanOnAction(ActionEvent actionEvent) {
        cleanFields();
        generateId();
    }

    public void btnSuppleriSaveOnAction(ActionEvent actionEvent) {
        try{
            Supplier supplier=new Supplier(
                    txtSupplierID.getText(),
                    cmdSupplierTitle.getSelectionModel().getSelectedItem().toString(),
                    txtSupplierName.getText(),
                    txtSupplierContact.getText(),
                    txtSupplierCompany.getText(),
                    txtSupplierEmail.getText()
            );
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("Insert into supplier values(?,?,?,?,?,?)");
            pstm.setObject(1,supplier.getSupplierId());
            pstm.setObject(2,supplier.getTitle());
            pstm.setObject(3,supplier.getSupplierName());
            pstm.setObject(4,supplier.getContact());
            pstm.setObject(5,supplier.getCompany());
            pstm.setObject(6,supplier.getEmail());
            if(pstm.executeUpdate()>0){
                new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
                cleanFields();
                generateId();
                loadTable();

            }

        }catch (SQLException | ClassNotFoundException | NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "No data to save").show();


        }
    }

    private void cleanFields() {
        txtSupplierID.setText("");
        cmdSupplierTitle.setValue(null);
        txtSupplierName.setText("");
        txtSupplierContact.setText("");
        txtSupplierCompany.setText("");
        txtSupplierEmail.setText("");
    }
    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT supplierId FROM Supplier ORDER BY supplierId DESC LIMIT 1");
        return rst.next() ? rst.getString("supplierId") : null;
    }

    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("SUP-\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(4)) + 1;
                String newSupplierId = String.format("SUP-%04d", numericPart);
                txtSupplierID.setText(newSupplierId);
            } else {
                txtSupplierID.setText("SUP-0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void loadTable() {
        ObservableList<SupplierTm> tmObservableList = FXCollections.observableArrayList();
        try {
            List<Supplier> list = new ArrayList<>();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier");
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                list.add(new Supplier(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                ));
            }
            for (Supplier supplier : list) {
                JFXButton btnDelete = new JFXButton("Delete");
                btnDelete.setBackground(Background.fill(Color.rgb(0, 0, 0)));
                btnDelete.setTextFill(Color.rgb(255,255,255));

                btnDelete.setOnAction(actionEvent -> {
                    try {
                        PreparedStatement pstm1 = connection.prepareStatement("DELETE FROM supplier WHERE supplierId=?");
                        pstm1.setString(1,supplier.getSupplierId());
                        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want delete " + supplier.getSupplierId() + " supplier ?", ButtonType.YES, ButtonType.NO).showAndWait();
                        if(buttonType.get()== ButtonType.YES){
                            if(pstm1.executeUpdate()>0){
                                loadTable();
                                new Alert(Alert.AlertType.INFORMATION,"Supplier Deleted...!").show();

                            }else{
                                new Alert(Alert.AlertType.ERROR,"Something went wrong...!").show();
                            }
                        }

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    }
                });

                tmObservableList.add(new SupplierTm(
                        supplier.getSupplierId(),
                        supplier.getTitle(),
                        supplier.getSupplierName(),
                        supplier.getContact(),
                        supplier.getCompany(),
                        supplier.getEmail(),
                        btnDelete
                ));
            }
            TreeItem<SupplierTm> treeItem=new RecursiveTreeItem<>(tmObservableList, RecursiveTreeObject::getChildren);
            tblSupplier.setRoot(treeItem);
            tblSupplier.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        generateId();
        cmdSupplierTitle.getItems().addAll("Mr", "Mrs", "Miss");
        colSupplierID.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierId"));
        colSupplierTitle.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        colSupplierName.setCellValueFactory(new TreeItemPropertyValueFactory<>("supplierName"));
        colSupplierContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contact"));
        colSupplierCompany.setCellValueFactory(new TreeItemPropertyValueFactory<>("company"));
        colSupplierEmail.setCellValueFactory(new TreeItemPropertyValueFactory<>("email"));
        colSupplierOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btnDelete"));

        tblSupplier.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
            if (newValue!=null){
                setData(newValue);
            }
        } );
        txtSupplierSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                tblSupplier.setPredicate(new Predicate<TreeItem<SupplierTm>>() {
                    @Override
                    public boolean test(TreeItem<SupplierTm> supplierTmTreeItem) {
                        boolean search=supplierTmTreeItem.getValue().getSupplierId().contains(newValue) ||
                                supplierTmTreeItem.getValue().getSupplierName().contains(newValue);
                        return search;
                    }
                });
            }
        });
    }

    private void setData(TreeItem<SupplierTm> value) {
        txtSupplierID.setText(value.getValue().getSupplierId());
        cmdSupplierTitle.setValue(value.getValue().getTitle());
        txtSupplierName.setText(value.getValue().getSupplierName());
        txtSupplierContact.setText(value.getValue().getContact());
        txtSupplierCompany.setText(value.getValue().getCompany());
        txtSupplierEmail.setText(value.getValue().getEmail());
    }

    public static Supplier searchSupplierById(String supId) throws SQLException, ClassNotFoundException {
        ResultSet rst=DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * From Supplier where supplierId='"+supId+"'");
        return rst.next() ? new Supplier(supId,rst.getString("title"),rst.getString("supplierName"),rst.getString("contact"),rst.getString("company"),rst.getString("email")):null;
    }
    public static Supplier searchSupplierByName(String supplierName) throws SQLException, ClassNotFoundException {
        ResultSet rst=DBConnection.getInstance().getConnection().createStatement().executeQuery("Select * From Supplier where supplierName='"+supplierName+"'");
        return rst.next() ? new Supplier(rst.getString("supplierId"),rst.getString("title"),supplierName,rst.getString("contact"),rst.getString("company"),rst.getString("email")):null;
    }


}
