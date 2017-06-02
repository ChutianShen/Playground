package com.example.kevin_sct.beastchat.services;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kevin_sct.beastchat.Utils.CONSTANT;
import com.example.kevin_sct.beastchat.activities.BaseFragmentActivity;
import com.example.kevin_sct.beastchat.activities.InboxActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.Socket;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by kevin_sct on 4/24/17.
 */

public class LiveAccountServices {

    private static LiveAccountServices mLiveAccountServices;

    private final int USER_ERROR_EMPTY_PASSWORD =1;
    private final int USER_ERROR_EMPTY_EMAIL =2;
    private final int USER_ERROR_EMPTY_USERNAME =3;
    private final int USER_ERROR_PASSWORD_SHORT =4;
    private final int USER_ERROR_EMAIL_BAD_FORMAT =5;


    private final int SERVER_SUCCESS = 6;
    private final int SERVER_FAILURE = 7;

    private final int USER_NO_ERRORS =8;

    public static LiveAccountServices getInstance(){
        if(mLiveAccountServices ==null){
            mLiveAccountServices = new LiveAccountServices();
        }
        return mLiveAccountServices;
    }


    public Subscription changeProfilePhoto(final StorageReference storageReference, Uri uri, final BaseFragmentActivity activity
            , final String currentUserEmail, final ImageView imageView, final SharedPreferences sharedPreferences, final Socket socket){
        Observable<Uri> uriObservable = Observable.just(uri);
        return uriObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<Uri, byte[]>() {
                    @Override
                    public byte[] call(Uri uri) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(),uri);
                            int outPutHeight= (int)(bitmap.getHeight() * (512.0/bitmap.getWidth()));
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,512,outPutHeight,true);      // make the figure smaller
                            ByteArrayOutputStream byteArrayOutPutStream = new ByteArrayOutputStream();
                            scaledBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutPutStream);
                            return byteArrayOutPutStream.toByteArray();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<byte[]>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(byte[] bytes) {
                        UploadTask uploadTask = storageReference.putBytes(bytes);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                JSONObject sendData = new JSONObject();
                                try {
                                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                    sendData.put("email", currentUserEmail);
                                    sendData.put("picUrl", downloadUrl.toString());
                                    socket.emit("userUpdatedPicture", sendData);
                                    sharedPreferences.edit().putString(CONSTANT.USER_PICTURE,downloadUrl.toString()).apply();
                                    Picasso.with(activity).load(downloadUrl.toString())
                                            .into(imageView);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });
    }

    public Subscription getAuthToken(JSONObject data, final BaseFragmentActivity activity, final SharedPreferences sharedPreferences){
        Observable<JSONObject> jsonObservable = Observable.just(data);

        return jsonObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<JSONObject, List<String>>() {
                    @Override
                    public List<String> call(JSONObject jsonObject) {
                        List<String> userDetails = new ArrayList<>();

                        try {
                            JSONObject serverData = jsonObject.getJSONObject("token");
                            String token = (String) serverData.get("authToken");
                            String email = (String) serverData.get("email");
                            String photo = (String) serverData.get("photo");
                            String userName = (String) serverData.get("displayName");

                            userDetails.add(token);
                            userDetails.add(email);
                            userDetails.add(photo);
                            userDetails.add(userName);
                            return userDetails;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return userDetails;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        String token = strings.get(0);
                        final String email = strings.get(1);
                        final String photo = strings.get(2);
                        final String userName = strings.get(3);

                        if (!email.equals("error")){
                            FirebaseAuth.getInstance().signInWithCustomToken(token)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(activity,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            } else{
                                                sharedPreferences.edit().putString(CONSTANT.USER_EMAIL,email).apply();
                                                sharedPreferences.edit().putString(CONSTANT.USER_NAME,userName).apply();
                                                sharedPreferences.edit().putString(CONSTANT.USER_PICTURE,photo).apply();

                                                Intent intent = new Intent(activity, InboxActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.startActivity(intent);
                                                activity.finish();

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public Subscription sendLoginInfo(final EditText userEmailEt, final EditText userPasswordEt, final Socket socket
            , final BaseFragmentActivity activity){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        final String userEmail = strings.get(0);
                        String userPassword = strings.get(1);

                        if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        } else if (userPassword.isEmpty()){
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if(userPassword.length()<6){
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)){
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        } else{
                            FirebaseAuth.getInstance().signInWithEmailAndPassword(userEmail,userPassword)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()){
                                                Toast.makeText(activity,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                            } else{
                                                JSONObject sendData = new JSONObject();
                                                try {
                                                    sendData.put("email",userEmail);

                                                    Toast.makeText(activity, userEmail, Toast.LENGTH_LONG).show();

                                                    socket.emit("userInfo",sendData);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });


                            try {
                                FirebaseInstanceId.getInstance().deleteInstanceId();
                                FirebaseInstanceId.getInstance().getToken();            //refresh a new one
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            FirebaseAuth.getInstance().signOut();
                            return USER_NO_ERRORS;
                        }
                    }

                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)){
                            userEmailEt.setError("Email Address Can't Be Empty");
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)){
                            userEmailEt.setError("Please check your email format");
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)){
                            userPasswordEt.setError("Password Can't Be Blank");
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)){
                            userPasswordEt.setError("Password must be at least 6 characters long");
                        }

                    }
                });
    }


    public Subscription sendRegistrationInfo(final EditText userNameEt, final EditText userEmailEt,
                                             final EditText userPasswordEt, final Socket socket){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userNameEt.getText().toString());
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());

        Observable<List<String>> userDetailsObservable = Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {
                        String userName = strings.get(0);
                        String userEmail = strings.get(1);
                        String userPassword = strings.get(2);

                        if (userName.isEmpty()){
                            return USER_ERROR_EMPTY_USERNAME;
                        } else if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        } else if (userPassword.isEmpty()){
                            return USER_ERROR_EMPTY_PASSWORD;
                        } else if(userPassword.length()<6){
                            return USER_ERROR_PASSWORD_SHORT;
                        } else if (!isEmailValid(userEmail)){
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        } else{
                            JSONObject sendData = new JSONObject();
                            try {
                                sendData.put("email",userEmail);
                                sendData.put("userName",userName);
                                sendData.put("password",userPassword);
                                socket.emit("userData",sendData);
                                return SERVER_SUCCESS;
                            } catch (JSONException e) {
                                Log.i(LiveAccountServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;
                            }
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if (integer.equals(USER_ERROR_EMPTY_EMAIL)){
                            userEmailEt.setError("Email Address Can't Be Empty");
                        } else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)){
                            userEmailEt.setError("Please check your email format");
                        } else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)){
                            userPasswordEt.setError("Password Can't Be Blank");
                        } else if (integer.equals(USER_ERROR_PASSWORD_SHORT)){
                            userPasswordEt.setError("Password must be at least 6 characters long");
                        } else if(integer.equals(USER_ERROR_EMPTY_USERNAME)){
                            userNameEt.setError("Username can't be empty");
                        }

                    }
                });

    }

    public Subscription registerResponse(JSONObject data, final BaseFragmentActivity activity){
        Observable<JSONObject> jsonObjectObservable = Observable.just(data);
        return jsonObjectObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<JSONObject, String>() {

                    @Override
                    public String call(JSONObject jsonObject) {
                        String message;

                        try {
                            JSONObject json = jsonObject.getJSONObject("message");
                            message = (String) json.get("text");
                            return message;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return e.getMessage();
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String stringResponse) {
                        if(stringResponse.equals("Success")){
                            activity.finish();
                            Toast.makeText(activity, "Registeration Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(activity, stringResponse, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
