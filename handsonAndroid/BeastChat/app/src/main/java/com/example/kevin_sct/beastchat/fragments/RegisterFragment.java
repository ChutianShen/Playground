package com.example.kevin_sct.beastchat.fragments;

import android.content.Context;
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

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.fragment_register_userName)
    EditText mUserNameEt;

    @BindView(R.id.fragment_register_userEmail)
    EditText mUserEmailEt;

    @BindView(R.id.fragment_register_userPassword)
    EditText mUserPasswordEt;

    @BindView(R.id.fragment_register_registerButton)
    Button mRegisterButton;

    @BindView(R.id.fragment_register_loginButton)
    Button mLoginButton;

    private Unbinder mUnbinder;
    private Socket mSocket;

    private BaseFragmentActivity mActivity;

    private LiveAccountServices mLiveAccountServices;

    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSocket = IO.socket(CONSTANT.IP_LOCAL_HOST);
        } catch (URISyntaxException e) {
            Log.i(RegisterFragment.class.getSimpleName(), e.getMessage());
            Toast.makeText(getActivity(), "Can't connect to the server", Toast.LENGTH_SHORT).show();
        }

        mSocket.connect();
        mSocket.on("message", accountResponse());

        mLiveAccountServices = LiveAccountServices.getInstance();
    }

    @OnClick(R.id.fragment_register_loginButton)
    public void setmLoginButton(){
        getActivity().finish();
    }

    private Emitter.Listener accoutResponse(){
        return new Emitter.Listener(){

            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                mCompositeSubscription.add(mLiveAccountServices.registerResponse(data, mActivity));
            }
        };
    }

    @OnClick(R.id.fragment_register_registerButton)
    public void setmRegisterButton(){
        mCompositeSubscription.add(mLiveAccountServices.sendRegistrationInfo(
                mUserNameEt,mUserEmailEt,mUserPasswordEt,mSocket
        ));
    }

    private Emitter.Listener accountResponse(){
        return  new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                mCompositeSubscription.add(mLiveAccountServices.registerResponse(data,mActivity));
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity =null;
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
