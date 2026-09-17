package com.ariston.training_module.utility;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ariston.training_module.R;

/**
 * targetSdk 35+ draws edge-to-edge. Call {@link #install(Activity)} after
 * setContentView on every Activity that does not already inset via InnosolsActivity.
 */
public final class SystemBarInsets {

	private static final int STATUS_BAR_COLOR = 0xFF00458E;
	private static final int NAV_BAR_COLOR = 0xFF000000;

	private SystemBarInsets() {
	}

	public static void install(Activity activity) {
		if (activity == null) {
			return;
		}
		WindowCompat.setDecorFitsSystemWindows(activity.getWindow(), false);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
			activity.getWindow().setNavigationBarColor(NAV_BAR_COLOR);
		}

		final View content = activity.findViewById(android.R.id.content);
		if (content == null) {
			return;
		}

		ViewCompat.setOnApplyWindowInsetsListener(content, (v, windowInsets) -> {
			Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
			ViewGroup frame = (ViewGroup) v;
			View root = frame.getChildCount() > 0 ? frame.getChildAt(0) : null;
			if (root == null) {
				return WindowInsetsCompat.CONSUMED;
			}

			boolean handledHeader = false;
			handledHeader |= insetContainer(findByName(root, activity, "toolbaar"), bars.top);
			handledHeader |= insetContainer(firstNonNull(
					root.findViewById(R.id.iv_backView),
					findByName(root, activity, "iv_backView")), bars.top);
			handledHeader |= insetMarginTop(firstNonNull(
					root.findViewById(R.id.iv_back),
					findByName(root, activity, "iv_back")), bars.top);
			handledHeader |= growHeightByInset(findByName(root, activity, "rl_header"), bars.top);
			handledHeader |= growHeightByInset(firstNonNull(
					root.findViewById(R.id.iv_bg_top),
					findByName(root, activity, "iv_bg_top")), bars.top);
			handledHeader |= growHeightByInset(firstNonNull(
					root.findViewById(R.id.iv_bg_top_tr_dsc),
					findByName(root, activity, "iv_bg_top_tr_dsc")), bars.top);

			if (handledHeader) {
				if (root.getPaddingLeft() != bars.left
						|| root.getPaddingRight() != bars.right
						|| root.getPaddingBottom() != bars.bottom) {
					root.setPadding(bars.left, root.getPaddingTop(), bars.right, bars.bottom);
				}
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					activity.getWindow().setStatusBarColor(STATUS_BAR_COLOR);
				}
				if (root.getPaddingLeft() != bars.left
						|| root.getPaddingTop() != bars.top
						|| root.getPaddingRight() != bars.right
						|| root.getPaddingBottom() != bars.bottom) {
					root.setPadding(bars.left, bars.top, bars.right, bars.bottom);
				}
			}
			return WindowInsetsCompat.CONSUMED;
		});
		ViewCompat.requestApplyInsets(content);
	}

	private static View firstNonNull(View a, View b) {
		return a != null ? a : b;
	}

	private static View findByName(View root, Activity activity, String idName) {
		int id = activity.getResources().getIdentifier(idName, "id", activity.getPackageName());
		if (id == 0) {
			id = activity.getResources().getIdentifier(idName, "id", activity.getPackageName() + ".debug");
		}
		// Also try library namespace for training layouts merged into app
		if (id == 0) {
			try {
				id = root.getResources().getIdentifier(idName, "id", "com.ariston.training_module");
			} catch (Exception ignored) {
			}
		}
		if (id == 0) {
			return null;
		}
		return root.findViewById(id);
	}

	private static boolean insetContainer(View view, int insetTop) {
		if (view == null) {
			return false;
		}
		int pl = view.getPaddingLeft();
		int pr = view.getPaddingRight();
		int pb = view.getPaddingBottom();
		if (view.getPaddingTop() != insetTop) {
			view.setPadding(pl, insetTop, pr, pb);
		}
		growHeightByInset(view, insetTop);
		return true;
	}

	private static boolean insetMarginTop(View view, int insetTop) {
		if (view == null) {
			return false;
		}
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (!(lp instanceof ViewGroup.MarginLayoutParams)) {
			return insetContainer(view, insetTop);
		}
		ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) lp;
		Object tag = view.getTag(R.id.tag_system_bar_base_height);
		int base;
		if (tag instanceof Integer) {
			base = (Integer) tag;
		} else {
			base = mlp.topMargin;
			view.setTag(R.id.tag_system_bar_base_height, base);
		}
		int desired = base + insetTop;
		if (mlp.topMargin != desired) {
			mlp.topMargin = desired;
			view.setLayoutParams(mlp);
		}
		return true;
	}

	private static boolean growHeightByInset(View view, int insetTop) {
		if (view == null) {
			return false;
		}
		ViewGroup.LayoutParams lp = view.getLayoutParams();
		if (lp == null || lp.height <= 0) {
			return true;
		}
		Object tag = view.getTag(R.id.tag_system_bar_base_height);
		int base;
		if (tag instanceof Integer) {
			base = (Integer) tag;
		} else {
			base = lp.height;
			view.setTag(R.id.tag_system_bar_base_height, base);
		}
		int desired = base + insetTop;
		if (lp.height != desired) {
			lp.height = desired;
			view.setLayoutParams(lp);
		}
		return true;
	}
}
