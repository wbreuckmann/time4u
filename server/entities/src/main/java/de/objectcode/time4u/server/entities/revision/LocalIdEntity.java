package de.objectcode.time4u.server.entities.revision;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import de.objectcode.time4u.server.api.data.SynchronizableType;

@Entity
@Table(name = "T4U_LOCALID")
public class LocalIdEntity
{
  private SynchronizableType m_entityType;
  private long m_loId;
  private long m_hiId;

  /**
   * Default constructor for hibernate.
   */
  protected LocalIdEntity()
  {
    m_loId = 0;
    m_hiId = 0;
  }

  public LocalIdEntity(final SynchronizableType entityType)
  {
    m_entityType = entityType;
  }

  @Id
  @Type(type = "de.objectcode.time4u.server.entities.util.GenericEnumUserType", parameters = {
      @Parameter(name = "enumClass", value = "de.objectcode.time4u.server.api.data.SynchronizableType"),
      @Parameter(name = "identifierMethod", value = "getValue"), @Parameter(name = "valueOfMethod", value = "forValue") })
  public SynchronizableType getEntityType()
  {
    return m_entityType;
  }

  public void setEntityType(final SynchronizableType entityType)
  {
    m_entityType = entityType;
  }

  public long getLoId()
  {
    return m_loId;
  }

  public void setLoId(final long loId)
  {
    m_loId = loId;
  }

  public long getHiId()
  {
    return m_hiId;
  }

  public void setHiId(final long hiId)
  {
    m_hiId = hiId;
  }
}
