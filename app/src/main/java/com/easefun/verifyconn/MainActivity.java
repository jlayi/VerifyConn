package com.easefun.verifyconn;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.easefun.verifyconn.core.PolyvResponseVerify;

public class MainActivity extends Activity {
    PolyvResponseVerify responseVerify;
    ScrollView sv_test;
    TextView tv_test;
    EditText et_test;
    CheckBox cb_test;
    Button bt_start, bt_stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sv_test = findViewById(R.id.sv_test);
        tv_test = findViewById(R.id.tv_test);
        et_test = findViewById(R.id.et_test);
        bt_start = findViewById(R.id.bt_start);
        bt_stop = findViewById(R.id.bt_stop);
        cb_test = findViewById(R.id.cb_test);

        et_test.setText("http://hls.videocc.net/c538856dde/6/c538856ddefc2bc9242bd180c2f19fe6_1.m3u8");

        responseVerify = new PolyvResponseVerify();

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_test.setText("开始检测");
                responseVerify.verify(et_test.getText().toString(), cb_test.isChecked(), new PolyvResponseVerify.OnResponseVerifyListener() {
                    @Override
                    public void result(@NonNull PolyvResponseVerify.VerifyResult verifyResult) {
                        append("\n检测结果\n");
                        String describe = PolyvResponseVerify.VerifyResult.getVerifyDescribe(verifyResult.verifyCode);
                        if (verifyResult.verifyCode == PolyvResponseVerify.CODE_RESPONSESTATUSFAIL)
                            describe = describe + "-" + verifyResult.responseCode;
                        append(verifyResult.currentVerifyPath + "\n" + describe + "-length:" + verifyResult.contentLength);
                    }

                    @Override
                    public void progress(@NonNull PolyvResponseVerify.VerifyResult verifyResult, int position, int size) {
                        String describe = PolyvResponseVerify.VerifyResult.getVerifyDescribe(verifyResult.verifyCode);
                        if (verifyResult.verifyCode == PolyvResponseVerify.CODE_RESPONSESTATUSFAIL)
                            describe = describe + "-" + verifyResult.responseCode;
                        append("\n" + verifyResult.currentVerifyPath + "\n" + describe + "-length:" + verifyResult.contentLength + "***" + (position + 1) + "-" + size);
                    }
                });
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                responseVerify.shutdown();
                if (tv_test.getText().length() > 0)
                    tv_test.append("\n取消检测");
            }
        });
    }

    public void append(String msg) {
        tv_test.append(msg);
        sv_test.post(new Runnable() {
            public void run() {
                sv_test.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        responseVerify.shutdown();
    }
}
