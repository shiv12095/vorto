package org.eclipse.vorto.server.devtool.service;

import org.eclipse.xtext.web.servlet.HttpServiceContext;

/**
 * 
 * @author shiv
 *
 */
public interface IInformationModelEditorService {

	/**
	 * 
	 * Adds the @FunctionBlock provided by namespace, name and version to
	 * the @InformationModel referenced by the resourceId. Returns the generated
	 * file
	 * 
	 * @param resourceId
	 * @param namespace
	 * @param name
	 * @param version
	 * @param httpServiceContext
	 * @return
	 */
	String addFunctionBlock(String resourceId, String namespace, String name, String version,
			HttpServiceContext httpServiceContext);

	String searchFunctionBlockByExpression(String expression);
}
