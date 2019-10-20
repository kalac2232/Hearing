package cn.kalac.hearing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author kalac.
 * @date 2019/10/20 22:56
 */
public class PunctuateIndicator extends View {
    public PunctuateIndicator(Context context) {
        this(context,null);
    }

    public PunctuateIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PunctuateIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //初始化画笔
    }

    public void bindViewPager(ViewPager viewPager) {

        if (viewPager == null) {
            return;
        }
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            return;
        }
        //获取相关数据


        //设置滑动监听，当滑动完成后切换指示器的位置
        initViewPagerListener(viewPager);
    }

    private void initViewPagerListener(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndicatorPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setIndicatorPosition(int position) {

    }
}
