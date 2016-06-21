package org.eclipse.vorto.server.devtool.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelId;
import org.eclipse.vorto.core.api.model.model.ModelReference;
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
	public String addFunctionBlock(String resourceId, String namespace, String name, String version,
			HttpServiceContext httpServiceContext) {

		ModelType modelType = informationModelEditorUtils.getModelType(namespace, name, version);

		if (modelType.equals(ModelType.Functionblock)) {
			String fileName = createFileName(namespace, name, version, modelType);

			IWebResourceSetProvider webResourceSetProvider = injector.getInstance(IWebResourceSetProvider.class);
			InformationModelResourceSetProvider informationModelResourceSetProvider = (InformationModelResourceSetProvider) webResourceSetProvider;
			HashSet<String> referencedResourceSet = (HashSet<String>) informationModelResourceSetProvider
					.getReferencedResourcesFromSession(httpServiceContext);
			ResourceSet resourceSet = informationModelResourceSetProvider.getResourceSetFromSession(httpServiceContext);

			Resource targetResource = resourceSet.getResource(URI.createURI(resourceId), true);
			InformationModel informationModel = (InformationModel) targetResource.getContents().get(0);

			ModelReference modelReference = new ModelId(ModelType.Functionblock, name, namespace, version)
					.asModelReference();

			URI uri = URI.createURI("fake:/" + fileName);

			if (!containsModelReference(informationModel, modelReference)) {
				Resource resource = null;
				if (!referencedResourceSet.contains(fileName)) {
					String fileContents = informationModelEditorUtils.downloadModelFile(namespace, name, version);
					try {
						resource = resourceSet.createResource(uri);
						resource.load(new ByteArrayInputStream(fileContents.getBytes((StandardCharsets.UTF_8))),
								resourceSet.getLoadOptions());
						referencedResourceSet.add(fileName);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					resource = resourceSet.getResource(uri, true);
				}
				EObject eObject = resource.getContents().get(0);
				targetResource.getContents().add(eObject);
				informationModel.getReferences().add(modelReference);
				EcoreUtil2.resolveAll(resourceSet);
			}
			try {
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				targetResource.save(byteArrayOutputStream, null);
				System.out.println("The resourceId is : " + resourceId);
				System.out.println(byteArrayOutputStream.toString());
				return byteArrayOutputStream.toString();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	private boolean containsModelReference(Model model, ModelReference reference) {
		for (ModelReference ref : model.getReferences()) {
			if (ref.getImportedNamespace().equals(reference.getImportedNamespace())) {
				return true;
			}
		}
		return false;
	}

	// private Set<String> getVariableNames(EList<FunctionblocSkProperty>
	// properties) {
	// Set<String> variableNames = new HashSet<String>();
	// for (FunctionblockProperty property : properties) {
	// variableNames.add(property.getName());
	// }
	// return variableNames;
	// }
	//
	// private FunctionblockProperty
	// createFunctionblockProperty(FunctionblockModel functionblockModel,
	// Set<String> existingVariableNames) {
	// FunctionblockProperty functionblockProperty =
	// InformationModelFactory.eINSTANCE.createFunctionblockProperty();
	// functionblockProperty.setType(functionblockModel);
	// functionblockProperty.setName(generateFunctionBlockVariableName(functionblockModel,
	// existingVariableNames));
	// return functionblockProperty;
	// }
	//
	// private String generateFunctionBlockVariableName(FunctionblockModel fbm,
	// Set<String> variableNames) {
	// String variableName = fbm.getName().toLowerCase();
	// int i = 0;
	// while (variableNames.contains(variableName)) {
	// variableName += ++i;
	// }
	// return variableName;
	// }

	private String createFileName(String namespace, String name, String version, ModelType modelType) {
		return namespace.replace(".", "_") + "_" + name + "_" + version.replace(".", "_") + modelType.getExtension();
	}
}
