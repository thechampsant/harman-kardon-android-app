package mob.field.harmonkardonff.quiz;

import com.fieldforce.harmonkardonff.MainActivity;

import linq.ArrayList;
import app.core.model.Response;
import app.core.server.Encode;
import app.core.server.Server;

public class WebServiceQuiz {
	
	//static String Web = "http://harmankardon.infield.co.in/";
	static String Web = "http://harman.infield.co.in/";
	static String WebController = "ispmobile/";
	static String ApiUrl = Web + WebController;
	static String SaveAMSOQuizAction="saveAMSOQuiz?";
	static String GetAMSOQuizAction="getAMSOQuiz?";
	/*static String UdateOtherInfoAction = "UpdateSaleForOther?";
	static String NewStockAction = "UpdateStock?";*/
	public Server server = new Server();
	
	@SuppressWarnings("rawtypes")
	public Response TryGetAMSOQuiz(String amid) {
		// TODO Auto-generated method stub
		String apiUrl = ApiUrl;
		apiUrl += GetAMSOQuizAction +  "UserName=" + MainActivity.MyInfo.EmployeeCode;
		return server.getResponse(apiUrl,new Quiz());
	}

	     public Response TrySubmitAMSOQuiz(ArrayList<Quiz> _data) {
		// TODO Auto-generated method stub
		String apiUrl = ApiUrl;
		apiUrl += SaveAMSOQuizAction;
		apiUrl+="UserName="+MainActivity.MyInfo.EmployeeCode;
		Response obj = server.getResponse(apiUrl,Encode.ToObjectArray(_data),new Quiz()); 
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
