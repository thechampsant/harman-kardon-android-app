package mob.field.harmonkardonff.entitiymodels;

import android.os.Parcel;
import android.os.Parcelable;

import app.core.sqllite.DataEntity;
import app.core.sqllite.DataTypes;


public class MyInfoModel extends DataEntity<MyInfoModel> implements Parcelable {

    static String TableName = "MyInfoModel";

    public MyInfoModel() {
        super(TableName);
        // TODO Auto-generated constructor stub
    }

    public String ISPName = "N.A";
    public String UserID;
    public String EmployeeCode = "000000";
    public String Storetype = "";
    public String CurrentStore = "N.A";
    public String CounterCode = "N.A.";
    public String AssignedOnS = "N.A.";
    public String CurrentStoreLocation = "N.A.";
    public String Pmobile1 = "N.A";
    public String version = "0.0";
    public String StoreID;
    public String LastUpdatedOn = "N.A";
    public String FileUrl;
    public String V5ID;
    public String TrainingType;
    public String ISPCategory;



    protected MyInfoModel(Parcel in) {
        ISPName = in.readString();
        UserID = in.readString();
        V5ID=in.readString();
        EmployeeCode = in.readString();
        CurrentStore = in.readString();
        CounterCode = in.readString();
        AssignedOnS = in.readString();
        CurrentStoreLocation = in.readString();
        Pmobile1 = in.readString();
        version = in.readString();
        StoreID = in.readString();
        LastUpdatedOn = in.readString();
        FileUrl = in.readString();
        ISPCategory = in.readString();
    }

    public static final Creator<MyInfoModel> CREATOR = new Creator<MyInfoModel>() {
        @Override
        public MyInfoModel createFromParcel(Parcel in) {
            return new MyInfoModel(in);
        }

        @Override
        public MyInfoModel[] newArray(int size) {
            return new MyInfoModel[size];
        }
    };

    @Override
    public void RegisterMappings() {
        // TODO Auto-generated method stub
        this.RegisterMapping("ISPName", DataTypes.TEXT);
        this.RegisterMapping("UserID", DataTypes.TEXT);
        this.RegisterMapping("V5ID", DataTypes.TEXT);
        this.RegisterMapping("EmployeeCode", DataTypes.TEXT);
        this.RegisterMapping("CurrentStore", DataTypes.TEXT);
        this.RegisterMapping("CounterCode", DataTypes.TEXT);
        this.RegisterMapping("AssignedOnS", DataTypes.TEXT);
        this.RegisterMapping("CurrentStoreLocation", DataTypes.TEXT);
        this.RegisterMapping("Pmobile1", DataTypes.TEXT);
        this.RegisterMapping("version", DataTypes.TEXT);
        this.RegisterMapping("StoreID", DataTypes.TEXT);
        this.RegisterMapping("LastUpdatedOn", DataTypes.TEXT);
        this.RegisterMapping("FileUrl", DataTypes.TEXT);
        this.RegisterMapping("TrainingType", DataTypes.TEXT);
        this.RegisterMapping("ISPCategory", DataTypes.TEXT);
        this.RegisterMapping("Storetype", DataTypes.TEXT);
    }

    @Override
    public MyInfoModel GetNewObject() {
        // TODO Auto-generated method stub
        return new MyInfoModel();
    }

    @Override
    public String toString() {
        return "MyInfoModel{" +
                "ISPName='" + ISPName + '\'' +
                ", UserID='" + UserID + '\'' +
                ",V5ID='"+V5ID+'\''+
                ", EmployeeCode='" + EmployeeCode + '\'' +
                ", CurrentStore='" + CurrentStore + '\'' +
                ", CounterCode='" + CounterCode + '\'' +
                ", AssignedOnS='" + AssignedOnS + '\'' +
                ", CurrentStoreLocation='" + CurrentStoreLocation + '\'' +
                ", Pmobile1='" + Pmobile1 + '\'' +
                ", version='" + version + '\'' +
                ", StoreID='" + StoreID + '\'' +
                ", LastUpdatedOn='" + LastUpdatedOn + '\'' +
                ", FileUrl='" + FileUrl + '\'' +
                ", ISPCategory='" + ISPCategory + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ISPName);
        dest.writeString(UserID);
        dest.writeString(V5ID);
        dest.writeString(EmployeeCode);
        dest.writeString(CurrentStore);
        dest.writeString(CounterCode);
        dest.writeString(AssignedOnS);
        dest.writeString(CurrentStoreLocation);
        dest.writeString(Pmobile1);
        dest.writeString(version);
        dest.writeString(StoreID);
        dest.writeString(LastUpdatedOn);
        dest.writeString(FileUrl);
        dest.writeString(ISPCategory);
    }
}
