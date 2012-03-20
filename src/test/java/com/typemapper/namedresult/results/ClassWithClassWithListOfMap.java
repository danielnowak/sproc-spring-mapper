package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

import java.util.List;

/**
 * @author Ingolf Wagner <ingolf.wagner@zalando.de>
 */
public class ClassWithClassWithListOfMap {

    @DatabaseField(name = "aa")
    private ClassWithListOfMap classWithListOfMap;

    @DatabaseField(name = "bb")
    private  String string;

    

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public ClassWithListOfMap getClassWithListOfMap() {
        return classWithListOfMap;
    }

    public void setClassWithListOfMap(ClassWithListOfMap classWithListOfMap) {
        this.classWithListOfMap = classWithListOfMap;
    }
}
