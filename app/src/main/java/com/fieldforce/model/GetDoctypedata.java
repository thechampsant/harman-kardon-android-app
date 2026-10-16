package com.fieldforce.model;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;
import mob.field.harmonkardonff.entitiymodels.VersionUpdationModel;

public class GetDoctypedata  extends DataEntity<GetDoctypedata> {
    public String DocID;
    public String DocUrl;
    public String IsSeen;
    public String DocType;
    public String ValidFrom;
    public String ValidTill;
    public String DisplayOrder;

    @Override
    public void RegisterMappings() {
        this.RegisterMapping("DocID", DataTypes.TEXT);
        this.RegisterMapping("DocUrl", DataTypes.TEXT);
        this.RegisterMapping("DocType", DataTypes.TEXT);
        this.RegisterMapping("IsSeen", DataTypes.TEXT);
        this.RegisterMapping("ValidFrom", DataTypes.TEXT);
        this.RegisterMapping("ValidTill", DataTypes.TEXT);
        this.RegisterMapping("DisplayOrder", DataTypes.TEXT);

    }

    @Override
    public GetDoctypedata GetNewObject() {
        // TODO Auto-generated method stub
        return new GetDoctypedata();
    }

}
