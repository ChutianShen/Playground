package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.R;
import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.activities.RegisterActivity;
import com.example.kevin_sct.beastchat.services.LiveAccountServices;

import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by kevin_sct on 4/24/17.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R.id.fragment_login_userEmail)
    EditText mUserEmailEt;

    @BindView(R.id.fragment_login_userPassword)
    EditText mUserPasswordEt;

    @BindView(R.id.fragment_login_login_button)
    Button mLoginButton;

    @BindView(R.id.fragment_login_register_button)
    Button mRegisterButton;

    private Unbinder mUnbinder;
    private Socket mSocket;

    private BaseFragmentActivity mActivity;
    private LiveAccountServices mLiveAccountServices;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(CONSTANT.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(LoginFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }

        mLiveAccountServices = LiveAccountServices.getInstance();
        mSocket.on("token", tokenListener());

        mSocket.connect();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login,container,false);
        mUnbinder= ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.fragment_login_login_button)
    public void setmLoginButton(){
        mCompositeSubscription.add(mLiveAccountServices.sendLoginInfo(mUserEmailEt
                ,mUserPasswordEt,mSocket,mActivity));
    }

    @OnClick(R.id.fragment_login_register_button)
    public void setmRegisterButton(){
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    private Emitter.Listener tokenListener(){
        return new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                mCompositeSubscription.add(mLiveAccountServices
                        .getAuthToken(jsonObject,mActivity,mSharedPreferences));
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
