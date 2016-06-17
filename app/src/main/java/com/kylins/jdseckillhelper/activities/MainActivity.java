package com.kylins.jdseckillhelper.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kylins.jdseckillhelper.R;
import com.kylins.jdseckillhelper.servicies.JDSeckillService;

public class MainActivity extends AppCompatActivity {

    private Switch aSwitch = null;

    private TextView name,price,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = (TextView) findViewById(R.id.tv_ss_goods_name);
        price =(TextView) findViewById(R.id.tv_ss_price);
        time = (TextView)findViewById(R.id.tv_ss_time);

        aSwitch = (Switch)findViewById(R.id.ss_switch);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(JDSeckillService.getInstall()==null){
                    aSwitch.setChecked(false);
                    Toast.makeText(MainActivity.this,"请打开辅助功能",Toast.LENGTH_LONG);
                }else{
                    if(isChecked){
                        if(!JDSeckillService.getInstall().start(name.getText().toString(),time.getText().toString(),price.getText().toString())){
                            aSwitch.setChecked(false);
                            Toast.makeText(MainActivity.this,"数据错误",Toast.LENGTH_LONG);
                        };
                    }else{
                        JDSeckillService.getInstall().stop();
                    }
                }

            }
        });
    }
}
