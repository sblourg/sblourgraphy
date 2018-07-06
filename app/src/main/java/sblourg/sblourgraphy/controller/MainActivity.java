package sblourg.sblourgraphy.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import sblourg.sblourgraphy.R;

import static java.lang.System.out;

public class MainActivity extends AppCompatActivity {



    private Button mImageViewHyperfocale;
    private Button mImageViewProfondeurDeChamp;
    private Button mImageViewPoseLongue;

    public static final int HYPERFOCALE_ACTIVITY_REQUEST_CODE = 40;
    public static final int PROFONDEUR_DE_CHAMP_ACTIVITY_REQUEST_CODE = 45;
    public static final int POSE_LONGUE_ACTIVITY_REQUEST_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mImageViewHyperfocale = (Button) findViewById(R.id.activity_hyperfocale_btn);
        mImageViewProfondeurDeChamp = (Button) findViewById(R.id.activity_profondeur_de_champ_btn);
        mImageViewPoseLongue = (Button) findViewById(R.id.activity_pose_longue_btn);

        mImageViewHyperfocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // User clicked the button
                Intent hyperfocaleIntent = new Intent(MainActivity.this, HyperfocaleActivity.class);
                startActivityForResult(hyperfocaleIntent, HYPERFOCALE_ACTIVITY_REQUEST_CODE);
            }
        });


        mImageViewProfondeurDeChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // User clicked the button
                Intent profondeurDeChampIntent = new Intent(MainActivity.this, ProfondeurDeChampActivity.class);
                startActivityForResult(profondeurDeChampIntent, PROFONDEUR_DE_CHAMP_ACTIVITY_REQUEST_CODE);
            }
        });

        mImageViewPoseLongue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // User clicked the button
                Intent poseLongueIntent = new Intent(MainActivity.this, PoseLongueActivity.class);
                startActivityForResult(poseLongueIntent, POSE_LONGUE_ACTIVITY_REQUEST_CODE);
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
