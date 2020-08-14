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
	
	@Autowired
	private ProjectService projectService;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {
		//Exceptions: Project not found
		// message ProjectNotFound: "Project not found"
		
		//PTs to be addded to a specific project, project != null, BL exists
		BackLog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBackLog();
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
		
		//INITIAL status when status is null
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}
		
		//INITIAL priority when priority null
		if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
			projectTask.setPriority(3);
		}
		
		return projectTaskRepository.save(projectTask);
		

	}

	public Iterable<ProjectTask> findBacklogById(String backlog_id, String username) {
		
		projectService.findProjectByIdentifier(backlog_id, username);
		
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {
		//make sure we are searching on an existing backlog
		projectService.findProjectByIdentifier(backlog_id, username);
		
		//make sure that our task exist
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' not found");
		}
		//make sure we are searching on the right backlog/project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '" + pt_id + "' "
					+ "does not exist in project '" + backlog_id + "'");
		}
		
		return projectTask;
	}
	
	public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTask = updatedTask;
		
		return projectTaskRepository.save(projectTask);
	}
	
	public void deletePTByProjectSequence(String backlog_id, String pt_id, String username) {
		ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);
		
		projectTaskRepository.delete(projectTask);
	}
}
