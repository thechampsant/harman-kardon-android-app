package app.core.base;

public abstract class IDB  {

	public app.core.sqllite.MySQLiteOpenHelper db;
	@SuppressWarnings("rawtypes")
	public BaseService service;
	
	@SuppressWarnings("rawtypes")
	public IDB(BaseService service)
	{
		db=service.getDataContextDb();
		this.service=service;
	}
}
