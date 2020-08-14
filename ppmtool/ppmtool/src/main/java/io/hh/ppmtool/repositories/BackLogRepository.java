package io.hh.ppmtool.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.hh.ppmtool.domain.BackLog;

@Repository
public interface BackLogRepository extends CrudRepository<BackLog, Long> {
	
	public BackLog findByProjectIdentifier(String identifier);
}
