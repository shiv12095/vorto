/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 *
 *******************************************************************************/
/*
 * generated by Xtext
 */
package org.eclipse.vorto.editor.infomodel.web

import org.eclipse.vorto.editor.infomodel.web.resource.InformationModelResourceSetProvider
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider

/**
 * Use this class to register additional components to be used within the web application.
 */
@FinalFieldsConstructor
class InformationModelWebModule extends AbstractInformationModelWebModule {
		
	def Class<? extends IWebResourceSetProvider> bindIWebResourceSetProvider() {
		return InformationModelResourceSetProvider
	}	
}
