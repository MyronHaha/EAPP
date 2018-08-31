package com.hyhscm.myron.eapp.view.morewindow;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.hyhscm.myron.eapp.R;
import com.hyhscm.myron.eapp.activity.User.LoginActivity;
import com.hyhscm.myron.eapp.data.IResultHandler;
import com.hyhscm.myron.eapp.data.Result;
import com.hyhscm.myron.eapp.data.UserInfo;
import com.hyhscm.myron.eapp.net.HttpCore;
import com.hyhscm.myron.eapp.utils.MyToast;
import com.hyhscm.myron.eapp.utils.common;

import me.leefeng.promptlibrary.PromptDialog;

/**
 * Created by Jason on 2017/12/15.
 */

public class MoreWindow extends PopupWindow  {

//    private String TAG = MoreWindow.class.getSimpleName();
    Activity mContext;
    private int mWidth;
    private int mHeight;
    private int statusBarHeight ;
//    private Bitmap mBitmap= null;
//    private Bitmap overlay = null;
//
//    private Handler mHandler = new Handler();

    public MoreWindow(Activity context) {
        mContext = context;
    }

    public void init() {
        Rect frame = new Rect();
        mContext.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        DisplayMetrics metrics = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay()
                .getMetrics(metrics);
        mWidth = metrics.widthPixels;
        mHeight = metrics.heightPixels;

        setWidth(mWidth);
        setHeight(mHeight);
    }

//    private Bitmap blur() {
//        if (null != overlay) {
//            return overlay;
//        }
//        long startMs = System.currentTimeMillis();
//
//        View view = mContext.getWindow().getDecorView();
//        view.setDrawingCacheEnabled(true);
//        view.buildDrawingCache(true);
//        mBitmap = view.getDrawingCache();
//
//        float scaleFactor = 8;//图片缩放比例；
//        float radius = 10;//模糊程度
//        int width = mBitmap.getWidth();
//        int height =  mBitmap.getHeight();
//
//        overlay = Bitmap.createBitmap((int) (width / scaleFactor),(int) (height / scaleFactor),Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(overlay);
//        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
//        Paint paint = new Paint();
//        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//        canvas.drawBitmap(mBitmap, 0, 0, paint);
//
//        overlay = FastBlur.doBlur(overlay, (int) radius, true);
//        Log.i(TAG, "blur time is:"+(System.currentTimeMillis() - startMs));
//        return overlay;
//    }

    private Animation showAnimation1(final View view, int fromY , int toY) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation go = new TranslateAnimation(0, 0, fromY, toY);
        go.setDuration(300);
        TranslateAnimation go1 = new TranslateAnimation(0, 0, -10, 2);
        go1.setDuration(100);
        go1.setStartOffset(250);
        set.addAnimation(go1);
        set.addAnimation(go);

        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

            }

        });
        return set;
    }


    public void showMoreWindow(View anchor,int bottomMargin) {
        final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.center_publish2, null);
        setContentView(layout);

        final LinearLayout close= (LinearLayout) layout.findViewById(R.id.close);
//        android.widget.RelativeLayout.LayoutParams params =new android.widget.RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.bottomMargin = bottomMargin;
//        params.addRule(RelativeLayout.BELOW, R.id.more_window_auto);
//        params.addRule(RelativeLayout.RIGHT_OF, R.id.more_window_collect);
//        params.topMargin = 200;
//        params.leftMargin = 18;
//        close.setLayoutParams(params);

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isShowing()) {
//                    closeAnimation(layout);
                    dismiss();
                }
            }

        });

//        showAnimation(layout);
//        setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), blur()));
        setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(R.color.white)));
        setOutsideTouchable(true);
        setFocusable(true);
        showAtLocation(anchor, Gravity.BOTTOM, 0, statusBarHeight);
    }

//    private void showAnimation(ViewGroup layout){
//
//        for(int i=0;i<layout.getChildCount();i++){
//
//            final View child = layout.getChildAt(i);
//            if(child.getId() == R.id.close){
//                ObjectAnimator animator = ObjectAnimator.ofFloat(child,"rotation",0,180,90);
//                animator.setDuration(1000);
//                animator.start();
//                continue;
//            }
////            child.setOnClickListener(this);
//            child.setVisibility(View.INVISIBLE);
//
//            mHandler.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    child.setVisibility(View.VISIBLE);
//                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
//                    fadeAnim.setDuration(100);
//                    KickBackAnimator kickAnimator = new KickBackAnimator();
//                    kickAnimator.setDuration(150);
//                    fadeAnim.setEvaluator(kickAnimator);
//                    fadeAnim.start();
//                }
//            }, i * 100);
//        }
//
//    }

//    private void closeAnimation(ViewGroup layout){
//
//        for(int i=0;i<layout.getChildCount();i++){
//            final View child = layout.getChildAt(i);
//            if(child.getId() == R.id.close){
//                ObjectAnimator animator = ObjectAnimator.ofFloat(child,"rotation",0,-135);
//                animator.setDuration(500);
//                animator.start();
//                continue;
//            }
//
//            if(child.getId() == R.id.iv_top){
//                continue;
//            }
////            child.setOnClickListener(this);
//            int delay = i*50;
//            child.postDelayed(new Runnable() {
//
//                @Override
//                public void run() {
//                    child.setVisibility(View.VISIBLE);
//                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 0, 600);
//                    fadeAnim.setDuration(1000);
////                    KickBackAnimator kickAnimator = new KickBackAnimator();
////                    kickAnimator.setDuration(100);
////                    fadeAnim.setEvaluator(kickAnimator);
//                    fadeAnim.start();
//                    fadeAnim.addListener(new Animator.AnimatorListener() {
//
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            // TODO Auto-generated method stub
//
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//                            // TODO Auto-generated method stub
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            child.setVisibility(View.INVISIBLE);
//                            dismiss();
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//                            // TODO Auto-generated method stub
//
//                        }
//                    });
//                }
//            }, delay);
//
////            if(child.getId() == R.id.more_window_local){
////                mHandler.postDelayed(new Runnable() {
////
////                    @Override
////                    public void run() {
////                        dismiss();
////                    }
////                }, (layout.getChildCount()-i) * 30 + 80);
////            }
//        }
////         dismiss();
//    }



//    public void destroy() {
//        if (null != overlay) {
//            overlay.recycle();
//            overlay = null;
//            System.gc();
//        }
//        if (null != mBitmap) {
//            mBitmap.recycle();
//            mBitmap = null;
//            System.gc();
//        }
//    }


}