package com.company.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import com.company.model.ImageRelation.*;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.ComplexType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RelationAnnotation {
    SimpleAttributeType type() default SimpleAttributeType.NONE;
    Class<? extends Enum> complexType() default Enum.class;
    Class<? extends Enum> classe() default Enum.class;

}
