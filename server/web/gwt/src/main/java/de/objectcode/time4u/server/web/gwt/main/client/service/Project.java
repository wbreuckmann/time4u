package de.objectcode.time4u.server.web.gwt.main.client.service;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Project implements IsSerializable {
	private String id;
	private String parentId;
	private String name;
	private boolean hasChildren;

	public Project() {
	}

	public Project(String id, String parentId, String name, boolean hasChildren) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.hasChildren = hasChildren;
	}

	public String getId() {
		return id;
	}

	public String getParentId() {
		return parentId;
	}

	public String getName() {
		return name;
	}

	public boolean isHasChildren() {
		return hasChildren;
	}

}