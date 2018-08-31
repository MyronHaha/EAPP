package com.hyhscm.myron.eapp.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hyhscm.myron.eapp.R;

/**
 * Created by Jason on 2017/12/12.
 */

public class CustomedPopu extends PopupWindow {

    Context context;
    PopupWindow popupWindow;

    public CustomedPopu(Context context) {
        super(context);
        this.context = context;
    }

    public CustomedPopu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomedPopu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public PopupWindow getPopupView(final View view) {
        if (popupWindow == null) {
            LinearLayout layout = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (getDisplayParm()[1] * 0.6));
            view.setLayoutParams(params);
            layout.addView(view);
            layout.setBackgroundColor(Color.argb(60, 0, 0, 0));
            popupWindow = new PopupWindow(layout, getDisplayParm()[0], getDisplayParm()[1]);
            popupWindow.setAnimationStyle(R.style.popwin_anim_style);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
         return popupWindow;

    }
    public int[] getDisplayParm(){
        int[] point = new int[2];
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        point[0] = wm.getDefaultDisplay().getWidth();
        point[1] = wm.getDefaultDisplay().getHeight();
        return  point;
    }
//
//    if (Build.VERSION.SDK_INT < 24) {
//        popWindow.showAsDropDown(parent,0,60);
//    } else {
//        int[] a = new int[2];
//        parent.getLocationInWindow(a);
//        popWindow.showAtLocation(getWindow().getDecorView(), Gravity.NO_GRAVITY, 0, parent.getHeight()+a[1]+60);
//        popWindow.update();
//    }
}