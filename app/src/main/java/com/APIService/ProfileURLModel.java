package com.APIService;

import com.fieldforce.asyntask.WebService;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class ProfileURLModel extends
		DataEntity<ProfileURLModel> {
	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getGet_profile() {
		return get_profile;
	}

	public void setGet_profile(String get_profile) {
		this.get_profile = get_profile;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	static String TableName = "ProfileURLModel";

	public ProfileURLModel() {
		super(TableName);
	}

	public String id;
	public String module;
	public String component;
	public String get_profile;
	public String username = WebService.getUsername();

	@Override
	public ProfileURLModel GetNewObject() {
		return new ProfileURLModel();
	}

	@Override
	public void RegisterMappings() {
		this.RegisterMapping("id", DataTypes.TEXT);
		this.RegisterMapping("module", DataTypes.TEXT);
		this.RegisterMapping("component", DataTypes.TEXT);
		this.RegisterMapping("get_profile", DataTypes.TEXT);
		this.RegisterMapping("username", DataTypes.TEXT);
	}

}
