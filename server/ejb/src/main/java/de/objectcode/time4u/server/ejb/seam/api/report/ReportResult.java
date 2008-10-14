package de.objectcode.time4u.server.ejb.seam.api.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ReportResult
{
  String m_name;
  List<ColumnDefinition> m_columns;
  List<ReportRow> m_rows;
  Map<Object, ReportResultGroup> m_groups;

  public ReportResult(final String name, final List<ColumnDefinition> columns)
  {
    m_name = name;
    m_columns = columns;
    m_rows = new ArrayList<ReportRow>();
    m_groups = new TreeMap<Object, ReportResultGroup>();
  }

  public String getName()
  {
    return m_name;
  }

  public List<ColumnDefinition> getColumns()
  {
    return m_columns;
  }

  public List<ReportRow> getRows()
  {
    return m_rows;
  }

  public boolean isHasGroups()
  {
    return !m_groups.isEmpty();
  }

  public Collection<ReportResultGroup> getGroups()
  {
    return m_groups.values();
  }

  public void addRow(final LinkedList<ValueLabelPair> groups, final Object[] row)
  {
    if (groups.isEmpty()) {
      m_rows.add(new ReportRow(m_rows.size(), row));
    } else {
      final ValueLabelPair top = groups.removeFirst();
      ReportResultGroup group = m_groups.get(top.getValue());

      if (group == null) {
        group = new ReportResultGroup(top);

        m_groups.put(group.getValue(), group);
      }

      group.addRow(groups, row);
    }
  }
}
