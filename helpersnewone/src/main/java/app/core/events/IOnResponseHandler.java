package app.core.events;

import app.core.model.Response;

public interface IOnResponseHandler {
	
	@SuppressWarnings("rawtypes")
	public void OnResponse(String code, Response response);
	
}
