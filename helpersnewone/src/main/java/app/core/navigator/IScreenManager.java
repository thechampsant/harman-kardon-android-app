package app.core.navigator;


public interface IScreenManager {	
	
	void OnActivate(InnosolsActivity baseActivity, Object arg0);
	int GetObjectID();
	boolean IsHome();
	boolean IsSelected(Boolean value);
}
