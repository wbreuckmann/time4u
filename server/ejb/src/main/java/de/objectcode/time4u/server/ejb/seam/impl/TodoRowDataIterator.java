package de.objectcode.time4u.server.ejb.seam.impl;

import java.util.List;

import de.objectcode.time4u.server.ejb.seam.api.report.IReportDataCollector;
import de.objectcode.time4u.server.ejb.seam.api.report.IRowDataAdapter;
import de.objectcode.time4u.server.entities.DayInfoEntity;
import de.objectcode.time4u.server.entities.PersonEntity;
import de.objectcode.time4u.server.entities.ProjectEntity;
import de.objectcode.time4u.server.entities.TaskEntity;
import de.objectcode.time4u.server.entities.TimePolicyEntity;
import de.objectcode.time4u.server.entities.TodoEntity;
import de.objectcode.time4u.server.entities.WorkItemEntity;

public class TodoRowDataIterator implements IRowDataIterator<TodoEntity>, IRowDataAdapter
{
  TodoEntity m_currentTodo;

  public void setCurrentRow(final Object row)
  {
    m_currentTodo = (TodoEntity) row;
  }

  public DayInfoEntity getDayInfo()
  {
    return null;
  }

  public PersonEntity getPerson()
  {
    return m_currentTodo.getReporter();
  }

  public ProjectEntity getProject()
  {
    if (m_currentTodo.getTask() != null) {
      return m_currentTodo.getTask().getProject();
    }
    return null;
  }

  public TaskEntity getTask()
  {
    return m_currentTodo.getTask();
  }

  public List<TimePolicyEntity> getTimePolicies()
  {
    return null;
  }

  public TodoEntity getTodo()
  {
    return m_currentTodo;
  }

  public WorkItemEntity getWorkItem()
  {
    return null;
  }

  public void iterate(final List<TodoEntity> result, final IReportDataCollector collector)
  {
    for (final TodoEntity todo : result) {
      m_currentTodo = todo;

      collector.collect(this);
    }
    collector.finish();
  }

}
