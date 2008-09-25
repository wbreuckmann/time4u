package de.objectcode.time4u.client.ui;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.objectcode.time4u.client.ui.views.CalendarView;
import de.objectcode.time4u.client.ui.views.ProjectTreeView;
import de.objectcode.time4u.client.ui.views.TaskListView;

public class Perspective implements IPerspectiveFactory
{
  public static final String ID = "de.objectcode.client.ui.perspective";

  public void createInitialLayout(final IPageLayout layout)
  {
    final String editorArea = layout.getEditorArea();
    layout.setEditorAreaVisible(false);

    layout.addStandaloneView(CalendarView.ID, true, IPageLayout.LEFT, 0.25f, editorArea);
    layout.addStandaloneView(TaskListView.ID, true, IPageLayout.TOP, 0.7f, CalendarView.ID);
    layout.addStandaloneView(ProjectTreeView.ID, true, IPageLayout.TOP, 0.6f, TaskListView.ID);
    //    layout.addStandaloneView(WorkItemView.ID, true, IPageLayout.TOP, 0.7f, editorArea);
    //    layout.addStandaloneView(StatisticsView.ID, true, IPageLayout.BOTTOM, 0.3f, editorArea);
    //    layout.addStandaloneView(PunchView.ID, true, IPageLayout.RIGHT, 0.7f, StatisticsView.ID);

    layout.getViewLayout(ProjectTreeView.ID).setCloseable(false);
    layout.getViewLayout(TaskListView.ID).setCloseable(false);
    layout.getViewLayout(CalendarView.ID).setCloseable(false);
    //    layout.getViewLayout(WorkItemView.ID).setCloseable(false);
    //    layout.getViewLayout(StatisticsView.ID).setCloseable(false);
    //    layout.getViewLayout(PunchView.ID).setCloseable(false);

  }
}
