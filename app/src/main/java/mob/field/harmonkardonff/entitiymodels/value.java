package mob.field.harmonkardonff.entitiymodels;

import app.core.sqllite.DataEntity;

public class value extends DataEntity<value> {
    public static final String TableName ="value";

    public value() {
        super(TableName);
    }

    public String Title="";
    public String TileValue ="";

    @Override
    public void RegisterMappings() {

    }

    @Override
    public value GetNewObject() {
        return new value();
    }

    @Override
    public String toString() {
        return "value{" +
                "Title='" + Title + '\'' +
                ", TileValue='" + TileValue + '\'' +
                '}';
    }
}
