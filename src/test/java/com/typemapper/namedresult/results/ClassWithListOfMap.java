package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

import java.util.List;
import java.util.Map;

/**
 * @author Ingolf Wagner <ingolf.wagner@zalando.de>
 */
public class ClassWithListOfMap {

    @DatabaseField(name = "map_array")
    private  List<Map<String, String>> mapList;

    @DatabaseField(name = "str")
   private String str;

    public List<Map<String, String>> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map<String, String>> mapList) {
        this.mapList = mapList;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
