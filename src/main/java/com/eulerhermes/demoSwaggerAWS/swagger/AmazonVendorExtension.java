package com.eulerhermes.demoSwaggerAWS.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Two ways of defining extensions:
 * - Annotate the method, so the definition will be applied to this method only.
 * - Annotate the class and the definition will be applied to all methods of this class.
 *
 * In case the same property is defined at class level and method level, only the method definition will be kept.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface  AmazonVendorExtension {

    /**
     * A file path containing the definition of the extension to apply to the swagger definition of the method or class.
     * JSON format required
     */
    String definitionFilePath() default "";

    /**
     * The definition of the extension to apply to the swagger definition of the method or class.
     */
    String jsonDefinition() default "";
}
