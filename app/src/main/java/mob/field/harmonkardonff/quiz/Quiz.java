package mob.field.harmonkardonff.quiz;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;



public class Quiz extends DataEntity<Quiz> {

	public static String _TableName="Quiz";
	 public Quiz() {
		super(_TableName);
		// TODO Auto-generated constructor stub
	}
	public String QuizID;
	public String QuizTitle;
	public String EffTill=null;
	public String PublishedOn=null;
	public String UpdatedOn=null;
	public String IsQuizCompleted="false";
	public String IsActive="false";
	public String IsPassed="false";
	public String QID;
	public String AID;
	public String Score=null;
	public String Question;
	public String HaveRemarks="false";
	public String IsMultipleChoice="false";
	public String DefaultAnswer;
	public String UserID;
	public String IsAnswered="false";
	public String Remarks;
	public String AnsweredOn="NA";
	public String IsUpdated="false";
	public String Time;
	@Override
	public void RegisterMappings() {
		// TODO Auto-generated method stub
		this.RegisterMapping("QuizTitle",DataTypes.TEXT);
		this.RegisterMapping("QuizID",DataTypes.TEXT);
		this.RegisterMapping("PublishedOn",DataTypes.TEXT);
		this.RegisterMapping("EffTill",DataTypes.TEXT);
		this.RegisterMapping("UpdatedOn",DataTypes.TEXT);
		this.RegisterMapping("IsQuizCompleted",DataTypes.TEXT);
		this.RegisterMapping("IsActive",DataTypes.TEXT);
		this.RegisterMapping("IsPassed",DataTypes.TEXT);
		this.RegisterMapping("QID",DataTypes.TEXT);
		this.RegisterMapping("AID",DataTypes.TEXT);
		this.RegisterMapping("Score",DataTypes.TEXT);
		this.RegisterMapping("Question",DataTypes.TEXT);
		this.RegisterMapping("IsMultipleChoice",DataTypes.TEXT);
		this.RegisterMapping("DefaultAnswer",DataTypes.TEXT);
		this.RegisterMapping("UserID",DataTypes.TEXT);
		this.RegisterMapping("IsAnswered",DataTypes.TEXT);
		this.RegisterMapping("Remarks",DataTypes.TEXT);
		this.RegisterMapping("AnsweredOn",DataTypes.TEXT);
		this.RegisterMapping("IsUpdated",DataTypes.TEXT);
		this.RegisterMapping("Time",DataTypes.TEXT);
	}
	@Override
	public Quiz GetNewObject() {
		// TODO Auto-generated method stub
		return new Quiz();
	}
}
