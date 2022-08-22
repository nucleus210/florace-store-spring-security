package com.nucleus.floracestore.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductStatusEnum {

    BACKORDER("BackOrder"),
    DISCOUNTED("Discontinued"),
    IN_STOCK("InStock"),
    IN_STORE_ONLY("InStoreOnly"),
    LIMITED_AVAILABILITY("LimitedAvailability"),
    ON_LINE_ONLY("OnlineOnly"),
    OUT_OF_STOCK("OutOfStock"),
    PRE_ORDERED("Algeria"),
    PRE_SALE("PreSale"),
    SOLD_OUT("SoldOut");;

    private final String name;
    private static final Map<String, String> map = new HashMap<>();

    ProductStatusEnum(String displayValue) {
        this.name = displayValue;
    }

    public String getDisplayValue() {
        return name;
    }

}
