package de.objectcode.time4u.server.ejb.seam.api.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.persistence.Query;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import de.objectcode.time4u.server.api.data.EntityType;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;

@XmlType(name = "or")
@XmlRootElement(name = "or")
public class OrFilter implements IFilter
{
  private static final long serialVersionUID = -6096667240200394101L;

  private List<IFilter> m_filters = new ArrayList<IFilter>();

  public OrFilter()
  {
  }

  public OrFilter(final IFilter... filters)
  {
    for (final IFilter filter : filters) {
      m_filters.add(filter);
    }
  }

  public void add(final IFilter filter)
  {
    m_filters.add(filter);
  }

  @XmlElementRefs( { @XmlElementRef(type = AndFilter.class), @XmlElementRef(type = OrFilter.class),
      @XmlElementRef(type = DateRangeFilter.class), @XmlElementRef(type = ParameterRef.class),
      @XmlElementRef(type = PersonFilter.class), @XmlElementRef(type = DayTagFilter.class) })
  public List<IFilter> getFilters()
  {
    return m_filters;
  }

  public void setFilters(final List<IFilter> filters)
  {
    m_filters = filters;
  }

  public String getWhereClause(final EntityType entityType, final Map<String, BaseParameterValue> parameters, ELContext context)
  {
    if (m_filters.isEmpty()) {
      return "";
    }
    final StringBuffer buffer = new StringBuffer("(");
    final Iterator<IFilter> it = m_filters.iterator();
    while (it.hasNext()) {
      buffer.append(it.next().getWhereClause(entityType, parameters, context));
      if (it.hasNext()) {
        buffer.append(" or ");
      }
    }
    buffer.append(")");
    return buffer.toString();
  }

  public void setQueryParameters(final EntityType entityType, final Query query,
      final Map<String, BaseParameterValue> parameters, ELContext context)
  {
    for (final IFilter filter : m_filters) {
      filter.setQueryParameters(entityType, query, parameters, context);
    }
  }
}
