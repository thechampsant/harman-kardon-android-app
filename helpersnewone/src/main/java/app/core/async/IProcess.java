package app.core.async;

/**
 * @author Sandeep Duhan says:
 *IProcess interface implement the necassary method to  do background works and handle response from background process 
 * */
public interface IProcess {
	/**
	 * add a a task that you want to do in background
	 * Note:Don't invoke any UI method in this ,it will leads an exception 
	 * @return
	 * return base class Object object as response
	 * @throws InterruptedException 
	 */
	public Object underProcess() throws Exception;
	
	/**
	 * @see
	 * It works on main thread.You can process your response here after casting
	 * like:- boolean IsSuccess=(Boolean)Object
	 * @return Void
	 * @param response
	 * is of Object type returned from underProcess method.
	 * It works on main thread.You can process your response here after casting
	 * like:- boolean IsSuccess=(Boolean)Object
	 */
	public void processResponse(Object response)throws Exception;
	


}
