package com.reference.apublic.parallax;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

public class ParallaxFragment extends Fragment implements LayoutInflaterFactory {
    protected String TAG = getClass().getSimpleName();

    private List<View> mViews = new ArrayList<>();
    private int mLayoutId;
    private ParallaxLayoutInflate mParallaxLayoutInflate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mParallaxLayoutInflate = new ParallaxLayoutInflate(inflater, getContext());
        LayoutInflaterCompat.setFactory(mParallaxLayoutInflate, this);
        mViews = new ArrayList<>();
        Bundle bundle = getArguments();
        mLayoutId = bundle.getInt("layoutId");
        return mParallaxLayoutInflate.inflate(mLayoutId, container, false);
    }

    public List<View> getAnimViews() {
        return mViews;
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        View view = null;
        try {
            AppCompatActivity activity = (AppCompatActivity) getActivity();
            AppCompatDelegate delegate = activity.getDelegate();
            LayoutInflaterFactory factory = (LayoutInflaterFactory) delegate;
            view = factory.onCreateView(parent, name, context, attrs);

            for (int i = 0; i < attrs.getAttributeCount(); i++) {
                String attributeName = attrs.getAttributeName(i);
                String attributeValue = attrs.getAttributeValue(i);
                Log.d(TAG,
                        "attributeName = " + attributeName + " attributeValue = " + attributeValue);
            }

            if (view == null) {
                if (-1 == name.indexOf('.')) {
                    try {
                        view = mParallaxLayoutInflate.createView(name, "android.widget.", attrs);
                    } catch (ClassNotFoundException e) {
                        view = mParallaxLayoutInflate.createView(name, "android.view.", attrs);
                    }
                } else {
                    view = mParallaxLayoutInflate.createView(name, null, attrs);
                }
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (view != null) {
            getCustomAttrs(context, attrs, view);
            mViews.add(view);
        }
        Log.d(TAG, "parent = " + parent + " name = " + name + " view = " + view);
        return view;
    }

    private void getCustomAttrs(Context context, AttributeSet attrs, View view) {
        int[] attrIds = {
                R.attr.layout_alphaIn, R.attr.layout_alphaOut, R.attr.layout_xIn, R.attr.layout_xOut, R.attr.layout_yIn, R.attr.layout_yOut,
        };

        TypedArray a = context.obtainStyledAttributes(attrs, attrIds);

        if (a != null && a.length() > 0) {
            ParallaxViewTag tag = new ParallaxViewTag();
            tag.alphaIn = a.getFloat(0, 0f);
            tag.alphaOut = a.getFloat(1, 0f);
            tag.xIn = a.getFloat(2, 0f);
            tag.xOut = a.getFloat(3, 0f);
            tag.yIn = a.getFloat(4, 0f);
            tag.yOut = a.getFloat(5, 0f);
            view.setTag(R.id.parallax_view_tag, tag);
            a.recycle();
        }

    }
}
