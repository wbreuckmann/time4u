package de.objectcode.time4u.client.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import de.objectcode.time4u.client.ui.UIPlugin;

public class WorkItemViewPreferencesPage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
  public void init(final IWorkbench workbench)
  {
    setPreferenceStore(UIPlugin.getDefault().getPreferenceStore());
  }

  @Override
  protected void createFieldEditors()
  {
    addField(new BooleanFieldEditor(PreferenceConstants.UI_CONFIRM_WORKITEM_DELETE, "Confirm &WorkItem delete",
        getFieldEditorParent()));
  }
}