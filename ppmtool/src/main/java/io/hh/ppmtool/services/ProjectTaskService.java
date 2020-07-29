package io.hh.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hh.ppmtool.domain.BackLog;
import io.hh.ppmtool.domain.Project;
import io.hh.ppmtool.domain.ProjectTask;
import io.hh.ppmtool.exceptions.ProjectIdException;
import io.hh.ppmtool.exceptions.ProjectNotFoundException;
import io.hh.ppmtool.repositories.BackLogRepository;
import io.hh.ppmtool.repositories.ProjectRepository;
import io.hh.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BackLogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		//Exceptions: Project not found
		// message ProjectNotFound: "Project not found"
		try {
			//PTs to be addded to a specific project, project != null, BL exists
			BackLog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			//set backlog to project task
			projectTask.setBacklog(backlog);
			//we want our project sequence to be like this: IDPRO-1 IDPRO-2
			Integer BackLogSequence = backlog.getPTSequence();
			// update backlog sequence
			BackLogSequence++;
			backlog.setPTSequence(BackLogSequence);
			//Add sequence to projecttask
			projectTask.setProjectSequence(projectIdentifier+"-"+BackLogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			
			//INITIAL priority when priority null
			if(projectTask.getPriority() == null) {
				projectTask.setPriority(3);
			}
			
			//INITIAL status when status is null
			if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}
			
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not Found");
		}

	}

	public Iterable<ProjectTask> findBacklogById(String backlog_id) {
		Project project = projectRepository.findByProjectIdentifier(backlog_id);
		
		if(project == null) {
			throw new ProjectNotFoundException("Project with ID: '" + backlog_id + "' does not exist");
		}
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}
	
	public ProjectTask findPTByProjectSequence(String sequence) {
		//make sure we are searching on the right backlog
		return projectTaskRepository.findByProjectSequence(sequence);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {
		return projectTaskRepository.findByProjectSequence(pt_id);
	}
}
