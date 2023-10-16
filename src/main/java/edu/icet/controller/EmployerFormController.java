package edu.icet.controller;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.icet.db.DBConnection;
import edu.icet.model.Employer;
import edu.icet.model.tm.EmployerTm;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class EmployerFormController implements Initializable {
    public JFXTextField txtEmplID;
    public JFXTextField txtEmplNic;
    public JFXTextField txtEmplAddress;
    public JFXTextField txtBankBranch;
    public JFXTextField txtBankAccNo;
    public JFXComboBox cmdTitle;
    public JFXTextField txtEmplContact;
    public JFXTextField txtEmplSearch;
    public JFXTextField txtEmplName;
    public DatePicker txtDob;
    private Parent parent;
    private Stage stage;
    private Scene scene;
    @FXML
    private TreeTableColumn colEmplAddress;

    @FXML
    private TreeTableColumn colEmplBankAccNo;

    @FXML
    private TreeTableColumn colEmplBankBranch;

    @FXML
    private TreeTableColumn colEmplContact;

    @FXML
    private TreeTableColumn colEmplDob;

    @FXML
    private TreeTableColumn colEmplID;

    @FXML
    private TreeTableColumn colEmplName;

    @FXML
    private TreeTableColumn colEmplNic;

    @FXML
    private TreeTableColumn colEmplOtion;

    @FXML
    private TreeTableColumn colEmplTitle;

    @FXML
    private JFXTreeTableView<EmployerTm> tblEmployer;

    public void btnBackOnAction(ActionEvent actionEvent) {
        try {
            parent = FXMLLoader.load(getClass().getResource("/view/home_page_form.fxml"));
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static String getLastSupplierId() throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        Statement stm = connection.createStatement();
        ResultSet rst = stm.executeQuery("SELECT empId FROM Employer ORDER BY empId DESC LIMIT 1");
        return rst.next() ? rst.getString("empId") : null;
    }

    private void generateId() {
        try {
            String lastSupplierId = getLastSupplierId();
            if (lastSupplierId != null && lastSupplierId.matches("EMP-\\d{4}")) {
                int numericPart = Integer.parseInt(lastSupplierId.substring(4)) + 1;
                String newSupplierId = String.format("EMP-%04d", numericPart);
                txtEmplID.setText(newSupplierId);
            } else {
                txtEmplID.setText("EMP-0001");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnEmplClearOnAction(ActionEvent actionEvent) {
        txtEmplID.setText("");
        cmdTitle.setValue(null);
        txtEmplName.setText("");
        txtEmplNic.setText("");
        txtDob.getEditor().clear();
        txtEmplAddress.setText("");
        txtBankAccNo.setText("");
        txtBankBranch.setText("");
        txtEmplContact.setText("");
        generateId();
    }

    public void btnEmplSaveOnAction(ActionEvent actionEvent) {
        try {
            Employer employer = new Employer(
                    txtEmplID.getText(),
                    cmdTitle.getSelectionModel().getSelectedItem().toString(),
                    txtEmplName.getText(),
                    txtEmplNic.getText(),
                    txtDob.getValue(),
                    txtEmplAddress.getText(),
                    txtBankAccNo.getText(),
                    txtBankBranch.getText(),
                    txtEmplContact.getText()
            );
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("Insert into Employer Values(?,?,?,?,?,?,?,?,?)");
            pstm.setObject(1, employer.getEmpId());
            pstm.setObject(2, employer.getTitle());
            pstm.setObject(3, employer.getName());
            pstm.setObject(4, employer.getNic());
            pstm.setObject(5, employer.getDateOfBirth());
            pstm.setObject(6, employer.getAddress());
            pstm.setObject(7, employer.getBankAccNo());
            pstm.setObject(8, employer.getBankBranch());
            pstm.setObject(9, employer.getContactNo());
            if (pstm.executeUpdate() > 0) {
                new Alert(Alert.AlertType.CONFIRMATION, "Success").show();
                txtEmplID.setText("");
                cmdTitle.setValue(null);
                txtEmplName.setText("");
                txtEmplNic.setText("");
                txtDob.getEditor().clear();
                txtEmplAddress.setText("");
                txtBankAccNo.setText("");
                txtBankBranch.setText("");
                txtEmplContact.setText("");
                generateId();
                loadTable();

            }
        } catch (SQLException | ClassNotFoundException | NullPointerException e) {
            new Alert(Alert.AlertType.ERROR, "No data to save").show();
//            e.printStackTrace();

        }

    }

    private void loadTable() {
        ObservableList<EmployerTm> tmObservableList = FXCollections.observableArrayList();
        try {
            List<Employer> list = new ArrayList<>();
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM employer");
            ResultSet resultSet = pstm.executeQuery();

            while (resultSet.next()) {
                list.add(new Employer(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getDate(5).toLocalDate(),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(9)
                ));
            }
            for (Employer employer : list) {
                JFXButton btnDelete = new JFXButton("Delete");
                btnDelete.setBackground(Background.fill(Color.rgb(0, 0, 0)));
                btnDelete.setTextFill(Color.rgb(255,255,255));

                btnDelete.setOnAction(actionEvent -> {
                    try {
                        PreparedStatement pstm1 = connection.prepareStatement("DELETE FROM employer WHERE empId=?");
                        pstm1.setString(1,employer.getEmpId());
                        Optional<ButtonType> buttonType = new Alert(Alert.AlertType.CONFIRMATION, "Do you want delete " + employer.getEmpId() + " employer ?", ButtonType.YES, ButtonType.NO).showAndWait();
                        if(buttonType.get()== ButtonType.YES){
                            if(pstm1.executeUpdate()>0){
                                loadTable();
                                new Alert(Alert.AlertType.INFORMATION,"Employer Deleted...!").show();

                            }else{
                                new Alert(Alert.AlertType.ERROR,"Something went wrong...!").show();
                            }
                        }

                    } catch (SQLException e) {
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    }
                });

                tmObservableList.add(new EmployerTm(
                        employer.getEmpId(),
                        employer.getTitle(),
                        employer.getName(),
                        employer.getNic(),
                        employer.getDateOfBirth(),
                        employer.getAddress(),
                        employer.getBankAccNo(),
                        employer.getBankBranch(),
                        employer.getContactNo(),
                        btnDelete
                        ));
            }
            TreeItem<EmployerTm> treeItem=new RecursiveTreeItem<>(tmObservableList, RecursiveTreeObject::getChildren);
            tblEmployer.setRoot(treeItem);
            tblEmployer.setShowRoot(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colEmplID.setCellValueFactory(new TreeItemPropertyValueFactory<>("empId"));
        colEmplTitle.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        colEmplName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colEmplNic.setCellValueFactory(new TreeItemPropertyValueFactory<>("nic"));
        colEmplDob.setCellValueFactory(new TreeItemPropertyValueFactory<>("dateOfBirth"));
        colEmplAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>("address"));
        colEmplBankAccNo.setCellValueFactory(new TreeItemPropertyValueFactory<>("bankAccNo"));
        colEmplBankBranch.setCellValueFactory(new TreeItemPropertyValueFactory<>("bankBranch"));
        colEmplContact.setCellValueFactory(new TreeItemPropertyValueFactory<>("contactNo"));
        colEmplOtion.setCellValueFactory(new TreeItemPropertyValueFactory<>("btnDelete"));
        generateId();
        loadTable();
        cmdTitle.getItems().addAll("Mr", "Mrs", "Miss");

      tblEmployer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) ->{
          if (newValue!=null){
              setData(newValue);
          }
      } );
      txtEmplSearch.textProperty().addListener(new ChangeListener<String>() {
          @Override
          public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
              tblEmployer.setPredicate(new Predicate<TreeItem<EmployerTm>>() {
                  @Override
                  public boolean test(TreeItem<EmployerTm> employerTmTreeItem) {
                      boolean search=employerTmTreeItem.getValue().getEmpId().contains(newValue) ||
                              employerTmTreeItem.getValue().getName().contains(newValue);
                      return search;
                  }
              });
          }
      });
    }

    private void setData(TreeItem<EmployerTm> value) {
        txtEmplID.setText(value.getValue().getEmpId());
        cmdTitle.setValue(value.getValue().getTitle());
        txtEmplName.setText(value.getValue().getName());
        txtEmplNic.setText(value.getValue().getNic());
        txtDob.setValue(value.getValue().getDateOfBirth());
        txtEmplAddress.setText(value.getValue().getAddress());
        txtBankAccNo.setText(value.getValue().getBankAccNo());
        txtBankBranch.setText(value.getValue().getBankBranch());
        txtEmplContact.setText(value.getValue().getContactNo());
    }


    public void txtSearchOnAction(ActionEvent actionEvent) {

    }
}
