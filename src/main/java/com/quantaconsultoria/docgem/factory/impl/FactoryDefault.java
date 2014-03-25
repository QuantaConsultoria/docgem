package com.quantaconsultoria.docgem.factory.impl;

import com.quantaconsultoria.docgem.Builder;
import com.quantaconsultoria.docgem.DocumentationConfiguration;
import com.quantaconsultoria.docgem.factory.Factory;
import com.quantaconsultoria.docgem.format.html.HtmlBuilder;
import com.quantaconsultoria.docgem.repository.InformationRepository;
import com.quantaconsultoria.docgem.repository.impl.InformationRepositoryDefault;

public class FactoryDefault implements Factory {
	
	private DocumentationConfiguration configuration;
	

	public FactoryDefault(DocumentationConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Builder getBuilder() {
		return new HtmlBuilder(configuration);
	}

	@Override
	public InformationRepository getRepository() {
		return new InformationRepositoryDefault(configuration);
	}

	@Override
	public DocumentationConfiguration getConfiguration() {
		return configuration;
	}

}
