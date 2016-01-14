package com.quantaconsultoria.docgem;

import java.io.IOException;

import com.quantaconsultoria.docgem.bags.DocumentationBag;


public interface Builder {

	void saveDocumentationInfo(DocumentationBag documentation) throws IOException;

	void copyResources();

	String generateFileDescription(String path, DocumentationConfiguration configuration) throws IOException;;

}
