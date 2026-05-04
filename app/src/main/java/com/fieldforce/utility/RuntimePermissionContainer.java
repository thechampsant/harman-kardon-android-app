package com.fieldforce.utility;

public class RuntimePermissionContainer {

    private static String[] permissionsRequiredForCheckIn;
    private static String[] permissionsRequiredForCheckOut;

    public static String[] getPermissionsForCheckIn() {
        return permissionsRequiredForCheckIn = new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA,
        };
    }

    public static String[] getPermissionsForCheckOut() {
        return permissionsRequiredForCheckOut = new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.CAMERA,
        };
    }

    public static String[] getBackgroundLocationPermission() {
        return new String[]{
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        };
    }
}
