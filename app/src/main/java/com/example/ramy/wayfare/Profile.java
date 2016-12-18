package com.example.ramy.wayfare;

import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.accounts.AccountManager.KEY_ERROR_MESSAGE;
import static com.example.ramy.wayfare.MainActivity.PARAM_USER_PASS;

public class Profile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

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
    JSONObject profile = null;
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
    private void findViews() {
        appbar = (AppBarLayout) findViewById(R.id.appbar);
        collapsing = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
//        coverImage = (ImageView)findViewById( R.id.main_imageview_placeholder);
//        framelayoutTitle = (FrameLayout)findViewById( R.id.main_framelayout_title);
//        linearlayoutTitle = (LinearLayout)findViewById( R.id.main_linearlayout_title);
//        lineartoolbar = (LinearLayout) findViewById(R.id.lineartoolbar);
        name = (TextView) findViewById(R.id.name);
        location = (TextView) findViewById(R.id.location);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        avatar = (SimpleDraweeView) findViewById(R.id.avatar);
//        textviewTitle = (TextView)findViewById( R.id.main_textview_title);
//        textviewSubtitle= (TextView) findViewById(R.id.main_textview_subtitle);

        relativelay = (RelativeLayout) findViewById(R.id.relativelay);
        following = (TextView) findViewById(R.id.following);
        followers = (TextView) findViewById(R.id.followers);
        relativeprof = (RelativeLayout) findViewById(R.id.relativeprof);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);
        findViews();

        context = this;

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(this.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        toolbar.setTitle("");
        appbar.addOnOffsetChangedListener(this);
        b = new Bundle();
        bool = false;

        setSupportActionBar(toolbar);
//        startAlphaAnimation(textviewTitle, 0, View.INVISIBLE);
//        startAlphaAnimation(textviewSubtitle, 0, View.INVISIBLE);
//        startAlphaAnimation(toolbar, 0, View.INVISIBLE);
//        startAlphaAnimation(lineartoolbar, 0, View.INVISIBLE);

        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithResourceId(R.drawable.ic_launcher).build();
        avatar.setImageURI(imageRequest.getSourceUri());

        Fragment fragment1 = null;
        Class fragmentClass = fragmentOne.class;
        try {
            fragment1 = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFrag = fragment1;
        fragmentManager.beginTransaction().replace(R.id.coord, fragment1).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        selectDrawerItem(item);
                        return true;
                    }
                });

        //noinspection ResourceType
//        avatar.setTranslationY(-240);
//        avatar.setX(100);
//        avatar.setY(300);

        //set avatar and cover
        c = getIntent().getExtras();
        if (c != null)
            bool = c.containsKey("notmine");
        new AsyncTask<String, Void, Intent>() {

            ServerTask s = new ServerTask(Profile.this);

            @Override
            protected Intent doInBackground(String... params) {

                //Log.d("REGISTER BRO", TAG + "> Started authenticating");
//                Log.i("bool", String.valueOf(getIntent().getExtras()));

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
        Log.i("profile", String.valueOf(profile));
        while (true) {
            if (profile != null) {
                loadProfileData(profile);
                break;
            }
        }

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, List.class);
                b.putInt("id", 1);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, List.class);
                b.putInt("id", 2);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (menuItem.getItemId()) {
            case R.id.action_favorites:

                fragmentClass = fragmentOne.class;
                break;
            case R.id.action_schedules:
                break;
            case R.id.action_music:
                fragmentClass = fragmentOne.class;
                break;
            default:
                fragmentClass = fragmentOne.class;
        }
            if (fragmentClass !=null) {
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                fragmentManager.beginTransaction().replace(R.id.frame, fragment).commit();
                currentFrag = fragment;
            }else if (currentFrag !=null){
                fragmentManager.beginTransaction().remove(currentFrag).commit();
                currentFrag = null;
            }
        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
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
