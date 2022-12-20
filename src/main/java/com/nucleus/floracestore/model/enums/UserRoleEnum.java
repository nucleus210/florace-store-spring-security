package com.nucleus.floracestore.model.enums;

import java.util.HashMap;
import java.util.Map;

public enum UserRoleEnum {
    ADMIN("Admin", 1),
    STAFF("Staff", 2),
    USER("User", 3),
    GUEST("Guest", 4),
    FACEBOOK_USER("FacebookUser", 5);

    private final String levelName;
    private final int levelCode;
    private static final Map<String, Integer> map = new HashMap<>();


    UserRoleEnum(String name, int levelCode) {
        this.levelName = name;
        this.levelCode = levelCode;
    }

    static {
        for (UserRoleEnum userLevelEnum : values()) {
            map.put(userLevelEnum.levelName, userLevelEnum.levelCode);
        }
    }

    public static String valueOf(int levelCode) {
        return map.get(levelCode).toString();
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevelCode() {
        return levelCode;
    }

    public UserRoleEnum fromString(String text) {
        for (UserRoleEnum userRoleEnum : UserRoleEnum.values()) {
            if (userRoleEnum.levelName.equalsIgnoreCase(text)) {
                return userRoleEnum;
            }
        }
        return null;
    }
}