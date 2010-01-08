package de.objectcode.time4u.server.web.gwt.main.server.dao.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.web.gwt.main.client.service.Project;
import de.objectcode.time4u.server.web.gwt.main.server.dao.IProjectDao;

@Repository("projectDao")
@Transactional(propagation=Propagation.MANDATORY)
public class JpaProjectDao extends JpaDaoBase implements IProjectDao {

	@SuppressWarnings("unchecked")
	public List<Project> findRootProjectsDTO() {
		Query query = entityManager
				.createQuery("select p, (select count(*) from "
						+ ProjectEntity.class.getName()
						+ " sp where sp.parent = p) from "
						+ ProjectEntity.class.getName()
						+ " p where p.parent is null and p.deleted = false order by p.name asc");

		List<Object[]> result = query.getResultList();

		List<Project> ret = new ArrayList<Project>();

		for (Object[] row : result) {
			Project project = toDTO((ProjectEntity) row[0], (Long) row[1]);

			ret.add(project);
		}

		return ret;
	}

	@SuppressWarnings("unchecked")
	public List<Project> findChildProjectsDTO(final String projectId) {
		Query query = entityManager
				.createQuery("select p, (select count(*) from "
						+ ProjectEntity.class.getName()
						+ " sp where sp.parent = p) from "
						+ ProjectEntity.class.getName()
						+ " p where p.parent.id = :parentId and p.deleted = false  order by p.name asc");

		query.setParameter("parentId", projectId);

		List<Object[]> result = query.getResultList();

		List<Project> ret = new ArrayList<Project>();

		for (Object[] row : result) {
			Project project = toDTO((ProjectEntity) row[0], (Long) row[1]);

			ret.add(project);
		}

		return ret;
	}

	public void save(ProjectEntity project) {
		entityManager.persist(project);
	}

	public void update(ProjectEntity project) {
		entityManager.merge(project);
	}

	static Project toDTO(ProjectEntity projectEntity, long subProjectCount) {
		return new Project(projectEntity.getId(),
				projectEntity.getParent() != null ? projectEntity.getParent()
						.getId() : null, projectEntity.getName(),
				subProjectCount > 0);

	}
}
