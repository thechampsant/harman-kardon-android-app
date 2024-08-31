package app.core.installer.manager;

public interface OnInstalledPackaged {
	
	public void packageInstalled(String packageName, int returnCode);

}
