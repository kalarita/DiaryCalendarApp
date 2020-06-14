package com.example.diarycanlendar.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.diarycanlendar.R;
import com.example.diarycanlendar.ui.WebDav.WebDavSardineUtil;
import com.example.diarycanlendar.ui.WebDav.WebDavUtil;
import com.example.diarycanlendar.login_activity;
import com.google.android.material.button.MaterialButton;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences config;
    private SharedPreferences.Editor editor;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //状态栏透明
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //获取工具栏
        Toolbar toolbar = findViewById(R.id.toolbar_page_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        // 设置工具栏中的界面标题
        for(int i = 0; i < toolbar.getChildCount(); i ++){
            View v = toolbar.getChildAt(i);
            if(v instanceof TextView){
                ((TextView) v).setText(getResources().getString(R.string.actionbar_title_settings));
                break;
            }
        }
        // 返回上一层
        for(int i = 0; i < toolbar.getChildCount(); i ++){
            View v = toolbar.getChildAt(i);
            if(v instanceof ImageView){
               v.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       finish();
                   }
               });
               break;
            }
        }
        // 根据配置文件配置初始化ui
        config = getSharedPreferences(String.valueOf(R.string.app_config),MODE_PRIVATE);
        editor = config.edit();
        if(!config.contains(getResources().getString(R.string.auto_picture))){
            editor.putString(getResources().getString(R.string.auto_picture), "true");
        }
        if(!config.contains(getResources().getString(R.string.auto_motto))){
            editor.putString(getResources().getString(R.string.auto_motto), "true");
        }
        if(!config.contains(getResources().getString(R.string.update_to_cloud))){
            editor.putString(getResources().getString(R.string.auto_update), "true");
        }

        // 初始化是否自动获取每日图片 默认 是, 并注册监听器
        boolean changeDataPicture = Boolean.valueOf(
                config.getString(getResources().getString(R.string.auto_picture), "true")
        );
        LinearLayout ly = (LinearLayout)findViewById(R.id.change_data_picture);
        initLayout(ly, R.string.change_data_picture, true, changeDataPicture);

        // 初始化是否自动获取每日一句 默认 是, 并注册监听器
        boolean changeDataMotto = Boolean.valueOf(
                config.getString(getResources().getString(R.string.auto_motto), "true")
        );
        LinearLayout ly2 = (LinearLayout)findViewById(R.id.change_data_motto);
        initLayout(ly2, R.string.change_data_motto, true, changeDataMotto);

        // 初始化是否使用网盘 默认否, 并注册监听器
        boolean update_to_cloud = Boolean.valueOf(
                config.getString(getResources().getString(R.string.auto_update), "false")
        );
        LinearLayout ly3 = (LinearLayout)findViewById(R.id.update_to_cloud);
        initLayout(ly3, R.string.use_cloud, true, update_to_cloud);

        // 更换账号
        LinearLayout ly4 = (LinearLayout)findViewById(R.id.change_user);
        initLayout(ly4, R.string.change_user, false, false);

        // 网盘配置
        LinearLayout ly9 = (LinearLayout)findViewById(R.id.cloud_config);
        initLayout(ly9, R.string.cloud_config, false, false);

        // 清除缓存
        LinearLayout ly5 = (LinearLayout)findViewById(R.id.clean_data_cache);
        initLayout(ly5, R.string.clean_data_cache, false, false);

        // 关于
        LinearLayout ly6 = (LinearLayout)findViewById(R.id.app_about);
        initLayout(ly6, R.string.app_about, false, false);

        // 检查更新
        LinearLayout ly7 = (LinearLayout)findViewById(R.id.app_check_update);
        initLayout(ly7, R.string.app_check_update, false, false);

        // 反馈
        LinearLayout ly8 = (LinearLayout)findViewById(R.id.app_feedback);
        initLayout(ly8, R.string.app_feedback, false, false);

        // 申明handle
        mHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0x1:
                        Bundle bundle = msg.getData();
                        String surl = bundle.getString("url");
                        String suid = bundle.getString("uid");
                        String spsd = bundle.getString("psd");
                        Toast.makeText(getBaseContext(),
                                getResources().getString(R.string.cloud_config_succeed)
                                        +"\nurl = " + surl + "; uid = " + suid + "; psw = " + spsd,
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 0x2:
                        Toast.makeText(getBaseContext(),
                                getResources().getString(R.string.cloud_config_failed),
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }


    /**
     * 对设置界面里面的控件进行初始化，初始化包括，状态， 标题，以及监听事件
     * @param ly :布局容器，每一个设置项都有一个布局文件
     * @param title ： 要设置的标题的id
     * @param isChecked ： 是否有switch开关
     * @param check_status ： 只有当有switch开关时有效，用于设置当前开关的状态
     */
    private void initLayout(ViewGroup ly,
                            int title,
                            boolean isChecked,
                            boolean check_status){
        initLayout(ly, getResources().getString(title), isChecked, check_status);
    }

    /**
     * 对设置界面里面的控件进行初始化，初始化包括，状态， 标题，以及监听事件
     * @param ly :布局容器，每一个设置项都有一个布局文件
     * @param title ： 要设置的标题
     * @param isChecked ： 是否有switch开关
     * @param check_status ： 只有当有switch开关时有效，用于设置当前开关的状态
     */
    private void initLayout(ViewGroup ly,
                            String title,
                            boolean isChecked,
                            boolean check_status){

        for(int i = 0; i < ly.getChildCount(); i ++){
            if(ly.getChildAt(i) instanceof TextView){
                TextView tv = (TextView)ly.getChildAt(i);
                tv.setText(title);
                break; // 由于switch本身集成了一个TextView ,因此找到第一个就break
            }
        }
        if(isChecked){
            for(int i = 0; i < ly.getChildCount(); i ++){
                if(ly.getChildAt(i) instanceof Switch){
                    Switch switch_compat =  (Switch)ly.getChildAt(i);
                    switch_compat.setChecked(check_status);
                }
            }
        }
        ly.setClickable(true);
        ly.setOnClickListener(this);  // 注册监听器
    }

    @Override
    public void finish(){
        if(editor != null){
            editor.commit();
        }
        super.finish();
    }

    @Override
    public void onClick(View item) {
        switch (item.getId()){
            case R.id.change_user:
                changeUser();
                break;
            case R.id.change_data_picture:
                changeDataPicture((ViewGroup)item);
                break;
            case R.id.change_data_motto:
                changeDataMotto((ViewGroup)item);
                break;
            case R.id.update_to_cloud:
                updateToCloud((ViewGroup)item);
                break;
            case R.id.cloud_config:
                cloudConfig();
                break;
            case R.id.clean_data_cache:
                cleanDataCache();
                break;
            case R.id.app_about:
                about();
                break;
            case R.id.app_check_update:
                checkUpdate();
                break;
            case R.id.app_feedback:
                feedback();
                break;
            default:
                break;
        }
    }

    /**
     *  更换用户,直接返回到登陆界面。注意为了避免回车又返回，我们对登陆界面使用SingleTask方式
     */
    private void changeUser(){
        SharedPreferences tpsf = getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor tpeditor = tpsf.edit();
        tpeditor.putString("history_account", tpsf.getString("account", "未登陆"));
        tpeditor.remove("account");
        tpeditor.commit();
        tpeditor.remove("account");
        Intent intent = new Intent();
        intent.setClass(SettingActivity.this, login_activity.class);
        startActivity(intent);
    }

    /**
     *
     */
    private final void changeDataPicture(ViewGroup item){
        for(int i = 0; i < item.getChildCount(); i ++){
            View view = item.getChildAt(i);
            if(view instanceof Switch){
                Switch switch_compat = (Switch)view;
                switch_compat.setChecked(!switch_compat.isChecked());
                editor.putString(getResources().getString(R.string.auto_picture),
                        String.valueOf(switch_compat.isChecked()));
                editor.commit();
                return;
            }
        }
    }

    private final void changeDataMotto(ViewGroup item){
        for(int i = 0; i < item.getChildCount(); i ++){
            View view = item.getChildAt(i);
            if(view instanceof Switch){
                Switch switch_compat = (Switch)view;
                switch_compat.setChecked(!switch_compat.isChecked());
                editor.putString(getResources().getString(R.string.auto_motto),
                        String.valueOf(switch_compat.isChecked()));
                editor.commit();
                return;
            }
        }
    }

    private final void updateToCloud(ViewGroup item){
        for(int i = 0; i < item.getChildCount(); i ++){
            View view = item.getChildAt(i);
            if(view instanceof Switch){
                Switch switch_compat = (Switch)view;
                switch_compat.setChecked(!switch_compat.isChecked());
                editor.putString(getResources().getString(R.string.auto_update),
                        String.valueOf(switch_compat.isChecked()));
                editor.commit();
                return;
            }
        }
    }


    private void cloudConfig(){
        final Context mContext = SettingActivity.this;
        if(Boolean.valueOf(
                config.getString(
                        getResources().getString(R.string.auto_update), "false"))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.setting_item_cloud, null);
            MaterialButton btn_sure = (MaterialButton) v.findViewById(R.id.cloud_setting_positive);
            MaterialButton btn_cancel = (MaterialButton) v.findViewById(R.id.cloud_setting_negative);
            final Spinner sp = (Spinner) v.findViewById(R.id.cloud_url);
            final EditText uid = (EditText) v.findViewById(R.id.cloud_uid);
            final EditText psd = (EditText) v.findViewById(R.id.cloud_psd);

            //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
            final Dialog dialog = builder.create();
            ((AlertDialog) dialog).setView(new EditText(mContext)); // 添加这句解决无法弹出软键盘问题
            dialog.show();
            dialog.getWindow().setContentView(v);//自定义布局应该在这里添加，要在dialog.show()的后面
//            dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置

            btn_sure.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String tpurl = "";
                    for (int i = 0; i < sp.getChildCount(); i++) {
                        if (sp.getChildAt(i).isSelected()) {
                            tpurl = getResources().getStringArray(R.array.cloud_url)[i];
                        }
                    }
                    final String surl = "https://dav.jianguoyun.com/dav/"; // 写死先
                    final String suid = uid.getText().toString();
                    final String spsd = psd.getText().toString();

                    // 不开启线程会陷入阻塞
//                    WebDavJackRabbitUtil utils = new WebDavJackRabbitUtil(surl, suid, spsd);
                    new Thread(){
                        @Override
                        public void run() {
                            WebDavUtil utils = new WebDavSardineUtil(surl, suid, spsd);
                            Message msg = new Message();
                            if (utils.initCloud()) {
                                synchronized (editor){ // 防止并发导致数据不一致
                                    editor.putString(getResources().getString(R.string.cloud_url), surl);
                                    editor.putString(getResources().getString(R.string.cloud_uid), suid);
                                    editor.putString(getResources().getString(R.string.cloud_psd), spsd);
                                    editor.commit();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", surl);
                                    bundle.putString("uid", suid);
                                    bundle.putString("psd", spsd);
                                    msg.setData(bundle);
                                }
                                msg.what=0x1;
                            } else {
                                msg.what=0x2;
                                Bundle bundle = new Bundle();
                                msg.setData(bundle);
                            }
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                    dialog.dismiss();
                }
            });

            btn_cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    dialog.dismiss();
                }
            });
        }else{
            Toast.makeText(mContext,
                    "请先启用网盘！",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanDataCache(){
        // ...
        Toast.makeText(this, R.string.clean_data_succeed,Toast.LENGTH_LONG).show();
    }

    private void about(){
        Toast.makeText(this, R.string.about_us,Toast.LENGTH_LONG).show();
    }

    private void checkUpdate(){
        // 检查更新逻辑 待补充

        //
        Toast.makeText(this, R.string.no_update,Toast.LENGTH_LONG).show();
    }

    private void feedback(){
        final EditText edit = new EditText(SettingActivity.this);
        AlertDialog.Builder dia = new AlertDialog.Builder(SettingActivity.this);
        dia.setTitle("反馈意见").setView(edit);
        dia.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(SettingActivity.this,
                                getResources().getString(R.string.thanks_for_feed_back),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        ).show();
        //
    }
}
