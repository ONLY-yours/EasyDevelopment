package com.esdev.easydev;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public abstract class FastBaseActivity extends AppCompatActivity {

    public static final String TAG = "IDcard service up";
    //比对阈值，建议为0.82
    public static final double THRESHOLD = 0.82d;
    public static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    // 在线激活所需的权限
    public static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    public int displayOrientation = 0;

    //用于快速授权
    protected boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    //用于快速启动活动
    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        //        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    /**
     * 存储键值对
     *
     * @param  key  value
     */
    public void saveTheKeyValues(String key,String value){
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        if (getTheKeyValues(key)!=null){
            editor.remove(""+key);
            editor.apply();
        }
//        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString(""+key, ""+value);
        editor.apply();
    }

    /**
     * 取出键值对中的值
     *
     * @param  key
     */
    public String getTheKeyValues(String key){
        SharedPreferences preferences = getSharedPreferences("data",MODE_PRIVATE);
        String str1 = preferences.getString(""+key,null);
        if (str1.length()<1){
            return "";
        }else return str1;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert mInputMethodManager != null;
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }
}
