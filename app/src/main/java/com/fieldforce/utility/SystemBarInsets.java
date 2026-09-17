package com.fieldforce.utility;

import android.app.Activity;

/**
 * @deprecated Prefer {@link com.ariston.training_module.utility.SystemBarInsets}
 * so app + training share one implementation.
 */
public final class SystemBarInsets {

	private SystemBarInsets() {
	}

	public static void install(Activity activity) {
		com.ariston.training_module.utility.SystemBarInsets.install(activity);
	}
}
