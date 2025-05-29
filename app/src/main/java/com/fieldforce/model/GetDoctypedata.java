package com.fieldforce.model;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;
import mob.field.harmonkardonff.entitiymodels.VersionUpdationModel;

public class GetDoctypedata  extends DataEntity<GetDoctypedata> {
    public String DocID;
    public String DocUrl;
    public String IsSeen;
    public String DocType;

    @Override
    public void RegisterMappings() {
        this.RegisterMapping("DocID", DataTypes.TEXT);
        this.RegisterMapping("DocUrl", DataTypes.TEXT);
        this.RegisterMapping("DocType", DataTypes.TEXT);
        this.RegisterMapping("IsSeen", DataTypes.TEXT);

    }

    @Override
    public GetDoctypedata GetNewObject() {
        // TODO Auto-generated method stub
        return new GetDoctypedata();
    }

}
