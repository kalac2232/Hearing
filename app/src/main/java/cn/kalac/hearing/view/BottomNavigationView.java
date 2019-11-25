package cn.kalac.hearing.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.kalac.hearing.R;
import cn.kalac.hearing.utils.DensityUtil;

/**
 * @author kalac.
 * @date 2019/10/26 14:28
 */
public class BottomNavigationView extends FrameLayout {

    private ImageView mIcon;
    private TextView mText;
    private boolean isSelected;

    public BottomNavigationView(@NonNull Context context) {
        this(context,null);
    }

    public BottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BottomNavigationView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_bottom_navigation,this,true);
        initView();
    }

    private void initView() {
        mIcon = findViewById(R.id.iv_bottom_icon);
        mText = findViewById(R.id.tv_bottom_text);
    }

    public void select(boolean selected) {
        isSelected = selected;

        if (selected) {
            int padding = DensityUtil.dip2px(getContext(), 5);
            mIcon.setPadding(padding,padding,padding,padding);
            mIcon.setBackgroundResource(R.drawable.shape_dragonball_bg);
            mIcon.setColorFilter(Color.WHITE);
        } else {

        }
    }
}
