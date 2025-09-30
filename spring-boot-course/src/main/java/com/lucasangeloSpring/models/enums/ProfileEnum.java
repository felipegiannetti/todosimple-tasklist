package com.lucasangeloSpring.models.enums;

import java.util.Objects;

public enum ProfileEnum {
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER"); // salva o numero no banco ao inves da escrita

    private final Integer code;
    private final String description;

    ProfileEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static ProfileEnum toEnum(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }

        for (ProfileEnum profile : ProfileEnum.values()) {
            if (code.equals(profile.getCode())) {
                return profile;
            }
        }

        throw new IllegalArgumentException("Id inválido: " + code);
    }
}
