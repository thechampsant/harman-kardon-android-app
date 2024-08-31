package com.APIService;

import com.fieldforce.asyntask.WebService;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class FeedbackUrlConfigModel extends
		DataEntity<FeedbackUrlConfigModel> {
	public String getSubmit_feedback() {
		return submit_feedback;
	}

	public void setSubmit_feedback(String submit_feedback) {
		this.submit_feedback = submit_feedback;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	static String TableName = "FeedbackUrlConfigModel";

	public FeedbackUrlConfigModel() {
		super(TableName);
	}

	public String id;
	public String module;
	public String component;
	public String submit_feedback;
	public String username = WebService.getUsername();

	@Override
	public FeedbackUrlConfigModel GetNewObject() {
		return new FeedbackUrlConfigModel();
	}

	@Override
	public void RegisterMappings() {

		this.RegisterMapping("id", DataTypes.TEXT);
		this.RegisterMapping("module", DataTypes.TEXT);
		this.RegisterMapping("component", DataTypes.TEXT);
		this.RegisterMapping("submit_feedback", DataTypes.TEXT);
		this.RegisterMapping("username", DataTypes.TEXT);
	}

}
