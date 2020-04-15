package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kalac.
 * @date 2020/4/15 22:58
 */
public class MainViewPagerIndicator extends FrameLayout implements ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<View> mTitleView;

    public MainViewPagerIndicator(@NonNull Context context) {
        this(context,null);
    }

    public MainViewPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MainViewPagerIndicator(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bindViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(this);
    }

    public void setIndicatorTitle(List<String> titleList) {
        mTitleView = new ArrayList<>();
        for (int i = 0; i < titleList.size(); i++) {
            TextView view = createTitleView(i, titleList.get(i));
            mTitleView.add(view);
            addView(view);
        }

        invalidate();
    }

    private TextView createTitleView(int index, String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(12);
        textView.setTextColor(Color.parseColor("#ccc"));
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToPosition(index);
            }
        });
        return textView;
    }

    private void changeToPosition(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
