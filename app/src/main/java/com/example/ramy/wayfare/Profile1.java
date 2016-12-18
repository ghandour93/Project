package com.example.ramy.wayfare;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;;import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.ramy.wayfare.R.attr.layoutManager;

public class Profile1 extends Fragment implements AppBarLayout.OnOffsetChangedListener{

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsToolBarVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private float mAvatarMaxSize;
    private static float mStartToolbarPosition;

    static Context context;

    private AppBarLayout appbar;
    private CollapsingToolbarLayout collapsing;
    private ImageView coverImage;
    private FrameLayout framelayoutTitle;
    private LinearLayout linearlayoutTitle;
    private LinearLayout lineartoolbar;
    private static Toolbar toolbar;
    private TextView name;
    private TextView location;
    private SimpleDraweeView avatar;
    private RelativeLayout relativelay;
    private TextView following;
    private TextView followers;
    private RelativeLayout relativeprof;
    public JSONObject profile = null;
    private Fragment currentFrag;
    boolean bool;

    private static int mStartYPosition;
    private static int mFinalYPosition;
    private static int finalHeight;
    private static int mStartHeight;
    private static int finalHeight1;
    private static int mStartHeight1;
    private static int finalHeight2;
    private static int mStartHeight2;
    private static int mStartXPosition;
    private static int mFinalXPosition;
    private static int mStartXPosition1;
    private static int mFinalXPosition1;
    private static int mStartXPosition2;
    private static int mFinalXPosition2;
    private static float z;
    private static float m;
    private static float n;
    private static int x;
    Bundle b;
    Bundle c;



