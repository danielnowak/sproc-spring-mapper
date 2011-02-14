package com.typemapper.namedresult.results;

public enum GenderCodeEnum {
    
    MALE("male"),
    FEMALE("female");
    
    private String code;
    private static final GenderCodeEnum[] copyOfValues = values();
    
    private GenderCodeEnum(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static GenderCodeEnum getCode(final String code) {
        for (GenderCodeEnum value : copyOfValues) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
    
}
