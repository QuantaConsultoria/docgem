package com.quantaconsultoria.docgem.factory;

import com.quantaconsultoria.docgem.Builder;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.repository.InformationRepository;


public interface Factory {
	
	Builder getBuilder();
	
	InformationRepository getRepository();
	
	DocumentationConfiguration getConfiguration();

}
