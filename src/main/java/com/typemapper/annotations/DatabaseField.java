package com.typemapper.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.typemapper.core.fieldMapper.AnyTransformer;
import com.typemapper.core.fieldMapper.ValueTransformer;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DatabaseField {
	
	String name() default "";
	Class<? extends ValueTransformer<String, ?>> transformer() default AnyTransformer.class;
}
