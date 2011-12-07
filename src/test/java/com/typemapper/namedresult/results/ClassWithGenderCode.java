package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;
import com.typemapper.namedresult.transformer.GenderCodeTransformer;

public class ClassWithGenderCode {

    @DatabaseField(name = "male_female", transformer = GenderCodeTransformer.class)
    private GenderCode genderCode;

    public GenderCode getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(final GenderCode genderCode) {
        this.genderCode = genderCode;
    }
    
}
