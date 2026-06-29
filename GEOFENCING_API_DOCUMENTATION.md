# Geofencing API Documentation

## API Endpoint
**URL:** `http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation`

**Method:** GET

**Purpose:** Track user's geofence entry/exit events when they move beyond 200m radius from their attendance marking location

---

## Request Parameters

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| LoginId | String | Yes | User's login ID from MyInfo | "EMP12345" |
| StoreId | String | Yes | Store ID where attendance was marked | "STORE001" |
| Lat | Double | Yes | Current GPS latitude (full decimal precision) | 28.4582884 |
| Long | Double | Yes | Current GPS longitude (full decimal precision) | 77.687662 |
| timestap | String | Yes | Timestamp of the event (yyyy-MM-dd HH:mm:ss) | "2024-01-15 14:30:45" |
| distance | Double | Yes | Distance in METERS beyond 200m boundary (0 if within boundary) | 50.9966526 |
| transitionType | String | Yes | Type of geofence event: "EXIT" or "ENTER" | "EXIT" |
| AppVersion | String | Yes | App version from package info | "1.0.5" |
| Source | String | Yes | Platform identifier | "Android" |

---

## Sample API Calls

### Example 1: User exits 200m boundary (250m from center)
```
GET http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation?LoginId=EMP12345&StoreId=STORE001&Lat=28.4582884&Long=77.687662&timestap=2024-01-15 14:30:45&distance=250.9966526&transitionType=EXIT&AppVersion=1.0.5&Source=Android
```

**Explanation:**
- User was at store location (center point)
- User walked 250 meters away from store
- Distance = 250.9966526 meters (total distance from center)
- Event type = EXIT (user left the 200m zone)

### Example 2: User returns to 200m boundary (180m from center)
```
API call is SKIPPED - User is within 200m boundary
```

**Explanation:**
- User returned and is now 180m from store (within 200m boundary)
- Distance = 180m (less than 200m threshold)
- Event type = ENTER (user came back to the 200m zone)
- **API call is SKIPPED because user is within 200m boundary**

### Example 3: User exits again (220m from center)
```
GET http://harman.infield.co.in/ISPMobile/SaveISPGeoLocation?LoginId=EMP12345&StoreId=STORE001&Lat=28.4585000&Long=77.688000&timestap=2024-01-15 16:10:30&distance=220.3456789&transitionType=EXIT&AppVersion=1.0.5&Source=Android
```

**Explanation:**
- User left the boundary again
- Distance = 220.3456789 meters (total distance from center)
- Event type = EXIT

---

## Distance Calculation Logic

### Formula:
```
distance = distanceBetween(storeLocation, currentGPSLocation)  // in meters

if (distance <= 200) {
    // User is within 200m boundary
    // API call is SKIPPED
} else {
    // User is beyond 200m boundary
    // API call is made with total distance from center
}
```

### Examples:
| Actual Distance from Store | Distance Parameter | API Called? |
|----------------------------|-------------------|-------------|
| 180m | N/A | ❌ No (skipped) |
| 200m | N/A | ❌ No (skipped) |
| 210m | 210.0 | ✅ Yes |
| 250.5m | 250.5 | ✅ Yes |
| 403.9966526m | 403.9966526 | ✅ Yes |

---

## Geofence Behavior

### When Geofencing Starts:
- User marks attendance with option = **"Present"** (P)
- Geofence is created with 200m radius at attendance marking location
- Geofence expires automatically at 11:59 PM (midnight)

### When Geofencing Stops:
- User logs out from the app
- User checks out (marks attendance out)
- Geofence expires at 11:59 PM (midnight)
- Date changes (old geofence becomes invalid)

### Event Triggers:
1. **EXIT Event:** Triggered when user crosses 200m boundary going OUT
2. **ENTER Event:** Triggered when user crosses 200m boundary coming IN

### Offline Queue System:
- If internet is not available when event triggers, the API call is queued
- When internet becomes available, all queued events are synced automatically
- Queue is stored in SharedPreferences as JSON

---

## Expected Response

