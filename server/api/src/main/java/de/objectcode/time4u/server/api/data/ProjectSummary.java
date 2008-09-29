package de.objectcode.time4u.server.api.data;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Project DTO object.
 * 
 * This DTO only transfers the summary of a project.
 * 
 * @author junglas
 */
@XmlType(name = "project-summary")
public class ProjectSummary implements ISynchronizableData
{
  private static final long serialVersionUID = 7578389791349119895L;

  /** The internal server id of the project */
  private UUID m_id;
  /** Revision number. */
  private long m_revision;
  /** Project name. */
  private String m_name;
  /** Flag if the project is active. */
  private boolean m_active;
  /** Flag if the project is deleted. */
  private boolean m_deleted;
  /** Internal id of the parent project (<tt>null</tt> for root level projects) */
  private UUID m_parentId;

  @XmlAttribute
  public UUID getId()
  {
    return m_id;
  }

  public void setId(final UUID id)
  {
    m_id = id;
  }

  @XmlAttribute
  public long getRevision()
  {
    return m_revision;
  }

  public void setRevision(final long revision)
  {
    m_revision = revision;
  }

  @XmlAttribute
  public String getName()
  {
    return m_name;
  }

  public void setName(final String name)
  {
    m_name = name;
  }

  @XmlAttribute
  public boolean isActive()
  {
    return m_active;
  }

  public void setActive(final boolean active)
  {
    m_active = active;
  }

  @XmlAttribute
  public boolean isDeleted()
  {
    return m_deleted;
  }

  public void setDeleted(final boolean deleted)
  {
    m_deleted = deleted;
  }

  /**
   * @return the parentId
   */
  @XmlAttribute
  public UUID getParentId()
  {
    return m_parentId;
  }

  /**
   * @param parentId
   *          the parentId to set
   */
  public void setParentId(final UUID parentId)
  {
    m_parentId = parentId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    return m_id != null ? m_id.hashCode() : 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (obj == this) {
      return true;
    }

    if (obj == null || !(obj instanceof ProjectSummary)) {
      return false;
    }

    final ProjectSummary castObj = (ProjectSummary) obj;
    if (m_id == null) {
      return false;
    }

    return m_id.equals(castObj.m_id);
  }

}
