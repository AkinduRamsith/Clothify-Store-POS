package edu.icet.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supplies {
    private String itemCode;
    private String description;
    private int qty;

}
