package de.objectcode.time4u.server.api.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Todo DTO object.
 * 
 * @author junglas
 */
public class Todo implements ISynchronizableData
{
  private static final long serialVersionUID = 5927951499996904471L;

  /** Internal server id of the todo. */
  private long m_id;
  /** Revision number. */
  private long m_revision;
  /** The server id of the task the todo belongs to. */
  private long m_taskId;
  /** Optional server id of the person the todo is assigned to. */
  private Long m_assignedToPersonId;
  /** Optional server id of the team the todo is assigned to. */
  private Long m_assignedToTeamId;
  /** Optional server id of the person who created the todo. */
  private Long m_reporterId;
  /** Priority of the todo. */
  private int m_priority;
  /** Todo header/title. */
  private String m_header;
  /** Todo description. */
  private String m_description;
  /** Flag if the todo has been completed. */
  private boolean m_completed;
  /** Timestamp the todo was created. */
  private Date m_createdAt;
  /** Optional timestamp the todo was completed. */
  private Date m_completedAt;
  /** Optional deadline of the todo. */
  private Date m_deadline;
  /** Map of all meta properties of the todo. */
  private List<MetaProperty> m_metaProperties;

  public Long getReporterId()
  {
    return m_reporterId;
  }

  public void setReporterId(final Long reporterId)
  {
    m_reporterId = reporterId;
  }

  public Long getAssignedToPersonId()
  {
    return m_assignedToPersonId;
  }

  public void setAssignedToPersonId(final Long assignedToPersonId)
  {
    m_assignedToPersonId = assignedToPersonId;
  }

  public Long getAssignedToTeamId()
  {
    return m_assignedToTeamId;
  }

  public void setAssignedToTeamId(final Long assignedToTeamId)
  {
    m_assignedToTeamId = assignedToTeamId;
  }

  public boolean isCompleted()
  {
    return m_completed;
  }

  public void setCompleted(final boolean completed)
  {
    m_completed = completed;
  }

  public Date getCreatedAt()
  {
    return m_createdAt;
  }

  public void setCreatedAt(final Date createdAt)
  {
    m_createdAt = createdAt;
  }

  public Date getCompletedAt()
  {
    return m_completedAt;
  }

  public void setCompletedAt(final Date completedAt)
  {
    m_completedAt = completedAt;
  }

  public Date getDeadline()
  {
    return m_deadline;
  }

  public void setDeadline(final Date deadline)
  {
    m_deadline = deadline;
  }

  public String getDescription()
  {
    return m_description;
  }

  public void setDescription(final String description)
  {
    m_description = description;
  }

  public String getHeader()
  {
    return m_header;
  }

  public void setHeader(final String header)
  {
    m_header = header;
  }

  public long getId()
  {
    return m_id;
  }

  public void setId(final long id)
  {
    m_id = id;
  }

  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  public int getPriority()
  {
    return m_priority;
  }

  public void setPriority(final int priority)
  {
    m_priority = priority;
  }

  public long getTaskId()
  {
    return m_taskId;
  }

  public void setTaskId(final long taskId)
  {
    m_taskId = taskId;
  }

  public List<MetaProperty> getMetaProperties()
  {
    return m_metaProperties;
  }

  public void setMetaProperties(final List<MetaProperty> metaProperties)
  {
    m_metaProperties = metaProperties;
  }

  public void addMetaProperties(final MetaProperty metaProperty)
  {
    if (m_metaProperties == null) {
      m_metaProperties = new ArrayList<MetaProperty>();
    }
    m_metaProperties.add(metaProperty);
  }

}
