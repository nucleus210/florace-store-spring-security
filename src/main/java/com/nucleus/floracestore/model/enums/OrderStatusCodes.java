package com.nucleus.floracestore.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusCodes {
    DRAFT("Draft", "Indicates that the Order has just been created but no approval has been requested yet.", 0),
    PENDING_APPROVAL("Pending approval", "Indicates that a request for approval for the Order has been made.", 1),
    APPROVED("Approved", "Indicates that the Order has been approved and is ready to serve.", 2),
    DISAPPROVED("Disapproved", "Indicates that the Order has been disapproved and is not eligible to serve.", 3),
    PAUSED("Paused", "This is a legacy state. Paused status should be checked on LineItems within the order.", 4),
    CANCELED("Canceled", "Indicates that the Order has been canceled and cannot serve. ", 5),
    DELETED("Deleted", "Indicates that the Order has been deleted by DSM. ", 6),
    UNKNOWN("Unknown", "The value returned if the actual value is not exposed by the requested API version.", 7);


    private final String levelName;
    private final String levelDescription;
    private final int levelCode;
    private static final Map<String, String> map = new HashMap<>();

    OrderStatusCodes(String levelName, String levelDescription, int levelCode) {
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
