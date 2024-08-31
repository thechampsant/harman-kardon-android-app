package app.core.sqllite;

import linq.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {

	String DataBaseName;
	public ArrayList<TableInfo> Tables = new ArrayList<TableInfo>();;

	public MySQLiteOpenHelper(Context context, String name, int version,
			ArrayList<TableInfo> _tables) {
		super(context, name, null, version);
		this.DataBaseName = name;
		this.Tables = _tables;
	}

	public String GetDatabaseName() {
		return this.DataBaseName;
	}

	public TableInfo GetTableInfo(String _name) {
		for (TableInfo info : this.Tables) {
			if (info.Name.equals(_name))
				return info;
		}
		return null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		for (TableInfo table : Tables) {
			db.execSQL(SyntextProvider.CreateTableStatement(table));
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	// Upgrade to Insert or Update
	public void Insert(String tableName, ContentValues values) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			String guid = values.getAsString("guid");

			db.insert(tableName, null, values);

		} finally {
			db.close();
		}
	}

	public void UpdateEntry(String tableName, ContentValues values,

	String whereClause, String[] whereArgs) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.update(tableName, values, whereClause, whereArgs);

		} finally {
			db.close();
		}
	}

	private static final String TAG = "MySQLiteOpenHelperO";
	public <T extends DataEntity<T>> ArrayList<T> FetchAllData(T obj)
	{
		Cursor cursor = null;
		ArrayList<T> list = new ArrayList<T>();
		try {
			String tablename = obj.TableName;
			SQLiteDatabase db = this.getReadableDatabase();
			String sql = "SELECT * FROM " + tablename;
            Log.d(TAG, "FetchAllData: "+sql);
			cursor = db.rawQuery(sql, null);
            Log.d(TAG, "FetchAllData: "+ Arrays.toString(cursor.getColumnNames()));
			list = new ArrayList<T>();

			if (cursor.moveToFirst()) {
				do {
					list.add(obj.GetNewObject(cursor));
				} while (cursor.moveToNext());
			}
			return list;
		}
		catch(Exception ex){
			Log.d(TAG, "FetchAllData: "+ex.getMessage());
		}
		finally {
			if (cursor!=null)
			cursor.close();
		}
		return list;
	}

	/*
	 * public <T extends DataEntity<T>> ArrayList<T> FetchDataByID(T obj, int
	 * id) { SQLiteDatabase db = this.getReadableDatabase(); db.close(); }
	 */

	public <T extends DataEntity<T>> T FetchDataByGUID(T obj, String guid) {
		/*
		 * SQLiteDatabase db = this.getReadableDatabase(); Cursor cursor =
		 * db.rawQuery("Select * from " + obj.TableName + " Where guid="+guid,
		 * null); db.close(); if(cursor.getCount()==0) return null; return
		 * obj.GetNewObject(cursor);
		 */
		return this.FetchDataByUniqueProp(obj, "guid", guid);
	}

	public <T extends DataEntity<T>> T FetchDataByUniqueProp(T obj,
			String Prop, Object PropValue) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = this.getReadableDatabase();
			String[] selectionArgs = { (String) PropValue };
			cursor = db.rawQuery("Select * from " + obj.TableName + " Where "
					+ Prop + " = ?", selectionArgs);

			if (!cursor.moveToLast())
				return null;
			cursor.moveToLast();
			T nobj = obj.GetNewObject(cursor);
			// db.close();
			return nobj;
		} finally {
			cursor.close();
		}
	}

	public <T extends DataEntity<T>> Boolean HasObj(T obj) {
		return this.FetchDataByGUID(obj, obj.guid) != null;
	}

	public void DeleteObj(String TableName, String guid) {
		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.delete(TableName, "guid=?", new String[] { guid });

		} finally {
			db.close();
		}
	}

	public <T extends DataEntity<T>> void DeleteAll(T TableObj) {
		ArrayList<T> data = new ArrayList<T>();
		data = this.FetchAllData(TableObj);
		for (T item : data) {
			this.DeleteObj(TableObj.TableName, item.guid);
		}
	}

	public <T extends DataEntity<T>> void InsertAll(ArrayList<T> data) {
		for (T item : data) {
			item.InsertOrUpdate();
		}
	}
}