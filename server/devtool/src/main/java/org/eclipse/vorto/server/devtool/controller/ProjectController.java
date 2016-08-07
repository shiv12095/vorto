package org.eclipse.vorto.server.devtool.controller;

import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.eclipse.xtext.web.servlet.HttpServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.inject.Injector;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/project")
public class ProjectController {

	@Autowired
	Injector injector;

	@Autowired
	IProjectRepositoryService projectRepositoryService;

	@ResponseStatus(value=HttpStatus.NOT_FOUND, reason = "Project not found")
    @ExceptionHandler(ProjectNotFoundException.class)
	public void handleProjectNotFoundException(){
		
	}
	
	@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason = "Project already exists")
    @ExceptionHandler(ProjectAlreadyExistsException.class)
	public void handleProjectAlreadyExistsException(){
		
	}
	
	@ApiOperation(value = "Checks whether a Vorto project already exists")
	@RequestMapping(value = "/check/{projectName}", method = RequestMethod.GET)
	public void checkProjectExists(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws ProjectAlreadyExistsException{

		Objects.requireNonNull(projectName, "projectName must not be null");

		String sessionId = request.getSession().getId();

		projectRepositoryService.checkProjectExists(sessionId, projectName);
	}
	
	@ApiOperation(value = "Creates a new Vorto project")
	@RequestMapping(value = "/new/{projectName}", method = RequestMethod.GET )
	public void createProject(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws ProjectAlreadyExistsException{

		Objects.requireNonNull(projectName, "projectName must not be null");

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);

		String sessionId = request.getSession().getId();

		Project project = projectRepositoryService.createProject(sessionId, projectName);

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
	}

	@ApiOperation(value = "Opens an existing Vorto project")
	@RequestMapping(value = "/open/{projectName}", method = RequestMethod.GET)
	public void openProject(@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		HttpServiceContext httpServiceContext = new HttpServiceContext(request);
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector
				.getInstance(IWebResourceSetProvider.class);

		String sessionId = request.getSession().getId();
		Project project = projectRepositoryService.openProject(sessionId, projectName);

		webEditorResourceSetProvider.setSessionResourceSet(httpServiceContext, project.getResourceSet());
		webEditorResourceSetProvider.setSessionRefencedResourceSet(httpServiceContext,
				project.getReferencedResourceSet());
	}
	
	@ApiOperation(value = "Returns all the project for the user")
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public ArrayList<Project> getProjects(final HttpServletRequest request) {
		String sessionId = request.getSession().getId();
		return projectRepositoryService.getProjects(sessionId);
	}

	@ApiOperation(value = "Deletes the resource in the Vorto project")
	@RequestMapping(value = "resource/delete/{projectName}/{resourceId}/", method = RequestMethod.GET)
	public void getProjectResourceContents(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "ResourceId", required = true) final @PathVariable String resourceId,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws ProjectNotFoundException{

		Objects.requireNonNull(projectName, "projectName must not be null");
		Objects.requireNonNull(resourceId, "resourceId must not be null");
		String sessionId = request.getSession().getId();
		projectRepositoryService.deleteResource(sessionId, projectName, resourceId);
	}
	
	@ApiOperation(value = "Returns a list of resources in the Vorto project")
	@RequestMapping(value = "/resources/{projectName}", method = RequestMethod.GET)
	public ArrayList<ProjectResource> getResources(
			@ApiParam(value = "ProjectName", required = true) final @PathVariable String projectName,
			@ApiParam(value = "Request", required = true) final HttpServletRequest request) throws ProjectNotFoundException {

		Objects.requireNonNull(projectName, "projectName must not be null");

		String sessionId = request.getSession().getId();
		return projectRepositoryService.getProjectResources(sessionId, projectName);
	}
}
