package com.reference.apublic.parallax;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class ParallaxAdapter extends FragmentPagerAdapter {

	private List<ParallaxFragment> fragments;
	public ParallaxAdapter(FragmentManager fm,List<ParallaxFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int p) {
		// TODO Auto-generated method stub
		return fragments.get(p);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragments.size();
	}

}
