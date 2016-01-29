package com.company.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.company.model.ImageRelation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RelationAnnotation {
    AnnotationType type() default AnnotationType.SINGLE_ENUM;
    ARFFType singleType() default ARFFType.NONE;
    Class<? extends Enum> multipleType() default Enum.class;
    String singleStringType() default "";
    String[] multipleArrayType() default {};
    boolean isClass() default false;
    boolean isDynamic() default false;
}
