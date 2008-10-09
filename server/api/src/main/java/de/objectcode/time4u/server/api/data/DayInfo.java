package de.objectcode.time4u.server.api.data;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * DayInfo DTO object.
 * 
 * @author junglas
 */
@XmlType(name = "day-info")
@XmlRootElement(name = "day-info")
public class DayInfo extends DayInfoSummary
{
  private static final long serialVersionUID = -2048583139605476186L;

  /** All workitems of the day. */
  private List<WorkItem> m_workItems;

  public List<WorkItem> getWorkItems()
  {
    return m_workItems;
  }

  public void setWorkItems(final List<WorkItem> workItems)
  {
    m_workItems = workItems;
  }

}
