package app.core.gcm;


import android.app.IntentService;
import android.os.Bundle;
import app.core.utils.NotificationHandler;

public interface IGcm {

	public void getGcmExtra(IntentService service,Bundle bundle,NotificationHandler nhandler);
}
