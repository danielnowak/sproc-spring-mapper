package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;
import com.typemapper.annotations.Embed;

public class ClassWithEmbedEnumClass {

    @Embed
    private ClassWithEnum embeddedEnum;

    @DatabaseField(name = "str")
    private String str;

    public ClassWithEnum getEmbeddedEnum() {
        return embeddedEnum;
    }

    public void setEmbeddedEnum(final ClassWithEnum embeddedEnum) {
        this.embeddedEnum = embeddedEnum;
    }

    public String getStr() {
        return str;
    }

    public void setStr(final String str) {
        this.str = str;
    }

}
