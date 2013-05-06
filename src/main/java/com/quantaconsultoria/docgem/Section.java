package com.quantaconsultoria.docgem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
public @interface Section {
	String id();
	Charpter charpter() default @Charpter(id="__not_in_use__");
}
