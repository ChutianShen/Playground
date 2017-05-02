package com.example.kevin_sct.beastchat.Utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;

/**
 * Created by kevin_sct on 5/2/17.
 */

public class MarshMellowPermisson {

    private static final int EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE =10;

    private static final int EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE =11;

    private static final int CAMERA_PERMISSION_REQUEST_CODE =12;


    private BaseFragmentActivity mActivity;

    public MarshMellowPermisson(BaseFragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    public boolean checkPermissionForReadExternalStorage(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public boolean checkPermissionForWriteExternalStorage(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }
    public boolean checkPermissionForCamera(){
        int result = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }


    public void requestPermissionForReadExternalStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(mActivity," External Storage permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    ,EXTERNAL_STORAGE_READ_PERMISSION_REQUEST_CODE);
        }
    }


    public void requestPermissionForWriteExternalStorage(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(mActivity," Write Storage permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    ,EXTERNAL_STORAGE_WRITE_PERMISSION_REQUEST_CODE);
        }
    }

    public void requestPermissionForCamera(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.CAMERA)){
            Toast.makeText(mActivity," Camera permission is needed. Please turn it on inside the settings"
                    ,Toast.LENGTH_SHORT).show();
        } else{
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.CAMERA}
                    ,CAMERA_PERMISSION_REQUEST_CODE);
        }
    }
}
