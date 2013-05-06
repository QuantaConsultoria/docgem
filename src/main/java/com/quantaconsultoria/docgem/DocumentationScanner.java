package com.quantaconsultoria.docgem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.quantaconsultoria.docgem.annotations.Charpter;
import com.quantaconsultoria.docgem.annotations.Section;

public class DocumentationScanner {

	private Map<String, Charpter> classesMapped;
	private Map<String, Section> methodsMapped;
	
	public DocumentationScanner() {
		classesMapped = new HashMap<String, Charpter>();
		methodsMapped = new HashMap<String, Section>();
	}
	
	public void scan() {
		
		Reflections reflections = new Reflections("com.quantaconsultoria.docgem.test", new TypeAnnotationsScanner());
		Set<Class<?>> testedCharpters = reflections.getTypesAnnotatedWith(Charpter.class);
		
		for (final Class<?> testedCharpter : testedCharpters) {
			Charpter charpter = testedCharpter.getAnnotation(
					Charpter.class);
			classesMapped.put(testedCharpter.getCanonicalName(), charpter);
		}
	}

	public boolean existCharpter(StackTraceElement element) {
		return classesMapped.containsKey(element.getClassName());
	}

	public Charpter getCharpter(StackTraceElement element) {
		return classesMapped.get(element.getClassName());
	}

	public boolean existSection(StackTraceElement element, Charpter charpter) {
		// TODO Auto-generated method stub
		return false;
	}

	public Section getSection(StackTraceElement element, Charpter charpter) {
		// TODO Auto-generated method stub
		return null;
	}
}
