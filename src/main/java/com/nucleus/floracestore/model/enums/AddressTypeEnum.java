package com.nucleus.floracestore.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum AddressTypeEnum {
    NONE("No address type specified."),
    INVOICE("The address used on an invoice."),
    DELIVERY("A delivery address."),
    ALT_DLV("An alternative delivery address."),
    SWIFT("A SWIFT address."),
    PAYMENT("A payment address."),
    SERVICE("A service address."),
    HOME("A home address."),
    OTHER("An address not covered by other types (i.e. other)."),
    BUSINESS("A business address."),
    REMIT_TO("A remit to address."),
    SHIP_CARRIER_THIRD_PARTY_SHIPPING("A shipping carrier address."),
    STATEMENT("A statement address."),
    FIXED_ASSET("A fixed asset address."),
    ONE_TIME("A one-time address."),
    RECRUIT("A recruit address."),
    SMS("The SMS address."),
    LANDING_W("The lading address for RU,LT,LV."),
    UN_LANDING_W("The unlading address for RU,LT,LV."),
    CONSIGNMENT_IN("The consignment address for IN.");

    private final String addressTypeDescription;
    private static final Map<String, String> map = new HashMap<>();

    AddressTypeEnum(String addressTypeDescription) {
        this.addressTypeDescription = addressTypeDescription;
    }

    public String getAddressTypeDescription() {
        return addressTypeDescription;
    }
}
