package cn.kalac.hearing.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.kalac.hearing.R;
import cn.kalac.hearing.api.ApiHelper;
import cn.kalac.hearing.javabean.net.LoginResultBean;
import cn.kalac.hearing.net.HttpCallback;
import cn.kalac.hearing.net.HttpHelper;

public class MineFragment extends Fragment {
    private View mBtnLogin;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_mine, container, false);
        initView(view);

        addListener();
        return view;
    }

    private void initView(View view) {
        mBtnLogin = view.findViewById(R.id.btn_login);
    }

    protected void addListener() {
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = ApiHelper.getPhoneLoginUrl("17684721017","qweasdzxc");

                HttpHelper.getInstance().get(url, new HttpCallback<LoginResultBean>() {

                    @Override
                    public void onResultSuccess(LoginResultBean loginResultBean) {
                        Toast.makeText(getContext(),"登录成功"+loginResultBean.getProfile().getNickname(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResultFailed(String string) {
                        Toast.makeText(getContext(),"登录失败"+string,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
