package app.core.gcm;

import android.app.IntentService;
import android.app.admin.DevicePolicyManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class AdminAction {

	public IntentService context;
	Uri alert;
    Ringtone rtone;
	   
	public AdminAction(IntentService _context)
	{
		context=_context;
	}
	
    @SuppressWarnings("static-access")
	public void trylock()
    {
    	DevicePolicyManager dpm = (DevicePolicyManager)context.getSystemService(context.DEVICE_POLICY_SERVICE);
    	Settings.System.putString(context.getContentResolver(), 
    			Settings.System.NEXT_ALARM_FORMATTED, "Screen Locked by Innosols Device Admin! Please call 0129-4077774 for more information!");
    	dpm.lockNow();
    }
    public void trypwchange(String password)
    {
    	DevicePolicyManager dpm = (DevicePolicyManager)context.getSystemService(context.DEVICE_POLICY_SERVICE);
    	dpm.resetPassword(password, 0);
    }
    public void UploadAppInfo(String reqcode)
    {
    	Toast.makeText(this.context, "Trying Upload", Toast.LENGTH_SHORT).show();
    	PackageInfoProvider pp = new PackageInfoProvider(this.context);
    	pp.TryPostData();
    }
    public void RmApp(String pkname)
    {
    	PackageInfoProvider pp = new PackageInfoProvider(this.context);
    	pp.Uninstall(pkname);
    }
    public  void tryring(Boolean isStart)
    {
    	if(!isStart)
    	{
    		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    		rtone = RingtoneManager.getRingtone(this.context, alert);
    		if(rtone.isPlaying())
    			rtone.stop();
    	}else
    	{
    		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
    		rtone = RingtoneManager.getRingtone(this.context, alert);
    		rtone.play();
    		try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		rtone.stop();
    	}
    }
	
	
}
