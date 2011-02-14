package com.typemapper.namedresult.results;

import com.typemapper.adapter.GenderCodeEnumValueExtractor;
import com.typemapper.annotations.DatabaseField;

public class ClassWithGenderCodeEnum {

    @DatabaseField(name = "male_female", adapter = GenderCodeEnumValueExtractor.class)
    private GenderCodeEnum gender;

    public GenderCodeEnum getGender() {
        return gender;
    }

    public void setGender(GenderCodeEnum gender) {
        this.gender = gender;
    }

}
