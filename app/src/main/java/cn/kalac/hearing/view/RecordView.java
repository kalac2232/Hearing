package cn.kalac.hearing.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import cn.kalac.hearing.R;


/*
 * Created by Kalac on 2019/2/7
 */

public class RecordView extends RelativeLayout {

    private View mRootView;

    public RecordView(Context context) {
        this(context,null);

    }

    public RecordView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        // 导入布局
        mRootView = LayoutInflater.from(context).inflate(R.layout.recordview_layout, this, true);
    }
}
