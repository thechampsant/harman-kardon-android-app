package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class StringModel extends DataEntity<StringModel> {
    public static final String TableName ="StringModel";

    public StringModel() {
        super(TableName);
    }

    public String message;

    @Override
    public void RegisterMappings() {

    }

    @Override
    public StringModel GetNewObject() {
        return new StringModel();
    }
}
