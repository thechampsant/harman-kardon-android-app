package app.core.sqllite;

public class ColumnInfo {
	public String Name;
	public String PropName;
	public DataTypes DataType;
	
	public ColumnInfo()
	{
		
	}
	
	/**
	 * 
	 * @param _name 
	 * @param _Type
	 */
	public ColumnInfo(String _name, DataTypes _Type)
	{
		this.Name = _name;
		this.DataType = _Type;
		this.PropName = _name;
	}
	
	public ColumnInfo(String _name, DataTypes _Type, String _PropName)
	{
		this.Name = _name;
		this.DataType = _Type;
		this.PropName = _PropName;
	}
	
}
