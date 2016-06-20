package org.eclipse.vorto.server.devtool.utils;

import org.eclipse.vorto.core.api.model.model.ModelType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonParser;

@Component
public class InformationModelEditorUtils {

	private String basePath = "http://vorto.eclipse.org//rest/";

	public String downloadModelFile(String namespace, String name, String version) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "model/file/{namespace}/{name}/{version}?output=DSL",
					String.class, namespace, name, version);
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		}
	}

	public String downloadModelAsString(String namespace, String name, String version) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			return restTemplate.getForObject(basePath + "model/{namespace}/{name}/{version}", String.class, namespace,
					name, version);
		} catch (RestClientException e) {
			throw new RuntimeException(e);
		}
	}

	public ModelType getModelType(String namespace, String name, String version) {
		String modelString = downloadModelAsString(namespace, name, version);
		JsonParser jsonParser = new JsonParser();
		String modelType = jsonParser.parse(modelString).getAsJsonObject().get("modelType").getAsString();
		if (modelType.equalsIgnoreCase(ModelType.Datatype.toString())) {
			return ModelType.Datatype;
		} else if (modelType.equalsIgnoreCase(ModelType.Functionblock.toString())) {
			return ModelType.Functionblock;
		} else if (modelType.equalsIgnoreCase(ModelType.InformationModel.toString())) {
			return ModelType.InformationModel;
		} else if (modelType.equalsIgnoreCase(ModelType.Mapping.toString())) {
			return ModelType.Mapping;
		} else {
			throw new UnsupportedOperationException("Given ModelType is unknown");
		}
	}

	public String searchByExpression(String expression) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(basePath + "model/query=" + expression, String.class);
	}
}
