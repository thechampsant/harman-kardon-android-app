package com.fieldforce.harmonkardonff;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import app.core.base.InnosolsActivity;

public class HelpLine extends InnosolsActivity {
    RelativeLayout iv_backView;
    private LinearLayout linearLayoutNumberContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_line);
        linearLayoutNumberContainer = findViewById(R.id.ll_number);
        iv_backView = findViewById(com.ariston.training_module.R.id.iv_backView);
        linearLayoutNumberContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:011-40655653"));
                startActivity(intent);*/
            }
        });
        iv_backView.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void RegisterTableInfoForLocalDB() {

    }
}
