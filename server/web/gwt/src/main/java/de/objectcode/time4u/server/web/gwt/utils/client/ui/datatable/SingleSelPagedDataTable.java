package de.objectcode.time4u.server.web.gwt.utils.client.ui.datatable;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.objectcode.time4u.server.web.gwt.utils.client.service.IDataPage;

public class SingleSelPagedDataTable<RowClass> extends PagedDataTable<RowClass> implements
		 HasSelectionHandlers<RowClass> {

	private SingleSelDataTable<RowClass> dataTable;

	public SingleSelPagedDataTable(int pageSize, DataTableColumn<RowClass>... columns) {
		super(pageSize, columns);
		
		dataTable = new SingleSelDataTable<RowClass>(columns);
		dataTable.setFixedRowCount(pageSize);

		VerticalPanel panel = new VerticalPanel();

		panel.add(dataTable);
		panel.add(dataPager);

		for (int i = 0; i < columns.length; i++) {
			if (columns[i].isSortable()) {
				setColumnSorting(i, true, false);
				break;
			}
		}

		initWidget(panel);
	}

	public RowClass getCurrentSelection() {
		return dataTable.getCurrentSelection();
	}

	public void setDataPage(IDataPage<RowClass> dataPage) {
		for (int i = 0; i < pageSize; i++) {
			dataTable.setRow(i, i < dataPage.getPageData().size() ? dataPage
					.getPageData().get(i) : null);
		}
		dataTable.updateSelection();

		dataPager.setDataPage(dataPage);
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<RowClass> handler) {
		return dataTable.addSelectionHandler(handler);
	}

	protected DataTable<RowClass> getDataTable() {
		return dataTable;
	}
}
