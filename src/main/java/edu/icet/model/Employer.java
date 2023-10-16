package edu.icet.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employer {
    private String empId;
    private String title;
    private String name;
    private String nic;
    private LocalDate dateOfBirth;
    private String address;
    private String bankAccNo;
    private String bankBranch;
    private String contactNo;
}
