package com.quantaconsultoria.docgem.repository;

import java.io.IOException;
import java.util.Map;

import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.*;

public interface InformationRepository {

	void saveInfoAction(Chapter chapter, Section section, ActionBag action)	throws IOException;

	DocumentationBag readDocumentationInfo() throws IOException;

	Map<String, ChapterBag> readActions() throws IOException;

}
