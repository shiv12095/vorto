package org.eclipse.vorto.server.devtool.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.editor.infomodel.web.resource.InformationModelResourceSetProvider;
import org.eclipse.vorto.server.devtool.utils.InformationModelEditorUtils;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.inject.Injector;

@Service
public class InformationModelEditorServiceImpl implements IInformationModelEditorService {

	@Autowired
	InformationModelEditorUtils informationModelEditorUtils;

	@Autowired
	Injector injector;

	@Override
	public String searchFunctionBlockByExpression(String expression) {
		String results = informationModelEditorUtils.searchByExpression(expression);
		JsonParser jsonParser = new JsonParser();
		JsonArray jsonArray = jsonParser.parse(results).getAsJsonArray();
		JsonArray functionBlockJsonArray = new JsonArray();
		for (JsonElement jsonElement : jsonArray) {
			if (ModelType.Functionblock.toString()
					.equalsIgnoreCase(jsonElement.getAsJsonObject().get("modelType").getAsString())) {
				functionBlockJsonArray.add(jsonElement);
			}
		}
		return functionBlockJsonArray.toString();
	}

	@Override
	public void addFunctionBlockToResourceSet(String namespace, String name, String version,
			HttpServiceContext httpServiceContext) {
		ModelType modelType = informationModelEditorUtils.getModelType(namespace, name, version);
		if (modelType.equals(ModelType.Functionblock)) {
			String fileName = createFileName(namespace, name, version, modelType);

			IWebResourceSetProvider webResourceSetProvider = injector.getInstance(IWebResourceSetProvider.class);
			InformationModelResourceSetProvider informationModelResourceSetProvider = (InformationModelResourceSetProvider) webResourceSetProvider;
			HashSet<String> referencedResourceSet = (HashSet<String>) informationModelResourceSetProvider
					.getReferencedResourcesFromSession(httpServiceContext);

			if (!referencedResourceSet.contains(fileName)) {
				addModelToResourceSet(namespace, name, version, fileName, httpServiceContext);
			}
		}
	}

	private void addModelToResourceSet(String namespace, String name, String version, String fileName,
			HttpServiceContext serviceContext) {
		String fileContents = informationModelEditorUtils.downloadModelFile(namespace, name, version);

		IWebResourceSetProvider webResourceSetProvider = injector.getInstance(IWebResourceSetProvider.class);
		InformationModelResourceSetProvider informationModelResourceSetProvider = (InformationModelResourceSetProvider) webResourceSetProvider;

		ResourceSet resourceSet = informationModelResourceSetProvider.getResourceSetFromSession(serviceContext);
		HashSet<String> referencedResourceSet = (HashSet<String>) informationModelResourceSetProvider
				.getReferencedResourcesFromSession(serviceContext);

		try {
			Resource resource = resourceSet.createResource(URI.createURI("fake:/" + fileName));
			resource.load(new ByteArrayInputStream(fileContents.getBytes((StandardCharsets.UTF_8))),
					resourceSet.getLoadOptions());
			referencedResourceSet.add(fileName);
			EcoreUtil2.resolveAll(resourceSet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String createFileName(String namespace, String name, String version, ModelType modelType) {
		return namespace.replace(".", "_") + "_" + name + "_" + version.replace(".", "_") + "."
				+ modelType.getExtension();
	}
}
