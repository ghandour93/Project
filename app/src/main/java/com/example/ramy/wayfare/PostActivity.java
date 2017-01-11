package com.example.ramy.wayfare;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PostActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ViewPager vp_post = (ViewPager) findViewById(R.id.vp_post);
//        vp_post.setAdapter(new PostAdapter(getSupportFragmentManager()));
//        vp_post.setCurrentItem(0);
        findViewById(R.id.iv_leftarrow).setVisibility(View.GONE);
//        vp_post.setPageTransformer(true, new Transformer());
//        vp_post.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                switch (position){
//                    case 0:
//                        findViewById(R.id.iv_leftarrow).setVisibility(View.GONE);
//                        return;
//                    case 1:
//                        findViewById(R.id.iv_leftarrow).setVisibility(View.VISIBLE);
//                        findViewById(R.id.iv_rightarrow).setVisibility(View.VISIBLE);
//                        return;
//                    case 2:
//                        findViewById(R.id.iv_rightarrow).setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_out_down);
    }

    private class PostAdapter extends FragmentPagerAdapter {

        public PostAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PostTextFragment();
                case 1:
                    return new PostImageFragment();
                case 2:
                    return new PostTextFragment();
                default:
                    return new ProfileFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position)
            {
                case 0: return "TextPost";
                case 1: return "ImagePost";
                case 2: return "Yahia";
                default : return "Feed";
            }
        }
    }

//    private class Transformer implements ViewPager.PageTransformer{
//        @Override
//        public void transformPage(View page, float position) {
//            TextView tv_titleimage = (TextView) page.findViewById(R.id.tv_titleimage);
//            int pageWidth = page.getWidth();
//            Log.d("id",String.valueOf(page.getId()));
//            Log.d("pos", String.valueOf(position));
//            if (position < -1) { // [-Infinity,-1)
//                // This page is way off-screen to the left.
//                page.setAlpha(0);
//
//            } else if (position <= 1) { // [-1,1]
////                if (position < 0) {
////                        tv_title.setTranslationX(-((1 - position) * pageWidth));
////                        Log.d("ramy", "<0");
////                    Log.d("ramy", String.valueOf(tv_title.getId()));
////                } else {
//                if (tv_titleimage!=null) {
//                    tv_titleimage.setTranslationX((-position) * (pageWidth/2));
//                    tv_titleimage.setAlpha(1.0f -Math.abs(2*position));
//                    Log.d("ramy", ">0");
//                }
//                if (tv_titletext!=null) {
//                    tv_titletext.setTranslationX((-position) * (pageWidth/2));
//                    tv_titletext.setAlpha(1.0f-Math.abs(2*position));
//                    Log.d("ramy", ">0");
//                }
//
//            } else { // (1,+Infinity]
//                // This page is way off-screen to the right.
//                page.setAlpha(0);
//            }
//        }
//    }

}
