package app.core.sqllite;

import java.util.ArrayList;

import android.database.Cursor;

public class TableInfo {
	
	public String Name;
	public ArrayList<ColumnInfo> Columns;
	public Class<? extends Object> BaseClass;
	
	public TableInfo()
	{
		Columns = new ArrayList<ColumnInfo>();
	}
	
	public String ToString()
	{
		return null;
	}
	
	public <T> T GetObject(T Object, Cursor cursor, String[] params)
	{
		return null;
	}
}
