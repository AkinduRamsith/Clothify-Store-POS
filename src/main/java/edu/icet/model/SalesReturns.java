package edu.icet.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalesReturns {
    private String returnId;
    private String orderId;
    private double total;
    private String date;
}
