package com.example.madhu.buymystuff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1,b2;
        b1=(Button)findViewById(R.id.btRegMain);
        b2=(Button)findViewById(R.id.btLoginMain);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
    }



    @Override
    public  void onClick(View v) {
        switch (v.getId()) {

            case R.id.btLoginMain:
                startActivity(new Intent(this,Login.class));

                break;
            case R.id.btRegMain:
                startActivity(new Intent(this,Register.class));
                break;


        }


    }
}
