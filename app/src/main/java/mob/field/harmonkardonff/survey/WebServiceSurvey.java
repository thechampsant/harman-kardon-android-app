package mob.field.harmonkardonff.survey;

import linq.ArrayList;
import app.core.model.Response;
import app.core.server.Encode;
import app.core.server.Server;

import com.fieldforce.harmonkardonff.MainActivity;

public class WebServiceSurvey {
	
	//static String Web = "http://harmankardon.infield.co.in/";old
	static String Web = "http://harman.infield.co.in/";
	static String WebController = "ispmobile/";
	static String ApiUrl = Web + WebController;
	static String SaveAMSOSurveyAction="saveAMSOSurvey?";
	static String GetAMSOSurveyAction="getAMSOSurvey?";
/*	static String UdateOtherInfoAction = "UpdateSaleForOther?";
	static String NewStockAction = "UpdateStock?";*/
	public Server server = new Server();
	
	@SuppressWarnings("rawtypes")
	public Response TryGetAMSOSurvey(String amid) {
		// TODO Auto-generated method stub
		String apiUrl = ApiUrl;
		apiUrl += GetAMSOSurveyAction +  "UserName=" + MainActivity.MyInfo.EmployeeCode;
		return server.getResponse(apiUrl,new Survey());
	}

	     public Response TrySubmitAMSOSurvey(ArrayList<Survey> _data) {
		// TODO Auto-generated method stub
		String apiUrl = ApiUrl;
		apiUrl += SaveAMSOSurveyAction;
		apiUrl+="UserName="+MainActivity.MyInfo.EmployeeCode;
		Response obj = server.getResponse(apiUrl,Encode.ToObjectArray(_data),new Survey()); 
		return obj;
	}

/*	     public Response TryUpdateOtherInfo(OtherInfoModel otherinfo) {
	 		String apiUrl = ApiUrl;
	 		apiUrl += UdateOtherInfoAction + Encode.ToObject(otherinfo);
	 		return server.getResponse(apiUrl);
	 	}

	     
	     
	     public Response TryUpdateStock(StocksModel newStock) {
	 		String apiUrl = ApiUrl;
	 		apiUrl += NewStockAction + Encode.ToObject(newStock);

	 		return server.getResponse(apiUrl);
	 	}*/
}
