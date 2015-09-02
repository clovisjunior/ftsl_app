package br.org.ftsl.sliding;

import android.support.v4.app.Fragment;

public class SamplePagerItem {
	
	private final CharSequence mTitle;
    private final int mIndicatorColor;
    private final int mDividerColor;
    private final int mPosition;
        
    private Fragment[] mListFragments;

    public SamplePagerItem(int position, CharSequence title, int indicatorColor, int dividerColor, Fragment[] listFragments) {
        this.mTitle = title;
        this.mPosition = position;
        this.mIndicatorColor = indicatorColor;
        this.mDividerColor = dividerColor;

        this.mListFragments = listFragments;
    }

    public Fragment createFragment() {
		return mListFragments[mPosition];
    }

    public CharSequence getTitle() {
        return mTitle;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public int getDividerColor() {
        return mDividerColor;
    }
}
