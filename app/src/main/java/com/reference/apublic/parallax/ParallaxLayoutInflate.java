package com.reference.apublic.parallax;

import android.content.Context;
import android.view.LayoutInflater;

/**
 * Created by byang059 on 8/10/17.
 */

public class ParallaxLayoutInflate extends LayoutInflater {

    private final LayoutInflater mOriginal;

    protected ParallaxLayoutInflate(LayoutInflater original, Context newContext) {
        super(original, newContext);
        mOriginal = original;
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflate(mOriginal,newContext);
    }

    public LayoutInflater getOriginal() {
        return mOriginal;
    }
}
