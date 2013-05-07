package com.quantaconsultoria.docgem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.quantaconsultoria.docgem.annotations.Charpter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.reflections.ReflectionsUtil;

public class DocumentationScanner {

	private Map<String, Charpter> classesMapped;
	private Map<Method, Section> methodsMapped;
	private DocumentationConfiguration configuration;
	
	public DocumentationScanner(DocumentationConfiguration configuration) {
		classesMapped = new HashMap<String, Charpter>();
		methodsMapped = new HashMap<Method, Section>();
		this.configuration = configuration;
	}
	
	public void scan() {
		
		Reflections reflections = new Reflections(configuration.getPackagePrefix(), new TypeAnnotationsScanner());
		Set<Class<?>> testedCharpters = reflections.getTypesAnnotatedWith(Charpter.class);
		
		for (final Class<?> testedCharpter : testedCharpters) {
			Charpter charpter = testedCharpter.getAnnotation(
					Charpter.class);
			classesMapped.put(testedCharpter.getCanonicalName(), charpter);
			for(Method method : testedCharpter.getMethods()) {
				Section section = method.getAnnotation(Section.class); 
				if (section!=null) {
					methodsMapped.put(method, section);
				}
			}
		}
	}

	public boolean existCharpter(StackTraceElement element) {
		return classesMapped.containsKey(element.getClassName());
	}

	public Charpter getCharpter(StackTraceElement element) {
		return classesMapped.get(element.getClassName());
	}

	public boolean existSection(Method method) {
		return methodsMapped.containsKey(method);	
	}

	public Section getSection(Method method) {
		return methodsMapped.get(method);
	}
}
