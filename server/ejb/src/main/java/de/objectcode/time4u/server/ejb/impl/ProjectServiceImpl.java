package de.objectcode.time4u.server.ejb.impl;

import java.util.UUID;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.RemoteBinding;

import de.objectcode.time4u.server.api.IProjectService;
import de.objectcode.time4u.server.api.ServiceException;
import de.objectcode.time4u.server.api.data.FilterResult;
import de.objectcode.time4u.server.api.data.Project;
import de.objectcode.time4u.server.api.data.ProjectSummary;
import de.objectcode.time4u.server.api.filter.ProjectFilter;

/**
 * EJB3 implementation of the project service interface.
 * 
 * @author junglas
 */
@Stateless
@Remote(IProjectService.class)
@RemoteBinding(jndiBinding = "time4u-server/ProjectServiceBean/remote")
@Local(IProjectService.class)
@LocalBinding(jndiBinding = "time4u-server/ProjectServiceBean/local")
public class ProjectServiceImpl implements IProjectService
{
  @PersistenceContext(unitName = "time4u")
  private EntityManager m_manager;

  public Project getProject(final UUID projectId) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public FilterResult<Project> getProjects(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public FilterResult<ProjectSummary> getProjectSumaries(final ProjectFilter filter) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public ProjectSummary getProjectSummary(final UUID projectId) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Project storeProject(final Project project) throws ServiceException
  {
    // TODO Auto-generated method stub
    return null;
  }

}
