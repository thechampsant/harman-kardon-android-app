package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class CoronaSurveyRequestModel extends DataEntity<CoronaSurveyRequestModel> {
    public static final String Table_Name ="CoronaSurveyRequestModel";

    public CoronaSurveyRequestModel() {
        super(Table_Name);
    }

   /* {
        "Question": "Are you feeling tired ?",
            "QID": "3",
            "Option": "true,false",
            "Type": "radiobutton"
    }*/

    public String Question="";
    public String QID="";
    public String Option ="";
    public String Type ="";
    public String SetError ="";
    public String SetAnswerValue ="";

    @Override
    public void RegisterMappings() {

    }

    @Override
    public CoronaSurveyRequestModel GetNewObject() {
        return new CoronaSurveyRequestModel();
    }

    @Override
    public String toString() {
        return "CoronaSurveyRequestModel{" +
                "Question='" + Question + '\'' +
                ", QID='" + QID + '\'' +
                ", Option='" + Option + '\'' +
                ", Type='" + Type + '\'' +
                ", SetError='" + SetError + '\'' +
                ", SetAnswerValue='" + SetAnswerValue + '\'' +
                '}'+"\n";
    }
}
