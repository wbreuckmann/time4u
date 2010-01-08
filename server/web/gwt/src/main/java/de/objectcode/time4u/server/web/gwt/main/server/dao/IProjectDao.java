package de.objectcode.time4u.server.web.gwt.main.server.dao;

import java.util.List;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.main.client.Project;

public interface IProjectDao {
	List<Project> findRootProjectsDTO();

	List<Project> findChildProjectsDTO(String projectId);

	void save(ProjectEntity project);

	void update(ProjectEntity project);
}