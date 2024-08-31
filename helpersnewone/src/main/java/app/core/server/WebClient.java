package app.core.server;

import app.core.entitymodels.ErrorModel;
import app.core.model.Response;


public class WebClient {

	static String Web = "http://apperror.innosols.co.in/";
	static String WebController = "Error/";
	static String ApiUrl = Web + WebController;
	static String ErrorAction = "setError?";
	public Server server = new Server();
	
	@SuppressWarnings({ "rawtypes" })
	public Response TrySendError(ErrorModel model) {

		String apiUrl = ApiUrl;
		apiUrl += ErrorAction + Encode.ToObject(model);
		return server.getResponse(apiUrl);
	}
}
