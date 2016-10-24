/** 
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package
/*
 * generated by Xtext
 */
org.eclipse.vorto.editor.datatype

import org.eclipse.vorto.editor.datatype.converter.DatatypeValueConverter
import org.eclipse.vorto.editor.datatype.formatting.DatatypeFormatter
import org.eclipse.vorto.editor.datatype.scoping.DatatypeScopeProvider
import org.eclipse.xtext.conversion.IValueConverterService
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.vorto.editor.datatype.formatting.DatatypeFormatter

/** 
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */

class DatatypeRuntimeModule extends AbstractDatatypeRuntimeModule {
	override Class<? extends IScopeProvider> bindIScopeProvider() {
		return DatatypeScopeProvider
	}

	override Class<? extends IQualifiedNameProvider> bindIQualifiedNameProvider() {
		return QualifiedNameWithVersionProvider
	}

	override Class<? extends IValueConverterService> bindIValueConverterService() {
		return DatatypeValueConverter
	}

	override bindIFormatter() {
		DatatypeFormatter
	}
}
