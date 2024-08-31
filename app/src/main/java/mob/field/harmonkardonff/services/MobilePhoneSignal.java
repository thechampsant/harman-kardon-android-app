package mob.field.harmonkardonff.services;

import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;

public class MobilePhoneSignal extends PhoneStateListener {
	public static final int UNKNOW_CODE = 99;
	int MAX_SIGNAL_DBM_VALUE = 31;
	public int signalStrengthPercent;
	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		super.onSignalStrengthsChanged(signalStrength);

		if (null != signalStrength
				&& signalStrength.getGsmSignalStrength() != UNKNOW_CODE) {
			signalStrengthPercent = calculateSignalStrengthInPercent(signalStrength
					.getGsmSignalStrength());
			
		}
	}

	private int calculateSignalStrengthInPercent(int signalStrength) {
		return (int) ((float) signalStrength / MAX_SIGNAL_DBM_VALUE * 100);
	}
}
