package de.objectcode.time4u.server.api.data;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;

/**
 * Helper class to encode filter results in XML.
 * 
 * The is a limitation in JBoss' web-service implementation that does not allow <tt>java.util.List</tt> results.
 * 
 * @author junglas
 * 
 * @param <T>
 */
@XmlType(name = "filter-result")
public class FilterResult<T> implements Serializable
{
  private static final long serialVersionUID = -8284477347654961022L;

  private List<T> results;

  @XmlElements( { @XmlElement(name = "project", type = Project.class),
      @XmlElement(name = "project-summary", type = ProjectSummary.class) })
  public List<T> getResults()
  {
    return results;
  }

  public void setResults(final List<T> results)
  {
    this.results = results;
  }

}
