package app.core.ibase;

public abstract class IEvent<T> {
	public  abstract void Completed(IService<T> sender,T args);
	public Object result=null;
	
}
