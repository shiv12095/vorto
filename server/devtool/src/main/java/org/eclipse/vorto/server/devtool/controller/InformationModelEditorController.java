package org.eclipse.vorto.server.devtool.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.server.devtool.service.IInformationModelEditorService;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/editor/infomodel")
public class InformationModelEditorController {

	@Autowired
	Injector injector;

	@Autowired
	IInformationModelEditorService iInformationModelEditorService;

	@ApiOperation(value = "Adds the function block to the resource set")
	@RequestMapping(value = "/add/functionblock/{namespace}/{name}/{version:.+}", method = RequestMethod.GET)
	public void addFunctionBlock(@ApiParam(value = "Namespace", required = true) final @PathVariable String namespace,
			@ApiParam(value = "Name", required = true) final @PathVariable String name,
			@ApiParam(value = "Version", required = true) final @PathVariable String version,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) {

		Objects.requireNonNull(namespace, "namespace must not be null");
		Objects.requireNonNull(name, "name must not be null");
		Objects.requireNonNull(version, "version must not be null");

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		iInformationModelEditorService.addFunctionBlockToResourceSet(namespace, name, version, httpServiceContext);
	}

	@ApiOperation(value = "Adds the function block to the resource set")
	@RequestMapping(value = "/search={expression:.*}", method = RequestMethod.GET)
	public String searchByExpression(@ApiParam(value = "Search expression", required = true) @PathVariable String expression) {
		
		Objects.requireNonNull(expression, "namespace must not be null");
//		List<ModelResource> modelResources = iInformationModelEditorService.searchByExpression(expression);
//		System.out.println(modelResources.size());
		return iInformationModelEditorService.searchFunctionBlockByExpression(expression);
		
	}
}