package com.APIService;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

public class QueryModel extends DataEntity<QueryModel> {

	static String TableName = "QueryModel";
	public String query_to_be_routed_to;
	public String tab;
	public String cat;

	public QueryModel(String query_to_be_routed_to, String tab, String cat) {
		super(TableName);
		this.query_to_be_routed_to = query_to_be_routed_to;
		this.tab = tab;
		this.cat = cat;
	}

	public QueryModel() {
		super(TableName);
	}

	@Override
	public QueryModel GetNewObject() {
		return new QueryModel();
	}

	@Override
	public void RegisterMappings() {
		this.RegisterMapping("query_to_be_routed_to", DataTypes.TEXT);
		this.RegisterMapping("tab", DataTypes.TEXT);
		this.RegisterMapping("cat", DataTypes.TEXT);
	}

}
