package edu.icet.model.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SupplierTm extends RecursiveTreeObject<SupplierTm> {
    private String supplierId;
    private String title;
    private String supplierName;
    private String contact;
    private String company;
    private String email;
    private JFXButton btnDelete;
}
