/**
 * 
 */
package mob.field.harmonkardonff.survey;
import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;

import com.fieldforce.harmonkardonff.MainActivity;
/**
 * @author newINNOSOLS
 *
 */
public class SurveyIDTimeDetailModel extends DataEntity<SurveyIDTimeDetailModel>{
	/**
	 * @param _Name
	 */
	static String TableName="SurveyIDTimeDetailModel";
	public String quizID;
	public String QuizMinute;
	public String quizSecond;
	public String currentMilli;
	public String UserName=MainActivity.MyInfo.EmployeeCode;
	public SurveyIDTimeDetailModel() {
		super(TableName);
	}
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("quizID", DataTypes.TEXT);
		this.RegisterMapping("QuizMinute", DataTypes.TEXT);
		this.RegisterMapping("quizSecond", DataTypes.TEXT);
		this.RegisterMapping("UserName", DataTypes.TEXT);
		this.RegisterMapping("currentMilli", DataTypes.TEXT);
	}
	
	/* (non-Javadoc)
	 * @see innosols.sqllite.DataEntity#GetNewObject()
	 */
	@Override
	public SurveyIDTimeDetailModel GetNewObject() {
		// TODO Auto-generated method stub
		return new SurveyIDTimeDetailModel();
	}
}
