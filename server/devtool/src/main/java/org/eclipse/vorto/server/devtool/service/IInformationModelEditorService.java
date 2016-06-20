package org.eclipse.vorto.server.devtool.service;


import org.eclipse.xtext.web.servlet.HttpServiceContext;


/**
 * 
 * @author shiv
 *
 */
public interface IInformationModelEditorService {
		
	void addFunctionBlockToResourceSet(String name, String namespace, String version,
			HttpServiceContext httpServiceContext);
	
	String searchFunctionBlockByExpression(String expression);
}
