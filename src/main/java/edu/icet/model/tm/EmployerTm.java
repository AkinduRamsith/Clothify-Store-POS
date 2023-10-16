package edu.icet.model.tm;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployerTm extends RecursiveTreeObject<EmployerTm> {
    private String empId;
    private String title;
    private String name;
    private String nic;
    private LocalDate dateOfBirth;
    private String address;
    private String bankAccNo;
    private String bankBranch;
    private String contactNo;
    private JFXButton btnDelete;
}
