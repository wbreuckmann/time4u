package de.objectcode.time4u.server.api.filter;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlType;

/**
 * A filter condition for querying projects.
 * 
 * @author junglas
 */
@XmlType(name = "project-filter")
public class ProjectFilter implements Serializable
{
  private static final long serialVersionUID = 4840094656452995441L;

  /** Condition for the parent project (optional, "" = root project). */
  private String m_parentProject;
  /** Condition for the active flag (optional). */
  private Boolean m_active;
  /** Condition for the delete flag (optional). */
  private Boolean m_deleted;
  /** Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned). */
  private Long m_minRevision;
  /** Maximum (inclusive) revision number (i.e. only revisions less or equals are returned). */
  private Long m_maxRevision;
  /** Client id of the last modification. */
  private Long m_lastModifiedByClient;
  /** Desired order. */
  private Order m_order;

  /**
   * Create a new ProjectFilter.
   */
  public ProjectFilter()
  {
    m_order = Order.ID;
  }

  /**
   * Create a new ProjectFilter.
   * 
   * @param active
   *          ondition for the active flag (optional)
   * @param deleted
   *          Condition for the delete flag (optional)
   * @param minRevision
   *          Minimum (inclusive) revision number (i.e. only revisions greater or equals are returned)
   * @param maxRevision
   *          Maximum (inclusive) revision number (i.e. only revisions less or equals are returned)
   * @param parentProject
   *          Condition for the parent project (optional, "" = root project)
   * @param order
   *          Desired order
   */
  public ProjectFilter(final Boolean active, final Boolean deleted, final Long minRevision, final Long maxRevision,
      final String parentProject, final Order order)
  {
    m_active = active;
    m_deleted = deleted;
    m_minRevision = minRevision;
    m_maxRevision = maxRevision;
    m_parentProject = parentProject;
    m_order = order;
  }

  public String getParentProject()
  {
    return m_parentProject;
  }

  public Boolean getActive()
  {
    return m_active;
  }

  public Boolean getDeleted()
  {
    return m_deleted;
  }

  public Long getMinRevision()
  {
    return m_minRevision;
  }

  public void setParentProject(final String parentProject)
  {
    m_parentProject = parentProject;
  }

  public void setActive(final Boolean active)
  {
    m_active = active;
  }

  public void setDeleted(final Boolean deleted)
  {
    m_deleted = deleted;
  }

  public void setMinRevision(final Long minRevision)
  {
    m_minRevision = minRevision;
  }

  public Long getMaxRevision()
  {
    return m_maxRevision;
  }

  public void setMaxRevision(final Long maxRevision)
  {
    m_maxRevision = maxRevision;
  }

  public Long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final Long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public Order getOrder()
  {
    return m_order;
  }

  public void setOrder(final Order order)
  {
    m_order = order;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    final StringBuffer buffer = new StringBuffer("ProjectFilter(");
    buffer.append("active=").append(m_active);
    buffer.append(",deleted=").append(m_deleted);
    buffer.append(",parentProject=").append(m_parentProject);
    buffer.append(",minRevision=").append(m_minRevision);
    buffer.append(",maxRevision=").append(m_maxRevision);
    buffer.append(",lastModifiedByClient=").append(m_lastModifiedByClient);
    buffer.append(",order=").append(m_order);
    buffer.append(")");

    return buffer.toString();
  }

  /**
   * Convenient method to create a "only top level projects" filter.
   * 
   * @param onlyActive
   *          <tt>true</tt> if only active projects should be filtered
   * @return The desired filter condition
   */
  public static ProjectFilter filterRootProjects(final boolean onlyActive)
  {
    return new ProjectFilter(onlyActive ? true : null, false, null, null, "", Order.NAME);
  }

  /**
   * Convenient method to create a "only sub-projects of a given project" filter.
   * 
   * @param parentProjectId
   *          The id of the parent project
   * @param onlyActive
   *          <tt>true</tt> if only active projects should be filtered
   * @return The desired filter condition
   */
  public static ProjectFilter filterChildProjects(final String parentProjectId, final boolean onlyActive)
  {
    return new ProjectFilter(onlyActive ? true : null, false, null, null, parentProjectId, Order.NAME);
  }

  /**
   * Order enumeration.
   */
  public static enum Order
  {
    /** Order by internal id. */
    ID,
    /** Order by project name. */
    NAME
  }
}
