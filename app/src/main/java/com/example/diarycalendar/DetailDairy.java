package com.example.diarycalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailDairy extends AppCompatActivity {

    private TextView date_tv;
    private TextView title_tv;
    private TextView content_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_diary);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        date_tv = findViewById(R.id.date_diary_tv);
        title_tv = findViewById(R.id.title_diary_tv);
        content_tv = findViewById(R.id.content_diary_tv);

        Intent intent = getIntent();
        date_tv.setText(intent.getStringExtra("date"));
        title_tv.setText(intent.getStringExtra("title"));
        content_tv.setText(intent.getStringExtra("content"));
    }
}
