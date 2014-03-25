package com.quantaconsultoria.docgem.repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.quantaconsultoria.docgem.annotations.Chapter;
import com.quantaconsultoria.docgem.annotations.Section;
import com.quantaconsultoria.docgem.bags.ActionBag;
import com.quantaconsultoria.docgem.bags.ChapterBag;

public interface InformationRepository {

	void saveInfoAction(Chapter chapter, Section section, ActionBag action)	throws IOException;

	List<ChapterBag> readSortedChapters() throws IOException;

	Map<String, ChapterBag> readActions() throws IOException;

}
