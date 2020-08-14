package io.hh.ppmtool.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hh.ppmtool.domain.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

	public List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
	
	public ProjectTask findByProjectSequence(String sequence);
}
