package com.flmboost.lagfix;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private LinearLayout content;
    private TextView ramInfo, storageInfo, status;
    private final int CYAN = Color.rgb(0, 217, 233);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(8,12,22));
        getWindow().setNavigationBarColor(Color.rgb(8,12,22));
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(8,12,22));
        content = new LinearLayout(this);
        content.setOrientation(LinearLayout.VERTICAL);
        content.setPadding(dp(22),dp(32),dp(22),dp(30));
        scroll.addView(content);
        setContentView(scroll);

        TextView title = text("FLM BOOST", 32, CYAN, true);
        content.addView(title);
        content.addView(text("LAG FIX  •  PERFORMANCE ASSISTANT", 12, Color.rgb(151,164,190),false));
        space(28);
        content.addView(text("DEVICE STATUS", 14, CYAN,true));
        space(12);
        LinearLayout panel = panel();
        ramInfo = text("RAM: Đang kiểm tra...", 17, Color.WHITE,true);
        storageInfo = text("Bộ nhớ: Đang kiểm tra...", 15, Color.LTGRAY,false);
        panel.addView(ramInfo);
        panel.addView(storageInfo);
        panel.addView(text("Android " + Build.VERSION.RELEASE + "  •  " + Build.MANUFACTURER + " " + Build.MODEL, 13, Color.LTGRAY,false));
        space(22);
        content.addView(text("QUICK BOOST", 14, CYAN,true));
        space(12);
        addButton("⚡  BOOST & REFRESH", () -> boost());
        space(12);
        addButton("♫  MỞ FL STUDIO MOBILE", () -> launchFLM());
        space(12);
        addButton("⚙  CÀI ĐẶT PIN", () -> openSettings(Settings.ACTION_BATTERY_SAVER_SETTINGS));
        space(12);
        addButton("◉  CÀI ĐẶT ỨNG DỤNG", () -> openSettings(Settings.ACTION_APPLICATION_SETTINGS));
        space(20);
        status = text("Sẵn sàng. Hãy đóng ứng dụng không cần thiết trước khi làm nhạc.", 14, Color.rgb(170,185,204),false);
        content.addView(status);
        space(24);
        content.addView(text("BOOST làm mới số liệu và gợi ý tối ưu. Android không cho ứng dụng này tự dọn RAM của app khác hay ép xung CPU.", 12, Color.rgb(119,135,156),false));
        refreshStats();
    }

    private void boost() {
        refreshStats();
        status.setText("✓ Đã làm mới thông tin. Để giảm giật: đóng app nền, tắt tiết kiệm pin khi cần, và giảm số plugin/track trong FL Studio Mobile.");
        status.setTextColor(CYAN);
    }

    private void refreshStats() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        if (am != null) {
            am.getMemoryInfo(info);
            ramInfo.setText(String.format(Locale.US,"RAM khả dụng: %.1f / %.1f GB", info.availMem/1073741824.0,info.totalMem/1073741824.0));
        }
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            storageInfo.setText(String.format(Locale.US,"Bộ nhớ trống: %.1f GB",stat.getAvailableBytes()/1073741824.0));
        } catch (Exception e) { storageInfo.setText("Không đọc được bộ nhớ"); }
    }

    private void launchFLM() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.imageline.FLM");
        if (intent != null) {
            startActivity(intent);
            status.setText("Đang mở FL Studio Mobile...");
        } else {
            status.setText("Không tìm thấy FL Studio Mobile. Hãy cài ứng dụng hoặc mở thủ công.");
        }
    }
    private void openSettings(String action) {
        try { startActivity(new Intent(action)); }
        catch (Exception e) { startActivity(new Intent(Settings.ACTION_SETTINGS)); }
    }
    private LinearLayout panel() {
        LinearLayout p = new LinearLayout(this);
        p.setOrientation(LinearLayout.VERTICAL);
        p.setPadding(dp(18),dp(16),dp(18),dp(16));
        p.setBackgroundResource(com.flmboost.lagfix.R.drawable.panel);
        content.addView(p);
        return p;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView v = new TextView(this);
        v.setText(value);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setPadding(0,dp(5),0,dp(5));
        if (bold) v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
        return v;
    }
    private void addButton(String value, Runnable action) {
        Button b = new Button(this);
        b.setText(value);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTextColor(Color.rgb(8,12,22));
        b.setBackgroundResource(R.drawable.button);
        b.setGravity(Gravity.CENTER);
        b.setOnClickListener(v -> action.run());
        content.addView(b, new LinearLayout.LayoutParams(-1,dp(56)));
    }
    private void space(int h) { View v = new View(this); content.addView(v,new LinearLayout.LayoutParams(1,dp(h))); }
    private int dp(int value) { return (int)(value*getResources().getDisplayMetrics().density+0.5f); }
}
