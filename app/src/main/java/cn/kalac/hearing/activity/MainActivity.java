package cn.kalac.hearing.activity;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.LoginResultBean;
import cn.kalac.hearing.javabean.RecommendSongsBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;

public class MainActivity extends BaseActivity {


    private View mBtnLogin;
    private View mBtnGetList;
    private View mBtnJump;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    protected void initData() {
        refreshLoginState();
    }

    @Override
    protected void initView() {
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnGetList = findViewById(R.id.btn_getList);
        mBtnJump = findViewById(R.id.btn_jumpTOPlay);
    }

    @Override
    protected void addListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getPhoneLoginUrl("17684721017","GHN,.155070");

                HttpHelper.getInstance().get(url, new HttpCallback<LoginResultBean>() {

                    @Override
                    public void onSuccess(LoginResultBean loginResultBean) {
                        Toast.makeText(mContext,"登录成功"+loginResultBean.getProfile().getNickname(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(mContext,"登录失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mBtnGetList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getRecommendSongsUrl();
                Log.i(TAG, "onClick: "+url);
                HttpHelper.getInstance().get(url, new HttpCallback<RecommendSongsBean>() {

                    @Override
                    public void onSuccess(RecommendSongsBean recommendSongsBean) {
                        Toast.makeText(mContext,"获取了"+recommendSongsBean.getRecommend().size()+"个数据",Toast.LENGTH_SHORT).show();
                        //Log.i(TAG, "onSuccess: "+recommendSongsBean);
                    }

                    @Override
                    public void onFailed(String string) {
                        Toast.makeText(mContext,"获取失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        mBtnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivty(PlayMusicActivity.class);
            }
        });
    }

    /**
     * 刷新登录状态
     */
    private void refreshLoginState() {
        String url = ApiHelper.getRefreshUrl();

        HttpHelper.getInstance().get(url, new HttpCallback<String>() {

            @Override
            public void onSuccess(String s) {

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(s);
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        Toast.makeText(mContext,"刷新登录状态成功"+s,Toast.LENGTH_SHORT).show();
                    } else {
                        String msg = jsonObject.getString("msg");
                        Toast.makeText(mContext,msg + s,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailed(String string) {
                Toast.makeText(mContext,"刷新登录状态错误"+string,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
