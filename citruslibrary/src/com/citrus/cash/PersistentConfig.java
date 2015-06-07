package com.citrus.cash;

import android.content.Context;
import android.content.SharedPreferences;

public class PersistentConfig {
	private static final String PREFS_NAME = "prefs_file";

	private SharedPreferences settings;

	public PersistentConfig(Context context) {

	settings = context.getSharedPreferences(PREFS_NAME, 0);

	}

	public String getCookieString() {

	return settings.getString("my_cookie", "");

	}

	public void setCookie(String cookie) {

	SharedPreferences.Editor editor = settings.edit();

	editor.putString("my_cookie", cookie);

	editor.commit();

	}
}
