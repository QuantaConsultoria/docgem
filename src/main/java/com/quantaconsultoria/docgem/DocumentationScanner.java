package com.quantaconsultoria.docgem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;

public class DocumentationScanner {

	private Map<String, Chapter> classesMapped;
	private Map<Method, Section> methodsMapped;
	private DocumentationConfiguration configuration;
	
	public DocumentationScanner(DocumentationConfiguration configuration) {
		classesMapped = new HashMap<String, Chapter>();
		methodsMapped = new HashMap<Method, Section>();
		this.configuration = configuration;
	}
	
	public void scan() {
		
		Reflections reflections = new Reflections(configuration.getPackagePrefix(), new TypeAnnotationsScanner());
		Set<Class<?>> testedChapters = reflections.getTypesAnnotatedWith(Chapter.class);
		
		for (final Class<?> testedChapter : testedChapters) {
			Chapter chapter = testedChapter.getAnnotation(
					Chapter.class);
			classesMapped.put(testedChapter.getCanonicalName(), chapter);
			for(Method method : testedChapter.getMethods()) {
				Section section = method.getAnnotation(Section.class); 
				if (section!=null) {
					methodsMapped.put(method, section);
				}
			}
		}
	}

	public boolean existChapter(StackTraceElement element) {
		return classesMapped.containsKey(element.getClassName());
	}

	public Chapter getChapter(StackTraceElement element) {
		return classesMapped.get(element.getClassName());
	}

	public boolean existSection(Method method) {
		return methodsMapped.containsKey(method);	
	}

	public Section getSection(Method method) {
		return methodsMapped.get(method);
	}
}
