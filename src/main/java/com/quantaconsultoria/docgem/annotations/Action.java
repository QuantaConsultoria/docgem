package com.quantaconsultoria.docgem.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Action {
	String text();
	String image() default "";
}
