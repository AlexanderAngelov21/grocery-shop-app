package com.cloudruid.groceryshoptill.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "deals")
public class Deal {
    public enum DealType {
        TWO_FOR_THREE, BUY_ONE_GET_ONE_HALF_PRICE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DealType type;

    @ElementCollection
    private List<String> applicableProducts;
}