    /**
     * Find the Views in the layout
     * Auto-created on 2016-03-03 11:32:38 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews(View view) {
        appbar = (AppBarLayout) view.findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) view.findViewById(R.id.collapsingToolbarLayout);
        name = (TextView) view.findViewById(R.id.name);
        location = (TextView) view.findViewById(R.id.location);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        avatar = (SimpleDraweeView) view.findViewById(R.id.avatar);
        relativelay = (RelativeLayout) view.findViewById(R.id.relativelay);
        following = (TextView) view.findViewById(R.id.following);
        followers = (TextView) view.findViewById(R.id.followers);
        relativeprof = (RelativeLayout) view.findViewById(R.id.relativeprof);

    }



    public Profile1() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_profile1, container, false);
        findViews(rootView);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getChildFragmentManager()));
        viewPager.setCurrentItem(0);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        context = getActivity();
        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        b=new Bundle();
        bool = false;

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.ic_launcher).build();
        avatar.setImageURI(imageRequest.getSourceUri());

        c = getArguments();
        if (c != null)
            bool = c.containsKey("notmine");
        new AsyncTask<String, Void, Intent>() {

            ServerTask s = new ServerTask(getActivity());

            @Override
            protected Intent doInBackground(String... params) {
                try {
                    if (bool) {
                        profile = s.getProfile(c.getString("userprofile"));
                    } else {
                        profile = s.getProfile("qoqo");
                    }

                } catch (Exception e) {

                }
                return null;
            }
        }.execute();
        while (true) {
            if (profile != null) {
                loadProfileData(profile);
                break;
            }
        }

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = null;
                Class fragmentClass = List1.class;
                try {
                    fragment1 = (Fragment) fragmentClass.newInstance();
                    b.putInt("id", 1);
                    fragment1.setArguments(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment1).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment1 = null;
                Class fragmentClass = List1.class;
                try {
                    fragment1 = (Fragment) fragmentClass.newInstance();
                    b.putInt("id", 2);
                    fragment1.setArguments(b);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment1).addToBackStack(null);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
            }
        });

        return rootView;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        float maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / maxScroll;

//        Log.d("mah", String.valueOf(percentage));
//        Log.d("gety", String.valueOf(avatar.getY()));
//        Log.d("getx", String.valueOf(avatar.getX()));
        ScaleText(name, location, percentage);
        ImageAnimation(avatar,1f - percentage, Math.abs(offset));
        TextAnimation1(name, 1f - percentage, Math.abs(offset));
        TextAnimation2(location, 1f - percentage, Math.abs(offset));
//        handleAlphaOnTitle(percentage);
//        handleToolbarTitleVisibility(percentage);

        Log.d("Off", String.valueOf(offset));

    }


    private void ScaleText(TextView textView1, TextView textView2, float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.88f, 1f, 0.88f, textView1.getWidth() / 2, textView1.getHeight() / 2);
                scaleAnimation.setDuration(ALPHA_ANIMATIONS_DURATION);
                scaleAnimation.setFillAfter(true);
                startAlphaAnimation(relativelay, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                textView1.startAnimation(scaleAnimation);
                textView2.startAnimation(scaleAnimation);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(0.88f, 1f, 0.88f, 1f, textView1.getWidth() / 2, textView1.getHeight() / 2);
                scaleAnimation.setDuration(ALPHA_ANIMATIONS_DURATION);
                scaleAnimation.setFillAfter(true);
                startAlphaAnimation(relativelay, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                textView1.startAnimation(scaleAnimation);
                textView2.startAnimation(scaleAnimation);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
//                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION+200, View.VISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(linearlayoutTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
//                startAlphaAnimation(toolbar, ALPHA_ANIMATIONS_DURATION+200, View.INVISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }


    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public void TextAnimation1(TextView textView, float expandedPercentageFactor, float dist) {
        InitPropertiesText1(textView);
        if (expandedPercentageFactor <= 1 && expandedPercentageFactor != m) {
//            float distanceXToSubtract = ((mStartXPosition1 - mFinalXPosition1)
//                    * (1f - expandedPercentageFactor));
            float q = expandedPercentageFactor <= 0.6 ? (float) 0.4 * dist : (1f - expandedPercentageFactor) * dist;
            //noinspection ResourceType
            textView.setTranslationY(dist);
//            textView.setX(mStartXPosition1 - distanceXToSubtract);
            Log.d("q", String.valueOf(q));
            m = expandedPercentageFactor;

        }
    }

    public void TextAnimation2(TextView textView, float expandedPercentageFactor, float dist) {
        InitPropertiesText2(textView);
        if (expandedPercentageFactor <= 1 && expandedPercentageFactor != n) {
//            float distanceXToSubtract = ((mStartXPosition2 - mFinalXPosition2)
//                    * (1f - expandedPercentageFactor));
            float q = expandedPercentageFactor <= 0.6 ? (float) 0.4 * dist : (1f - expandedPercentageFactor) * dist;
            //noinspection ResourceType
            textView.setTranslationY(dist);
//            textView.setX(mStartXPosition2 - distanceXToSubtract);
//            if(expandedPercentageFactor<0.5){
//                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            Log.d("q", String.valueOf(q));
            n = expandedPercentageFactor;

        }
    }

    public void ImageAnimation(SimpleDraweeView avatar, float expandedPercentageFactor, float dist) {
        InitPropertiesImage(avatar, dist);
        Log.d("ramy", "ramy");
        if (expandedPercentageFactor <= 1 && expandedPercentageFactor != z) {
            Log.d("ramy", "ramy");

            float distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                    * (1f - expandedPercentageFactor)) + (avatar.getWidth() / 2);

            float heightToSubtract = ((mStartHeight - finalHeight) * (1f - expandedPercentageFactor));
            float q = expandedPercentageFactor <= 0.6 ? (float) 0.4 * dist : (1f - expandedPercentageFactor) * dist;
            //noinspection ResourceType
            avatar.setTranslationY(q);
            avatar.setX(mStartXPosition - distanceXToSubtract);
            Log.d("setX", String.valueOf(mStartXPosition - distanceXToSubtract));
            Log.d("setY", String.valueOf(q));

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) avatar.getLayoutParams();
            lp.width = (int) (mStartHeight - heightToSubtract);
            lp.height = (int) (mStartHeight - heightToSubtract);
            avatar.setLayoutParams(lp);
            z = expandedPercentageFactor;

        }
    }

    @SuppressLint("PrivateResource")
    private void InitPropertiesImage(SimpleDraweeView child, float dist) {
        if (mStartYPosition == 0)
            mStartYPosition = (int) (child.getY());
        Log.d("StartYPos", String.valueOf(mStartYPosition));
        if (mFinalYPosition == 0)
            mFinalYPosition = (child.getHeight() / 2);
        Log.d("FinalYPos", String.valueOf(mFinalYPosition));

        if (mStartHeight == 0)
            mStartHeight = child.getHeight();

        if (finalHeight == 0)
            finalHeight = context.getResources().getDimensionPixelOffset(R.dimen.image_small_width);

        if (mStartXPosition == 0)
            mStartXPosition = (int) (child.getX() + (child.getWidth() / 2));

//        if (mFinalXPosition == 0)
        mFinalXPosition = context.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (finalHeight / 2);

        if (mStartToolbarPosition == 0)
            mStartToolbarPosition = child.getY() + (child.getHeight() / 2);
        Log.d("StartTool", String.valueOf(mStartToolbarPosition));
//            mStartToolbarPosition = dependency.getY() + (dependency.getHeight()/2);
    }

    @SuppressLint("PrivateResource")
    private void InitPropertiesText1(TextView child) {
        if (mStartHeight1 == 0)
            mStartHeight1 = child.getHeight();

        if (finalHeight1 == 0)
            finalHeight1 = child.getHeight();
        if (mStartXPosition1 == 0)
            mStartXPosition1 = (int) (child.getX() + (child.getWidth() / 2));

        if (mFinalXPosition1 == 0)
            mFinalXPosition1 = ((LinearLayout) child.getParent()).getWidth() - (context.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (child.getWidth() / 2));
    }

    private void InitPropertiesText2(TextView child) {
        if (mStartHeight2 == 0)
            mStartHeight2 = child.getHeight();

        if (finalHeight2 == 0)
            finalHeight2 = context.getResources().getDimensionPixelOffset(R.dimen.image_small_width);
        if (mStartXPosition2 == 0)
            mStartXPosition2 = (int) (child.getX() + (child.getWidth() / 2));
//        mFinalXPosition2=((LinearLayout)child.getParent()).getWidth()-context.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material);
        if (mFinalXPosition2 == 0)
            mFinalXPosition2 = ((LinearLayout) child.getParent()).getWidth() - (context.getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (child.getWidth() / 2));
//            Log.d("finalx", String.valueOf(mFinalXPosition2));
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void loadProfileData(JSONObject obj) {
        try {
            String username = obj.getString("user");
            String location1 = obj.getString("location");
            name.setText(username);
            location.setText(location1);
            following.setText(obj.getString("following_count"));
            followers.setText(obj.getString("followers_count"));
            JSONArray x = obj.getJSONArray("following");
            JSONArray y = obj.getJSONArray("followers");
            int length = x.length();
            ArrayList<String> followinglist = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                followinglist.add(x.getString(i));
            }
            length = y.length();
            ArrayList<String> followerslist = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                followerslist.add(y.getString(i));
            }
            b.putStringArrayList("following", followinglist);
            b.putStringArrayList("followers", followerslist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
