package com.moj.warehouse.customUI.bezier;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.moj.warehouse.R;

public class BezierActivity extends AppCompatActivity {
    private BezierView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
        mView = findViewById(R.id.bezier);
    }

    public void anim(View v){
        mView.startAnim();
    }

    public void reset(View v){
        mView.reset();
    }
}
