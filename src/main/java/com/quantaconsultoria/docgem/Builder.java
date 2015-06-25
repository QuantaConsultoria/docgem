package com.quantaconsultoria.docgem;

import java.io.IOException;
import java.util.List;

import com.quantaconsultoria.docgem.bags.ChapterBag;


public interface Builder {

	void saveDocumentationInfo(List<ChapterBag> chapters) throws IOException;

	void copyResources();

}
