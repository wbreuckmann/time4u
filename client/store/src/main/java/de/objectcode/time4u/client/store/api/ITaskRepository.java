package de.objectcode.time4u.client.store.api;

import java.util.List;

import de.objectcode.time4u.server.api.data.Task;

/**
 * Interface of the client side task repository.
 * 
 * @author junglas
 */
public interface ITaskRepository
{
  /**
   * Get a task by its identifier.
   * 
   * @param taskId
   *          The task id
   * @return The task with id <tt>taskId</tt> or <tt>null</tt>
   * @throws RepositoryException
   *           on error
   */
  Task getTask(long taskId) throws RepositoryException;

  /**
   * Get all taks matching a filter condition.
   * 
   * @param filter
   *          The filter condition
   * @return A tasks matching <tt>filter</tt>
   * @throws RepositoryException
   *           on error
   */
  List<Task> getTasks(TaskFilter filter) throws RepositoryException;

  /**
   * Store a task. This method either inserts a new tasks or updates an existing one.
   * 
   * @param task
   *          The task to be stored
   * @throws RepositoryException
   *           on error
   */
  Task storeTask(Task task) throws RepositoryException;

  /**
   * Store a collection of tasks at once. Only one revision number is generated for this oeration.
   * 
   * @param tasks
   *          A list of tasks to be stored
   * @return The list of stored tasks (including generated ids)
   * @throws RepositoryException
   *           on error
   */
  List<Task> storeTask(List<Task> tasks) throws RepositoryException;
}
