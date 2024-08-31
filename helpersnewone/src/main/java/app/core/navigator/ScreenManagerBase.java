package app.core.navigator;

public abstract class ScreenManagerBase<T> implements IScreenManager {
	
	public InnosolsActivity BaseActivity;
	
	@SuppressWarnings("unchecked")
	@Override
	public void OnActivate(InnosolsActivity baseActivity, Object arg0) {
		// TODO Auto-generated method stub
		this.BaseActivity = baseActivity;
		if(arg0 != null)
			this.SetObject((T)arg0);
		Activate((T)arg0);
	}
	
	
	public abstract void Activate(T arg0);


	@Override
	public boolean IsHome() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean IsSelected(Boolean value) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public abstract void SetObject(T arg0);
	public abstract T GetObject();

}
