package io.hh.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hh.ppmtool.domain.BackLog;
import io.hh.ppmtool.domain.ProjectTask;
import io.hh.ppmtool.repositories.BackLogRepository;
import io.hh.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BackLogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		//Exceptions: Project not found
		
		//PTs to be addded to a specific project, project != null, BL exists
		BackLog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
		//set backlog to project task
		projectTask.setBacklog(backlog);
		//we want our project sequence to be like this: IDPRO-1 IDPRO-2
		Integer BackLogSequence = backlog.getPTSequence();
		// update backlog sequence
		BackLogSequence++;
		//Add sequence to projecttask
		projectTask.setProjectSequence(projectIdentifier+"-"+BackLogSequence);
		projectTask.setProjectIdentifier(projectIdentifier);
		
		//INITIAL priority when priority null
		/*if(projectTask.getPriority() == 0 || projectTask.getPriority() == null) {
			projectTask.setPriority(3);
		}*/
		//INITIAL status when status is null
		if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
			projectTask.setStatus("TO_DO");
		}
		
		return projectTaskRepository.save(projectTask);
	}
}
