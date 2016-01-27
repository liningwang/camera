package com.example.util;

import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class AppFilter {

	public static Spanned getHtml (String text) {
		return Html.fromHtml(text);
	}
	
	/* used by list classes */
	public static void setHtml (TextView tv, String text) {
		if (true) {
		} else {
			tv.setText(text);
		}
	}
}