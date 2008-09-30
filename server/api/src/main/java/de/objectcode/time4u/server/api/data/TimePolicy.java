package de.objectcode.time4u.server.api.data;

public abstract class TimePolicy implements ISynchronizableData
{
  private static final long serialVersionUID = 5344090291753057165L;

  /** Internal server id of the team. */
  private String m_id;
  /** Revision number. */
  private long m_revision;
  /** Client id of the last modification */
  private long m_lastModifiedByClient;
  protected CalendarDay m_validFrom;
  protected CalendarDay m_validUntil;

  public String getId()
  {
    return m_id;
  }

  public void setId(final String id)
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

  public long getLastModifiedByClient()
  {
    return m_lastModifiedByClient;
  }

  public void setLastModifiedByClient(final long lastModifiedByClient)
  {
    m_lastModifiedByClient = lastModifiedByClient;
  }

  public CalendarDay getValidFrom()
  {
    return m_validFrom;
  }

  public void setValidFrom(final CalendarDay validFrom)
  {
    m_validFrom = validFrom;
  }

  public CalendarDay getValidUntil()
  {
    return m_validUntil;
  }

  public void setValidUntil(final CalendarDay validUntil)
  {
    m_validUntil = validUntil;
  }

  public abstract int getRegularTime(CalendarDay day);
}
