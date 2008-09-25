package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Project;

/**
 * Interface to the client side project repository.
 * 
 * @author junglas
 */
public interface IProjectRepository
{
  /**
   * Get a project by its identifier.
   * 
   * @param projectId
   *          The project id
   * @return The project with id <tt>projectId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  Project getProject(long projectId) throws RepositoryException;

  /**
   * Get all projects that match a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return All projects matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Project> getProjects(ProjectFilter filter) throws RepositoryException;

  /**
   * Store a project. This method either updates an existing project or inserts a new one.
   * 
   * @param project
   *          The project to store
   * @return The stored project (including generated id for new projects)
   * @throws RepositoryException
   *           on error
   */
  Project storeProject(Project project) throws RepositoryException;
}
