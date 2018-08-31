package com.hyhscm.myron.eapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyhscm.myron.eapp.R;

/**
 * Created by Jason on 2017/10/20.
 */

public class MyToast {
        public  static Toast mToast;

        private MyToast(Context context, CharSequence text, int duration) {
            View v = LayoutInflater.from(context).inflate(R.layout.layout_toast, null);
//            v.getBackground().setAlpha(100);
            TextView textView = (TextView) v.findViewById(R.id.textView1);
            textView.setText(text);
            mToast = new Toast(context);
            mToast.setDuration(duration);
            mToast.setView(v);
        }

        public static MyToast makeText(Context context, CharSequence text, int duration) {
               if(mToast != null){
               mToast.cancel();
               }
            return new MyToast(context, text, duration);
        }



        public void show() {
            if (mToast != null) {
                mToast.show();
            }
        }
        public void setGravity(int gravity, int xOffset, int yOffset) {
            if (mToast != null) {
                mToast.setGravity(gravity, xOffset, yOffset);
            }
        }
    }

