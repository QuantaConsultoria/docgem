package com.quantaconsultoria.docgem;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import com.quantaconsultoria.docgem.annotations.Action;
import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;

public class DocumentationScanner {

	private Map<String, Chapter> classesMapped;
	private Map<Method, Section> methodsMappedWithSections;
	private Map<Method, Action> methodsMappedWithActions;
	private DocumentationConfiguration configuration;
	
	public DocumentationScanner(DocumentationConfiguration configuration) {
		classesMapped = new HashMap<String, Chapter>();
		methodsMappedWithSections = new HashMap<Method, Section>();
		methodsMappedWithActions = new HashMap<Method, Action>();
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
				Action action = method.getAnnotation(Action.class);
				if (section!=null) {
					methodsMappedWithSections.put(method, section);
				}
				if(action != null) {
					methodsMappedWithActions.put(method, action);
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
		return methodsMappedWithSections.containsKey(method);	
	}

	public Section getSection(Method method) {
		return methodsMappedWithSections.get(method);
	}

	public boolean existAction(Method method) {
		return methodsMappedWithActions.containsKey(method);	
	}
	
	public Action getAction(Method method) {
		return methodsMappedWithActions.get(method);
	}
}
