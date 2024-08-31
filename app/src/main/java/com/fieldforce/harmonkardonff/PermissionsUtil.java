package com.fieldforce.harmonkardonff;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
/**
 * Created by Amar on 19/4/16.
 */
public class PermissionsUtil
{
    public static final int READ_STORAGE_PERMISSION_ID = 1;
    public static final int WRITE_STORAGE_PERMISSION_ID = 2;
    public static final int CAMERA_PERMISSION_ID=3;
    public static final int READ_PHONE_STATE_ID=4;
    public static final int ACCESS_LOCATION_PERMISSION_ID=5;

    public static boolean isMarshMellowAndAbove()
    {
           return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static boolean isPermissionHas(Context context, String permission)
    {
           return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

}
