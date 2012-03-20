package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

/**
 * @author Ingolf Wagner <ingolf.wagner@zalando.de>
 */
public class ClassWithClassWithMap {
    @DatabaseField(name = "aa")
    private ClassWithMap classWithMap;

    @DatabaseField(name = "bb")
    private String string;


    public ClassWithMap getClassWithMap() {
        return classWithMap;
    }

    public void setClassWithMap(ClassWithMap classWithMap) {
        this.classWithMap = classWithMap;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
