package com.typemapper.namedresult.results;

import java.io.File;

import java.util.Arrays;
import java.util.List;

import com.typemapper.annotations.DatabaseField;

import com.typemapper.namedresult.transformer.FileTransformer;
import com.typemapper.namedresult.transformer.GenderCodeIntegerTransformer;
import com.typemapper.namedresult.transformer.GenderCodeTransformer;
import com.typemapper.namedresult.transformer.StringListTransformer;

public class ClassWithSimpleTransformers {

    @DatabaseField(name = "gender_as_code", transformer = GenderCodeTransformer.class)
    private GenderCode genderCode;

    @DatabaseField(name = "gender_as_int", transformer = GenderCodeIntegerTransformer.class)
    private GenderCode genderCodeAsInt;

    @DatabaseField(name = "gender_as_name")
    private GenderCode genderCodeAsName;

    @DatabaseField(name = "file_column", transformer = FileTransformer.class)
    private File file;

    @DatabaseField(name = "string_list_with_separtion_char", transformer = StringListTransformer.class)
    private List<String> stringList;

    public ClassWithSimpleTransformers() { }

    public ClassWithSimpleTransformers(final GenderCode genderCode, final GenderCode genderCodeAsInt,
            final GenderCode genderCodeAsName, final String file, final String... stringList) {
        this.genderCode = genderCode;
        this.genderCodeAsInt = genderCodeAsInt;
        this.genderCodeAsName = genderCodeAsName;
        this.file = new File(file);

        this.stringList = Arrays.asList(stringList);
    }

    public GenderCode getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(final GenderCode genderCode) {
        this.genderCode = genderCode;
    }

    public GenderCode getGenderCodeAsInt() {
        return genderCodeAsInt;
    }

    public void setGenderCodeAsInt(final GenderCode genderCodeAsInt) {
        this.genderCodeAsInt = genderCodeAsInt;
    }

    public GenderCode getGenderCodeAsName() {
        return genderCodeAsName;
    }

    public void setGenderCodeAsName(final GenderCode genderCodeAsName) {
        this.genderCodeAsName = genderCodeAsName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(final File file) {
        this.file = file;
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(final List<String> stringList) {
        this.stringList = stringList;
    }
}
