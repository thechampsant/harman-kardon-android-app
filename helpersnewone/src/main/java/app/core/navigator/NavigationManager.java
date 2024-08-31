package app.core.navigator;

import java.util.Stack;

import android.content.pm.ActivityInfo;


public class NavigationManager {
	InnosolsActivity BaseActivity;
	
	public IScreenManager HomeScreen;
	public IScreenManager CurrentScreen;
	
	public Stack<IScreenManager> Screens = new Stack<IScreenManager>();
	
	// Construction
	public NavigationManager(InnosolsActivity _activity)
	{
		BaseActivity = _activity;
	}
	
	public void NavigateTo(IScreenManager _screen) {
		this.NavigateTo(_screen,null);
	}
	
	// Navigate to Desired Screen and Also Maintain Stack of Screens History.
	public void NavigateTo(IScreenManager _screen, Object arg0) {

		IScreenManager last = null;
		
		if(Screens.size()>0) 		//In Case Stack is not Empty.
		{
			last = Screens.peek();	// Extract Last Screen from Stack of Screens.
			
			//Add into stack if Last one is not the same.
			if (!last.equals(_screen)) //Check If Last one is not the same.
			{
				MakeIsSelectedFalseInAll();
				_screen.IsSelected(true);
				Screens.push(_screen);// Add Screens into Stack of Screens. 
			}
		}
		
		else //In case of Stack is Empty. 
		{
			MakeIsSelectedFalseInAll();
			_screen.IsSelected(true);
			Screens.push(_screen);
		}
		
		//Navigate to _screen 
		BaseActivity.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		BaseActivity.setContentView(_screen.GetObjectID());		
		_screen.OnActivate(BaseActivity,arg0);
		CurrentScreen = _screen;
	}
	
	private void MakeIsSelectedFalseInAll() {
		// TODO Auto-generated method stub

		for (IScreenManager screen : Screens) {
			screen.IsSelected(false);
		}
	}
	
	public void NavigateToHome()
	{
		if(this.HomeScreen != null)
			this.NavigateTo(HomeScreen);
	}
}
