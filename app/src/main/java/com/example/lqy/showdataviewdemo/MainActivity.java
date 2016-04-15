package com.example.lqy.showdataviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Float> mileList = new ArrayList<>();
    ArrayList<Float> heartList = new ArrayList<>();
    ArrayList<Float> speedList = new ArrayList<>();
    ArrayList<Integer> timeList = new ArrayList<>();
    ArrayList<String> dateList = new ArrayList<>();

    ShowDataView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        view = (ShowDataView) findViewById(R.id.sdv);
        // test data
        for (int i = 0; i < 30; ++i) {
            mileList.add((float) (Math.random() * 200));
            heartList.add((float) (Math.random() * 150));
            speedList.add((float) (Math.random() * 20));
            timeList.add(i);
            dateList.add(i + "");
        }
        view.setDataLists(mileList, heartList, speedList, timeList, dateList);

//        ShowDataSurface viewf = (ShowDataSurface) findViewById(R.id.sdf);
//        viewf.setDataLists(mileList, heartList, speedList, timeList, dateList);
        findViewById(R.id.tv_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mileList.clear();
                heartList.clear();
                speedList.clear();
                timeList.clear();
                dateList.clear();
                int size = (int) (Math.random()*200);
                for (int i = 0; i < size; ++i) {
                    mileList.add((float) (Math.random() * 200));
                    heartList.add((float) (Math.random() * 150));
                    speedList.add((float) (Math.random() * 20));
                    timeList.add(i);
                    dateList.add(i + "");
                }
                ((TextView)findViewById(R.id.tv_show_num)).setText("now data's number is " + size);
                view.setDataLists(mileList, heartList, speedList, timeList, dateList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
