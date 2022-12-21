package com.nucleus.floracestore.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum ProductStatusEnum {

    BACKORDER("Back-ordered", "Product that is back-ordered (i.e. temporarily out of stock).", 1),
    DISCOUNTED("Discontinued", "Product that has been discounted.", 2),
    IN_STOCK("In stock", "Product in stock.", 3),
    IN_STORE_ONLY("In store only", "Product can only be ordered in store.", 4),
    LIMITED_AVAILABILITY("Limited availability", "Product has limited availability.", 5),
    ON_LINE_ONLY("Online only", "Product can order on-line only.", 6),
    OUT_OF_STOCK("Out of stock", "Product out of stock.", 7),
    PRE_ORDERED("Pre ordered", " Product that is in pre-order state.", 8),
    PRE_SALE("Pre sale", " Product that is in pre-sale state.", 9),
    SOLD_OUT("Sold out", "Product that is out of stock.", 10);

    private final String levelName;
    private final String levelDescription;
    private final int levelCode;

    private static final Map<String, String> map = new HashMap<>();

    ProductStatusEnum(String levelName, String levelDescription, int levelCode) {
        this.levelName = levelName;
        this.levelDescription = levelDescription;
        this.levelCode = levelCode;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelDescription() {
        return levelDescription;
    }

    public int getLevelCode() {
        return levelCode;
    }
}