### Success Response:
```json
{
  "status": "success",
  "message": "Geolocation saved successfully"
}
```

**HTTP Status Code:** 200

### Error Response:
```json
{
  "status": "error",
  "message": "Error description"
}
```

---

## Technical Details

### Coordinate Precision:
- Latitude/Longitude: Full decimal precision (typically 7-10 decimal places)
- Distance: Full decimal precision in METERS (e.g., 50.9966526)
- No rounding is applied

### Distance Calculation Method:
- Uses Android's `Location.distanceTo()` method
- Implements Haversine formula
- Returns distance in METERS between two GPS coordinates

### Timestamp Format:
- Format: `yyyy-MM-dd HH:mm:ss`
- Timezone: Device local time
- Example: `2024-01-15 14:30:45`

### Location Providers:
- GPS_PROVIDER (primary)
- NETWORK_PROVIDER (fallback)
- Uses most recent location from available providers

---

## App Implementation Notes

### Files Involved:
1. **GeofenceBroadcastReceiver.java** - Handles geofence events and API calls
2. **OfflineQueueManager.java** - Manages offline queue
3. **NetworkChangeReceiver.java** - Syncs queue when internet returns
4. **MarkAttendanceActivity.java** - Starts geofencing on attendance marking

### SharedPreferences Keys:
**GeofencePrefs:**
- `login_id` - User's login ID
- `store_id` - Store ID
- `lat` - Center point latitude (store location)
- `lng` - Center point longitude (store location)
- `start_date` - Date when geofence was created (yyyy-MM-dd)

**GeofenceQueue:**
- `pending_requests` - JSON array of queued API calls

### Permissions Required:
- ACCESS_FINE_LOCATION
- ACCESS_COARSE_LOCATION
- ACCESS_BACKGROUND_LOCATION (Android 10+)
- INTERNET
- ACCESS_NETWORK_STATE

---

## Testing Scenarios

### Scenario 1: Normal Flow
1. User marks attendance as "Present" at store location
2. Geofence starts with 200m radius
3. User walks 250m away → EXIT event → API called with distance=250
4. User returns to 180m → ENTER event → API skipped (within 200m)
5. User walks 220m away → EXIT event → API called with distance=220

### Scenario 2: Offline Flow
1. User marks attendance as "Present"
2. User turns on Airplane mode
3. User walks 300m away → EXIT event → Queued (distance=300)
4. User returns to 150m → ENTER event → Skipped (within 200m)
5. User turns off Airplane mode → Queued event synced automatically

### Scenario 3: App Restart
1. User marks attendance and geofence is active
2. User closes/kills the app
3. User walks 250m away → EXIT event still triggers (Android System handles it)
4. User restarts device → Geofence restored automatically via BootReceiver

### Scenario 4: Midnight Expiration
1. User marks attendance at 2:00 PM
2. Geofence expires at 11:59 PM automatically
3. After midnight, no events are triggered
4. Old geofence data is cleared

---

## Important Notes for Backend Team

1. **Distance is the TOTAL distance from store location**
   - Distance = Total distance from center point to current GPS location
   - Example: User at 250m from store → distance = 250m (NOT 50m)
   - API is only called when distance > 200m

2. **API calls are SKIPPED when user is within 200m**
   - If distance ≤ 200m, no API call is made
   - This applies to both EXIT and ENTER events

3. **Lat/Long in API are CURRENT GPS location**
   - NOT the center point (store location)
   - Center point is stored in SharedPreferences for distance calculation

4. **Full decimal precision is sent**
   - No rounding applied to coordinates or distance
   - Example: 203.9966526 (not 204)

5. **Events can arrive out of order**
   - Due to offline queue sync
   - Check timestamp to determine actual sequence

6. **Multiple events per day**
   - User can exit/enter multiple times
   - Each event generates separate API call

7. **Geofence auto-expires at midnight**
   - No events after 11:59 PM
   - New geofence created on next day's attendance marking

---

## Contact
For any questions or clarifications, contact the mobile development team.

**Last Updated:** January 2024
**API Version:** 1.0
**App Version:** Compatible with all versions
