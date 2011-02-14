package com.typemapper.namedresult.results;

import com.typemapper.adapter.GenderEnumValueExtractor;
import com.typemapper.annotations.DatabaseField;

public class ClassWithGenderEnum {

    @DatabaseField(name = "male_female", adapter = GenderEnumValueExtractor.class)
    private GenderEnum gender;

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }
    
}
