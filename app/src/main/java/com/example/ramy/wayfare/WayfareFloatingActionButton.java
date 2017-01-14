//package com.example.ramy.wayfare;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.os.Build;
//import android.support.annotation.RequiresApi;
//import android.support.design.widget.FloatingActionButton;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.RelativeLayout;
//
///**
// * Created by Ramy on 1/11/2017.
// */
//
//public class WayfareFloatingActionButton extends FrameLayout{
//    int fragment;
//    Context context;
//    FloatingActionButton btn_main;
//    FloatingActionButton btn_one;
//    FloatingActionButton btn_two;
//    FloatingActionButton btn_three;
//
//    public WayfareFloatingActionButton(Context context) {
//        super(context);
//        this.context = context;
//        this.setWillNotDraw(false);
//    }
//
//    public WayfareFloatingActionButton(Context context, AttributeSet attrs){
//        super(context, attrs);
//        this.context = context;
//        this.setWillNotDraw(false);
//    }
//    public WayfareFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr){
//        super(context, attrs, defStyleAttr);
//        this.context = context;
//        this.setWillNotDraw(false);
//
//    }
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public WayfareFloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
//        super(context, attrs, defStyleAttr, defStyleRes);
//        this.context = context;
//        this.setWillNotDraw(false);
//    }
//
//    public void setFragment(int fragment){
//        this.fragment = fragment;
//        btn_main = new FloatingActionButton(context);
//        btn_one = new FloatingActionButton(context);
//        btn_two = new FloatingActionButton(context);
//        btn_main.setSize(FloatingActionButton.SIZE_NORMAL);
//        btn_one.setSize(FloatingActionButton.SIZE_MINI);
//        btn_two.setSize(FloatingActionButton.SIZE_MINI);
//
//
//        if (fragment == AccountGeneral.PROFILE_FRAGMENT || fragment == AccountGeneral.MYPROFILE_FRAGMENT) {
//            btn_three = new FloatingActionButton(context);
//            btn_three.setSize(FloatingActionButton.SIZE_MINI);
//        }
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec,
//                             int heightMeasureSpec){
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (btn_main.getParent()!=null)
//            ((ViewGroup)btn_main.getParent()).removeView(btn_main);
//        if (btn_one.getParent()!=null)
//            ((ViewGroup)btn_one.getParent()).removeView(btn_one);
//        if (btn_two.getParent()!=null)
//            ((ViewGroup)btn_two.getParent()).removeView(btn_two);
//        addView(btn_one);
//        addView(btn_two);
////        addView(btn_three);
//        addView(btn_main);
//        Log.d("ramy","wfr");
//
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//    }
//
////    @Override
////    protected void onVisibilityChanged(View changedView, int visibility) {
////        if (visibility == View.VISIBLE) {
////            btn_main.setVisibility(View.VISIBLE);
////            btn_one.setVisibility(View.VISIBLE);
////            btn_two.setVisibility(View.VISIBLE);
////            if (btn_three != null)
////                btn_three.setVisibility(View.VISIBLE);
////        }else{
////            btn_main.setVisibility(View.GONE);
////            btn_one.setVisibility(View.GONE);
////            btn_two.setVisibility(View.GONE);
////            if (btn_three != null)
////                btn_three.setVisibility(View.GONE);
////        }
////        super.onVisibilityChanged(changedView, visibility);
////    }
//}
