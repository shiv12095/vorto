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
package org.eclipse.vorto.editor.datatype.web

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Provider
import com.google.inject.util.Modules
import java.util.concurrent.ExecutorService
import org.eclipse.vorto.devtool.projectrepository.IProjectRepositoryService
import org.eclipse.vorto.editor.datatype.DatatypeRuntimeModule
import org.eclipse.vorto.editor.datatype.DatatypeStandaloneSetup
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor

/**
 * Initialization support for running Xtext languages in web applications.
 */
@FinalFieldsConstructor
class DatatypeWebSetup extends DatatypeStandaloneSetup {
	
	val Provider<ExecutorService> executorServiceProvider;
	
	val IProjectRepositoryService projectRepositoryService;
	
	override Injector createInjector() {
		val runtimeModule = new DatatypeRuntimeModule()
		val webModule = new DatatypeWebModule(executorServiceProvider)
		return Guice.createInjector(Modules.override(runtimeModule).with(webModule,new AbstractModule(){
			
			override protected configure() {
				bind(typeof(IProjectRepositoryService)).toInstance(projectRepositoryService);
			}
			
			}))
	}
	
}
