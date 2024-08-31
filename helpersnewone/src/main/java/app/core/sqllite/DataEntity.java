package app.core.sqllite;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.UUID;

public abstract class DataEntity<T extends DataEntity<T>> {
    public TableInfo tableInfo;
    String TableName;
    public String guid;
    public boolean IsSuccess = false;
    public String Message = "";
    public String Code = "";

    public DataEntity() {
    }

    public void setMessage(String message) {
        this.Message = message;
        this.IsSuccess = false;
    }

    public void setMessage(String message, String ErrorCode) {
        this.Message = message;
        this.IsSuccess = false;
    }

    public void setMessage(String message, boolean IsSuccess) {
        this.Message = message;
        this.IsSuccess = IsSuccess;
    }

    public DataEntity(String _Name) {
        tableInfo = new TableInfo();
        UUID sd = UUID.randomUUID();
        guid = sd.toString();
        this.SetTableName(_Name);
        this.TableName = _Name;
        this.RegisterMapping("guid", DataTypes.TEXT);
        this.RegisterMappings();
    }

    public TableInfo GetTableInfo() {
        return tableInfo;
    }

    public void SetTableName(String Name) {
        this.tableInfo.Name = Name;
    }

    public void RegisterMapping(String _ColName, DataTypes _Type) {
        tableInfo.Columns.add(new ColumnInfo(_ColName, _Type));
    }

    public void RegisterMapping(String _ColName, DataTypes _Type, String PropertyName) {
        tableInfo.Columns.add(new ColumnInfo(_ColName, _Type, PropertyName));
    }

    public abstract void RegisterMappings();

    public abstract T GetNewObject();

    public Object GetValue(String PropertyName) {
        try {
            return this.getClass().getField(PropertyName).get(this);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void SetValue(String PropName, Object value) {
        try {
            this.getClass().getField(PropName).set(this, value);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ContentValues GetContentValues() {
        ContentValues values = new ContentValues();
        for (ColumnInfo col : tableInfo.Columns) {
            Object retVal = this.GetValue(col.PropName);
            if (retVal == null) continue;
            if (col.DataType == DataTypes.INT)
                values.put(col.Name, (Integer) retVal);
            if (col.DataType == DataTypes.TEXT)
                values.put(col.Name, (String) retVal);
        }
        return values;
    }

    public static MySQLiteOpenHelper db;

    public static void SetDB(MySQLiteOpenHelper _db) {
        db = _db;
    }

    public void InsertOrUpdate() {
        T old_obj = this.Single("guid", this.guid);
        if (old_obj == null)
            db.Insert(this.TableName, this.GetContentValues());
        else
            db.UpdateEntry(this.TableName, this.GetContentValues(), "guid = ?", new String[]{this.guid});
    }

    public void UpdateEntr(ContentValues values, String whereClause, String[] whereArgs) {
        db.UpdateEntry(this.TableName, values, whereClause, whereArgs);
    }

    public void Delete() {
        db.DeleteObj(this.TableName, this.guid);
    }

    public void UpdateFromCursor(Cursor cursor) {
        for (ColumnInfo col : tableInfo.Columns) {
            if (col.DataType == DataTypes.INT)
                this.SetValue(col.Name, cursor.getInt(cursor.getColumnIndex(col.Name)));
            if (col.DataType == DataTypes.TEXT)
                this.SetValue(col.Name, cursor.getString((cursor.getColumnIndex(col.Name))));
        }
    }

    public T GetNewObject(Cursor cursor) {
        T newObj = this.GetNewObject();
        for (ColumnInfo col : tableInfo.Columns) {
            if (col.DataType == DataTypes.INT)
                ((DataEntity<T>) newObj).SetValue(col.Name, cursor.getInt(cursor.getColumnIndex(col.Name)));
            if (col.DataType == DataTypes.TEXT)
                ((DataEntity<T>) newObj).SetValue(col.Name, cursor.getString((cursor.getColumnIndex(col.Name))));
        }
        return newObj;
    }

    public T Single(String PropertyName, Object Value) {
        return db.FetchDataByUniqueProp(this.GetNewObject(), PropertyName, Value);
    }
}
