package mob.field.harmonkardonff.entitiymodels;

import java.util.List;

import app.core.sqllite.DataEntity;
import linq.ArrayList;

public class CoronaHistoryModal extends DataEntity<CoronaHistoryModal> {
    public static final String TableName = "CoronaHistoryModal";


      /*"
      "Date": "2020-05-05",
      "Temp": "9",
      "Fever": "No",
      "Diarrhea": "Yes",
      "Cough": "No",
      "Respiratory": null,
      "Sorethroat": null,
      "Breath": null
    }
}*/

    public String Date ="";
    public String Temp="";
    public String Fever ="";
    public String Diarrhea="";
    public String Cough="";
    public String Respiratory="";
    public String Sorethroat="";
    public String Breath="";

    public CoronaHistoryModal() {
        super(TableName);
    }

    @Override
    public void RegisterMappings() {

    }

    @Override
    public CoronaHistoryModal GetNewObject() {
        return new CoronaHistoryModal();
    }
}
