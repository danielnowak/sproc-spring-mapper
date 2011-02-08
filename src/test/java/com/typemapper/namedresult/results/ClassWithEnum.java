package com.typemapper.namedresult.results;

import com.typemapper.annotations.DatabaseField;

public class ClassWithEnum {

    @DatabaseField(name = "male_female")
    private SampleEnum sampleEnum;

    public SampleEnum getSampleEnum() {
        return sampleEnum;
    }

    public void setSampleEnum(SampleEnum sampleEnum) {
        this.sampleEnum = sampleEnum;
    }
    
}
