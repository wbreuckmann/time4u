package de.objectcode.time4u.client.store.impl.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import de.objectcode.time4u.client.store.api.IProjectRepository;
import de.objectcode.time4u.client.store.api.ProjectFilter;
import de.objectcode.time4u.client.store.api.RepositoryException;
import de.objectcode.time4u.client.store.api.event.ProjectRepositoryEvent;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.entities.ProjectEntity;

public class HibernateProjectRepository implements IProjectRepository
{
  private final HibernateRepository m_repository;
  private final HibernateTemplate m_hibernateTemplate;

  HibernateProjectRepository(final HibernateRepository repository, final HibernateTemplate hibernateTemplate)
  {
    m_repository = repository;
    m_hibernateTemplate = hibernateTemplate;
  }

  /**
   * {@inheritDoc}
   */
  public Project getProject(final long projectId) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Project>() {
      public Project perform(final Session session)
      {
        final ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, projectId);

        if (projectEntity != null) {
          final Project project = new Project();
          projectEntity.toDTO(project);

          return project;
        }
        return null;
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public List<Project> getProjects(final ProjectFilter filter) throws RepositoryException
  {
    return m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<List<Project>>() {
      public List<Project> perform(final Session session)
      {
        final Criteria criteria = session.createCriteria(ProjectEntity.class);

        if (filter.getActive() != null) {
          criteria.add(Restrictions.eq("active", filter.getActive()));
        }
        if (filter.getDeleted() != null) {
          criteria.add(Restrictions.eq("deleted", filter.getDeleted()));
        }
        if (filter.getParentProject() != null) {
          if (filter.getParentProject().longValue() == 0L) {
            criteria.add(Restrictions.isNull("parent"));
          } else {
            criteria.add(Restrictions.eq("parent.id", filter.getParentProject()));
          }
        }
        if (filter.getMinRevision() != null) {
          criteria.add(Restrictions.ge("revision", filter.getMinRevision()));
        }
        criteria.addOrder(Order.asc("id"));

        final List<Project> result = new ArrayList<Project>();

        for (final Object row : criteria.list()) {
          final Project project = new Project();

          ((ProjectEntity) row).toDTO(project);

          result.add(project);
        }

        return result;
      }

    });
  }

  /**
   * {@inheritDoc}
   */
  public Project storeProject(final Project project) throws RepositoryException
  {
    final Project result = m_hibernateTemplate.executeInTransaction(new HibernateTemplate.Operation<Project>() {
      public Project perform(final Session session)
      {
        ProjectEntity projectEntity = (ProjectEntity) session.get(ProjectEntity.class, project.getId());

        if (projectEntity != null) {
        } else {
          projectEntity = new ProjectEntity();

          session.persist(projectEntity);
        }

        final Project result = new Project();

        projectEntity.toDTO(result);

        return result;
      }
    });

    m_repository.fireRepositoryEvent(new ProjectRepositoryEvent(project));

    return result;
  }
}
