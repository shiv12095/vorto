package org.eclipse.vorto.server.devtool.service.impl;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.vorto.editor.web.resource.WebEditorResourceSetProvider;
import org.eclipse.vorto.server.devtool.exception.ProjectAlreadyExistsException;
import org.eclipse.vorto.server.devtool.exception.ProjectNotFoundException;
import org.eclipse.vorto.server.devtool.models.Project;
import org.eclipse.vorto.server.devtool.models.ProjectResource;
import org.eclipse.vorto.server.devtool.service.IProjectRepositoryService;
import org.eclipse.vorto.server.devtool.service.IProjectRespositoryDAO;
import org.eclipse.xtext.web.server.model.IWebResourceSetProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.inject.Injector;

@Component
public class ProjectRepositoryServiceImpl implements IProjectRepositoryService{
	
	@Autowired
	Injector injector;
	
	@Autowired
	IProjectRespositoryDAO projectRespositoryDAO;
	
	@Override
	public void checkProjectExists(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		if(projectRespositoryDAO.projectExists(projectName, sessionId)){
			throw new ProjectAlreadyExistsException();
		}
	}	
		
	@Override
	public Project createProject(String sessionId, String projectName) throws ProjectAlreadyExistsException {
		WebEditorResourceSetProvider webEditorResourceSetProvider = (WebEditorResourceSetProvider) injector.getInstance(IWebResourceSetProvider.class);		
		Project project = new Project(projectName);
		project.setResourceSet(webEditorResourceSetProvider.getNewResourceSet());
		project.setReferencedResourceSet(new HashSet<String>());
		project.setResourceList(new ArrayList<>());
		if(!projectRespositoryDAO.projectExists(projectName, sessionId)){
			projectRespositoryDAO.createProject(project, sessionId);
			return project;
		}else{
			throw new ProjectAlreadyExistsException();
		}
	}

	@Override
	public Project openProject(String sessionId, String projectName) throws ProjectNotFoundException {
		Project project = projectRespositoryDAO.openProject(projectName, sessionId);
		if(project == null){
			throw new ProjectNotFoundException();			
		}
		return project;
	}

	@Override
	public ArrayList<ProjectResource> getProjectResources(String sessionId, String projectName) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName);
		return project.getResourceList();
	}
	
	@Override
	public ArrayList<Project> getProjects(String sessionId) {
		ArrayList<Project> projectList = projectRespositoryDAO.getProjects(sessionId);
		ArrayList<Project> projectNameList = new ArrayList<>();
		for(Project iterProject : projectList){
			Project project = new  Project(iterProject.getProjectName());
			projectNameList.add(project);
		}
		return projectNameList;
	}

	@Override
	public void createResource(String sessionId, String projectName, String resourceName, String resourceId) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName);
		ArrayList<ProjectResource> resourceList = project.getResourceList();
		ProjectResource projectResource = new ProjectResource();
		projectResource.setResourceId(resourceId);
		projectResource.setName(resourceName);
		resourceList.add(projectResource);
	}

	@Override
	public void deleteResource(String sessionId, String projectName, String resourceId) throws ProjectNotFoundException {
		Project project = openProject(sessionId, projectName) ;		
		URI uri = URI.createURI(resourceId);
		ResourceSet resourceSet = project.getResourceSet();
		Resource resource = resourceSet.getResource(uri, true);
		resourceSet.getResources().remove(resource);
		ProjectResource projectResource = new ProjectResource();
		projectResource.setResourceId(resourceId);
		project.getResourceList().remove(projectResource);
	}
}