package de.objectcode.time4u.server.web.gwt.main.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import de.objectcode.time4u.server.web.gwt.login.client.LoginService;
import de.objectcode.time4u.server.web.gwt.login.client.LoginServiceAsync;
import de.objectcode.time4u.server.web.gwt.login.client.UserAccountInfo;
import de.objectcode.time4u.server.web.gwt.utils.client.Utils;

public class MainPage implements EntryPoint {
	private static MainPageUiBinder uiBinder = GWT
			.create(MainPageUiBinder.class);

	interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
	}

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	@UiField
	MenuBar mainMenu;

	public void onModuleLoad() {
		loginService.getAuthenticatedUser(new AsyncCallback<UserAccountInfo>() {
			public void onFailure(Throwable caught) {
				Window.alert("Server error: " + caught);
			}

			public void onSuccess(UserAccountInfo result) {
				final Widget mainPage = uiBinder.createAndBindUi(MainPage.this);

				MenuBar reportMenu = new MenuBar(true);
				mainMenu.addItem("Report", reportMenu);

				MenuBar adminMenu = new MenuBar(true);
				mainMenu.addItem("Admin", adminMenu);

				mainMenu.addSeparator(new UserIdMenuItemSeparator(result.getUserId()));

				mainMenu.addItem("Logout", new Command() {
					public void execute() {
						loginService.logout(new AsyncCallback<Void>() {

							public void onSuccess(Void result) {
								Utils.redirect("LoginUI.html");
							}

							public void onFailure(Throwable caught) {
							}
						});
					}
				});
				
				RootLayoutPanel.get().add(mainPage);
			}
		});

//		RootPanel.get("mainMenu").add(mainMenu);

	}
}
