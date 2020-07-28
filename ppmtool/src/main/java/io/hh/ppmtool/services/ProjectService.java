package io.hh.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hh.ppmtool.domain.BackLog;
import io.hh.ppmtool.domain.Project;
import io.hh.ppmtool.exceptions.ProjectIdException;
import io.hh.ppmtool.repositories.BackLogRepository;
import io.hh.ppmtool.repositories.ProjectRepository;

@Service
public class ProjectService {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BackLogRepository backlogRepository;
	
	public Project saveOrUpdateProject(Project project) {
		try {
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId() == null) {
				BackLog backlog = new BackLog();
				project.setBackLog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			
			if(project.getId() != null) {
				project.setBackLog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
		} catch (Exception e) {
			throw new ProjectIdException("Project Id '" + project.getProjectIdentifier().toUpperCase() + "' already exists");
		}
		 
	}
	
	public Project findProjectByIdentifier(String projectIdentifier) {
		Project project = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project Id '" + projectIdentifier + "' does not exist");
		}
		
		return project;
	}
	
	public Iterable<Project> findAllProjects() {
		return projectRepository.findAll();
	}
	
	public void deleteProjectByIdentifier(String projectId) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Cannot delete Project with Id '" + projectId + "'. This project does not exist");
		}
		
		projectRepository.delete(project);
	}
	
}
