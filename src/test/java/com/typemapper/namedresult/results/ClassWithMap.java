package com.typemapper.namedresult.results;

import java.util.Map;

import com.typemapper.annotations.DatabaseField;

public class ClassWithMap {

    @DatabaseField(name = "str")
    private String str;

    @DatabaseField(name = "map")
    private Map<String, String> map;

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(final Map<String, String> map) {
        this.map = map;
    }

    public String getStr() {
        return str;
    }

    public void setStr(final String str) {
        this.str = str;
    }

}
