package app.core.sqllite;

public class SyntextProvider {

	// / Generate String Corresponding to Create Table Statement Using Table
	// Information
	public static String CreateTableStatement(TableInfo _tableinfo) {
		String result = "Create Table " + _tableinfo.Name + " (";

		int ncols = _tableinfo.Columns.size();
		int i = 0;
		ColumnInfo col;
		for (i = 0; i < ncols - 1; i++) {
			col = _tableinfo.Columns.get(i);
			result += col.Name + " "
					+ SyntextProvider.DataTypeConverter(col.DataType) + ", ";
		}
		col = _tableinfo.Columns.get(i);
		result += col.Name + " "
				+ SyntextProvider.DataTypeConverter(col.DataType) + ")";
		return result;
	}

	// /
	public static String DataTypeConverter(DataTypes _Type) {
		if (_Type == DataTypes.INT)
			return "INT NOT NULL";
		else if (_Type == DataTypes.INT_NULL)
			return "INT";
		else if (_Type == DataTypes.TEXT)
			return "TEXT";
		else if (_Type == DataTypes.INTEGER_PRIMARY_KEY)
			return "INTEGER PRIMARY KEY";
		return "";
	}
}
