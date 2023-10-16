package edu.icet.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {
    private String itemCode;
    private String description;
    private int qtyOnHand;
    private double sellingPrice;
    private double buyingPrice;
    private String type;
    private String size;
    private String supplierId;

}
