package com.reference.apublic.parallax;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    protected String TAG = getClass().getSimpleName();
    private ArrayList<ParallaxFragment> fragments;
    private ImageView mIvPeople;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectLayoutInflater();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUp(R.layout.view_intro_1, R.layout.view_intro_2, R.layout.view_intro_3,
                R.layout.view_intro_4, R.layout.view_intro_5, R.layout.view_login);
        initView();
    }

    private final String[] prefixs = {
            "android.widget.", "android.view."
    };

    private void injectLayoutInflater() {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new LayoutInflaterFactory() {

            @Override
            public View onCreateView(View parent, String name, Context context,
                                     AttributeSet attrs) {
                View view = null;
                /*try {*/
                    AppCompatDelegate delegate = getDelegate();
                    LayoutInflaterFactory factory = (LayoutInflaterFactory) delegate;
                    view = factory.onCreateView(parent, name, context, attrs);


                    for (int i = 0; i < attrs.getAttributeCount(); i++) {
                        String attributeName = attrs.getAttributeName(i);
                        String attributeValue = attrs.getAttributeValue(i);
                        Log.d(TAG, "attributeName = " + attributeName + " attributeValue = "
                                + attributeValue);
                    }

                    /*if (view == null) {
                        if (-1 == name.indexOf('.')) {
                            try {
                                view = getLayoutInflater()
                                        .createView(name, "android.widget.", attrs);
                            } catch (ClassNotFoundException e) {
                                view = getLayoutInflater().createView(name, "android.view.", attrs);
                            }
                        } else {
                            view = getLayoutInflater().createView(name, null, attrs);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "parent = " + parent + " name = " + name + " view = " + view);*/
                return view;
            }
        });
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_vp_bg);
        mViewPager.setAdapter(new ParallaxAdapter(getSupportFragmentManager(), fragments));
        mViewPager.addOnPageChangeListener(this);
        mIvPeople = (ImageView) findViewById(R.id.main_iv_people);
    }

    public void setUp(int... ids) {
        fragments = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ParallaxFragment f = new ParallaxFragment();
            Bundle args = new Bundle();
            args.putInt("layoutId", ids[i]);
            f.setArguments(args);
            fragments.add(f);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int containerWidth = mViewPager.getWidth();
        ParallaxFragment inFragment = null;
        try {
            inFragment = fragments.get(position - 1);
        } catch (Exception e) {
        }
        ParallaxFragment outFragment = null;
        try {
            outFragment = fragments.get(position);
        } catch (Exception e) {
        }

        if (inFragment != null) {
            List<View> views = inFragment.getAnimViews();
            for (int i = 0; i < views.size(); i++) {
                View view = views.get(i);
                ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                Log.i("ricky", "tag_in:" + tag);
                if (tag == null) {
                    continue;
                }
                view.setTranslationX((containerWidth - positionOffsetPixels) * tag.xIn);
                view.setTranslationY((containerWidth - positionOffsetPixels) * tag.yIn);

            }
        }
        if (outFragment != null) {
            List<View> views = outFragment.getAnimViews();
            for (int i = 0; i < views.size(); i++) {
                View view = views.get(i);
                ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                Log.i("ricky", "tag_out:" + tag);
                if (tag == null) {
                    continue;
                }
                view.setTranslationX((-positionOffsetPixels) * tag.xOut);
                view.setTranslationY((-positionOffsetPixels) * tag.yOut);

            }
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case SCROLL_STATE_IDLE:
                stopWalk();
                break;
            case SCROLL_STATE_DRAGGING:
            case SCROLL_STATE_SETTLING:
                startWalk();
                break;
            default:
                break;
        }
    }

    private void stopWalk() {
        Drawable drawable = mIvPeople.getBackground();
        if (drawable instanceof Animatable) {
            Animatable animatable = (Animatable) drawable;
            if (animatable.isRunning()) {
                animatable.stop();
            }
        }
    }

    private void startWalk() {
        Drawable drawable = mIvPeople.getBackground();
        if (drawable instanceof Animatable) {
            Animatable animatable = (Animatable) drawable;
            if (!animatable.isRunning()) {
                animatable.start();
            }
        }
    }

    public interface LayoutInflateListener {
        void onInflate(View parent, String name, Context context, AttributeSet attrs,
                       View inflateView);
    }
}
