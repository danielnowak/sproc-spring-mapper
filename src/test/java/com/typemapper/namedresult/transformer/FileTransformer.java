package com.typemapper.namedresult.transformer;

import java.io.File;

import com.typemapper.core.ValueTransformer;

public class FileTransformer extends ValueTransformer<String, File> {

    @Override
    public File unmarshalFromDb(final String value) {
        return new File(value);
    }

    @Override
    public String marshalToDb(final File bound) {
        return bound.getPath();
    }
}
