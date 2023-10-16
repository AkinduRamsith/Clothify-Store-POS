package edu.icet.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplier {
    private String supplierId;
    private String title;
    private String supplierName;
    private String contact;
    private String company;
    private String email;
}
