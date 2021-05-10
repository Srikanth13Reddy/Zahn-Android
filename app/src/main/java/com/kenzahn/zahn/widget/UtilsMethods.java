package com.kenzahn.zahn.widget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.kenzahn.zahn.R;
import com.kenzahn.zahn.utils.FontManager;

public class UtilsMethods {
    public static boolean internetCheck(Context context) {
        if (!verifiyConnection(context)) {
            initToast(context, "Please check the internet connection");
            return false;
        }
        return true;
    }

    public static void initToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private static boolean verifiyConnection(Context context) {
        Boolean network = false;
        ConnectivityManager conectivtyManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert conectivtyManager != null;
        if (conectivtyManager.getActiveNetworkInfo() != null) {
            NetworkInfo var4 = conectivtyManager.getActiveNetworkInfo();
            if (var4.isAvailable()) {
                var4 = conectivtyManager.getActiveNetworkInfo();
                if (var4.isConnected()) {
                    network = true;
                }
            }
        }
        return network;
    }

    public static final void changeTabsFont(TabLayout tabLayout, Context context) {
        View var10000 = tabLayout.getChildAt(0);
        ViewGroup vg = (ViewGroup) var10000;
        int tabsCount = vg.getChildCount();
        int j = 0;
        for (int var6 = tabsCount; j < var6; ++j) {
            var10000 = vg.getChildAt(j);
            ViewGroup vgTab = (ViewGroup) var10000;
            int tabChildsCount = vgTab.getChildCount();
            int i = 0;

            for (int var10 = tabChildsCount; i < var10; ++i) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(FontManager.getTypeface(context, context.getString(R.string.font_bold)));
                    ((TextView) tabViewChild).setAllCaps(false);
                }
            }
        }

    }
}
