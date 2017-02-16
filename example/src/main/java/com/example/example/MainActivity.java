package com.example.example;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.library.BarChartView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BarChartView chartView;
    private SeekBar seekBar;
    private List<Float> floats;
    private RadioGroup rg,rg1,rg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chartView = ((BarChartView) findViewById(R.id.barchart));
        seekBar = ((SeekBar) findViewById(R.id.value_seek));
        rg = ((RadioGroup) findViewById(R.id.rg));
        rg1 = ((RadioGroup) findViewById(R.id.rg1));
        rg2 = ((RadioGroup) findViewById(R.id.rg2));


        float[] progress = new float[]{110,120,130,140,150,160,170,180,190};
        chartView.setBarChartList(progress);
        floats = new ArrayList<>();
        seekBar.setProgress(15);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress > 15) {
                    floats.add((float) (120+progress*5));
                }else{
                    if (floats.size() > 0) {
                        floats.remove(0);
                    }
                }
                list2Float(floats);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        chartView.setOnViewClick(new BarChartView.onViewClick() {
            @Override
            public void onClick(int index, int value) {
                Toast.makeText(MainActivity.this, "onClick: "+index+"  "+value, Toast.LENGTH_SHORT).show();
            }
        });


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.hide_gird) {
                    chartView.setHideGirdLine(false);
                }else if(checkedId == R.id.show_gird){
                    chartView.setHideGirdLine(true);
                }
            }
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.line) {
                    chartView.setCharType(1);
                }else if(checkedId == R.id.bar){
                    chartView.setCharType(0);
                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.enable) {
                    chartView.setBarPressEnable(true);
                }else if(checkedId == R.id.unable){
                    chartView.setBarPressEnable(false);
                }
            }
        });

    }

    private void list2Float(List<Float> floats){
        float[] progress = new float[floats.size()];
        for (int i = 0; i < floats.size(); i++) {
            progress[i] = floats.get(i);
        }
        chartView.setBarChartList(progress);
    }
}
