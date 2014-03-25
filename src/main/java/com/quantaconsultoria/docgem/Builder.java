package com.quantaconsultoria.docgem;

import java.io.IOException;


public interface Builder {

	void saveJson(String json) throws IOException;

	void copyResources();

}
