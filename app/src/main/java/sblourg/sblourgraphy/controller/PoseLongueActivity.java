package sblourg.sblourgraphy.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.commons.math3.fraction.BigFraction;

import com.google.gson.Gson;

import org.apache.commons.math3.fraction.FractionConversionException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import sblourg.sblourgraphy.R;
import sblourg.sblourgraphy.model.bean.ShotBank;
import sblourg.sblourgraphy.model.bean.ShotBean;



public class PoseLongueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ShotBean mShot;
    private ShotBank mShotBank;
    //GUI
    private TextView mTextIsoSelectedValue, mTextViewApertureSelectedValue, mTextViewExposureSelectedValue;
    private TextView mTextIsoSelected, mTextViewApertureSelected, mTextViewExposureSelected;
    private Spinner mSpinnerIso, mSpinnerAperture, mSpinnerExposure;

    private RadioButton mBooleanIso, mBooleanAperture, mBooleanExposure, mBooleanPola, mBooleanVerre, mBooleanReset;
    private RadioGroup mRadioGroup;
    private final List<String> mEvaluate= new ArrayList<>();
    private ArrayAdapter<String> adapterExposure;
    private ArrayAdapter<Double>  adapterAperture;
    private ArrayAdapter<Long> adapterIso;
    private Boolean mInvisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GUI
        setContentView(R.layout.exposure_activity);

        //initialize context
        try {
            readFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initializeShotBean();

        initializeGui();
    }

    private void initializeShotBean() {
        mEvaluate.add("FIXED");
        mShot = mShotBank.getShotBank().get(0);
        mShot.setExposureSelected(mShot.getExposureList().get(0));
        mShot.setIsoSelected(mShot.getIsoList().get(0));
        mShot.setApertureSelected(mShot.getApertureList().get(0));
    }

    private void initializeGui() {

        mTextIsoSelectedValue =findViewById(R.id.textViewIsoSelectedValue);
        mTextViewApertureSelectedValue =  findViewById(R.id.textViewApertureSelectedValue);
        mTextViewExposureSelectedValue =  findViewById(R.id.textViewExposureSelectedValue);
        mTextIsoSelected = findViewById(R.id.textViewIsoSelected);
        mTextViewApertureSelected =  findViewById(R.id.textViewApertureSelected);
        mTextViewExposureSelected =  findViewById(R.id.textViewExposureSelected);

        mBooleanAperture =  findViewById(R.id.radio_Aperture);
        mBooleanExposure =  findViewById(R.id.radio_Exposure);
        mBooleanIso =  findViewById(R.id.radio_iso);
        mBooleanReset =  findViewById(R.id.radio_Reset);
        mRadioGroup =  findViewById(R.id.radioGroup);
        mBooleanReset.setChecked(true);

        mSpinnerIso =  findViewById(R.id.spinnerIso);
        mSpinnerAperture =  findViewById(R.id.spinnerAperture);
        mSpinnerExposure =  findViewById(R.id.spinnerExposure);


        // Spinner iso
        adapterIso = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mShot.isoList);
        adapterIso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerIso.setAdapter(adapterIso);
        mSpinnerIso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!mBooleanIso.isChecked()){
                    mShot.setIsoSelected(mShot.getIsoList().get(i));
                    mTextIsoSelectedValue.setText(String.valueOf(mShot.getIsoSelected()));
                    if (mBooleanAperture.isChecked()) {

                        if (mShot.calculateEXPO() <1 ) {
                            BigFraction expoCalculatedBF = null;
                            try {
                                expoCalculatedBF = new BigFraction(mShot.calculateEXPO(), 10000);
                            } catch (FractionConversionException e) {
                                e.printStackTrace();
                            }
                            mTextViewExposureSelectedValue.setText( String.valueOf(expoCalculatedBF));
                        } else {
                            mTextViewExposureSelectedValue.setText(String.valueOf(mShot.calculateEXPO()));
                        }
                    }
                    if (mBooleanExposure.isChecked()) {
                        mTextViewApertureSelectedValue.setText(String.valueOf(mShot.calculateAPERTURE()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Spinner aperture
        adapterAperture = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mShot.apertureList);
        adapterAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAperture.setAdapter(adapterAperture);
        mSpinnerAperture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!mBooleanAperture.isChecked()) {
                    mShot.setApertureSelected(mShot.getApertureList().get(i));

                    mTextViewApertureSelectedValue.setText(String.valueOf(mShot.getApertureSelected()));
                    if (mBooleanIso.isChecked()) {

                        if (mShot.calculateEXPO() <1 ) {
                            BigFraction expoCalculatedBF = null;
                            try {
                                expoCalculatedBF = new BigFraction(mShot.calculateEXPO(), 10000);
                            } catch (FractionConversionException e) {
                                e.printStackTrace();
                            }
                            mTextViewExposureSelectedValue.setText( String.valueOf(expoCalculatedBF));
                        } else {
                            mTextViewExposureSelectedValue.setText(String.valueOf(mShot.calculateEXPO()));
                        }
                    }
                    if (mBooleanExposure.isChecked()) {
                        mTextIsoSelectedValue.setText(String.valueOf(mShot.calculateISO()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Spinner exposure
        adapterExposure = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, mShot.exposureList);
        adapterExposure.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerExposure.setAdapter(adapterExposure);
        mSpinnerExposure.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!mBooleanExposure.isChecked()) {
                    mShot.setExposureSelected(mShot.getExposureList().get(i));
                    mTextViewExposureSelectedValue.setText(String.valueOf(mShot.getExposureSelected()));
                    if (mBooleanIso.isChecked()) {
                        mTextViewApertureSelectedValue.setText(String.valueOf(mShot.calculateAPERTURE()));
                    }
                    if (mBooleanAperture.isChecked()) {
                        mTextIsoSelectedValue.setText(String.valueOf(mShot.calculateISO()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        setFieldsInvisible();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        ArrayAdapter<String> adapterEvaluate = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mEvaluate);

        // Check which radio button was clicked
        switch(view.getId()) {

            case R.id.radio_Reset:
                if (checked){
                    mSpinnerExposure.setAdapter(adapterExposure);
                    mSpinnerIso.setAdapter(adapterIso);
                    mSpinnerAperture.setAdapter(adapterAperture);
                    mRadioGroup.clearCheck();
                    mInvisible = true;
                    setFieldsInvisible();
                    break;
                }

            case R.id.radio_iso:
                if (checked) {
                    mSpinnerIso.setAdapter(adapterEvaluate);
                    mShot.setLv(mShot.calculateLv());
                    mTextIsoSelectedValue.setText(String.valueOf(mShot.getIsoSelected()));
                    mSpinnerExposure.setAdapter(adapterExposure);
                    mSpinnerAperture.setAdapter(adapterAperture);
                    mBooleanReset.setChecked(false);
                    mInvisible=false;
                    setFieldsInvisible();
                    break;
                }

            case R.id.radio_Aperture:
                if (checked){
                    mShot.setLv(mShot.calculateLv());
                    mTextViewApertureSelectedValue.setText(String.valueOf(mShot.getApertureSelected()));
                    mSpinnerAperture.setAdapter(adapterEvaluate);
                    mSpinnerExposure.setAdapter(adapterExposure);
                    mSpinnerIso.setAdapter(adapterIso);
                    mBooleanReset.setChecked(false);
                    mInvisible=false;
                    setFieldsInvisible();
                    break;
                }

            case R.id.radio_Exposure:
                if (checked){
                    mShot.setLv(mShot.calculateLv());
                    mSpinnerExposure.setAdapter(adapterEvaluate);
                    mTextViewExposureSelectedValue.setText(String.valueOf(mShot.getExposureSelected()));
                    mSpinnerIso.setAdapter(adapterIso);
                    mSpinnerAperture.setAdapter(adapterAperture);
                    mBooleanReset.setChecked(false);
                    mInvisible=false;
                    setFieldsInvisible();
                    break;
                }
        }
    }

    private void setFieldsInvisible () {
        if (mInvisible){
            mTextIsoSelectedValue.setVisibility(View.INVISIBLE);
            mTextViewApertureSelectedValue.setVisibility(View.INVISIBLE);
            mTextViewExposureSelectedValue.setVisibility(View.INVISIBLE);
            mTextIsoSelected.setVisibility(View.INVISIBLE);
            mTextViewApertureSelected.setVisibility(View.INVISIBLE);
            mTextViewExposureSelected.setVisibility(View.INVISIBLE);
        } else {
            mTextIsoSelectedValue.setVisibility(View.VISIBLE);
            mTextViewApertureSelectedValue.setVisibility(View.VISIBLE);
            mTextViewExposureSelectedValue.setVisibility(View.VISIBLE);
            mTextIsoSelected.setVisibility(View.VISIBLE);
            mTextViewApertureSelected.setVisibility(View.VISIBLE);
            mTextViewExposureSelected.setVisibility(View.VISIBLE);
        }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void readFromJson() throws JSONException {
        String json = initializeJsonReader();
        populateShotBank(json);
    }

    private void populateShotBank(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray shotBank = jsonObject.getJSONArray("shot");

        for (int j=0; j < shotBank.length(); j++) {

            JSONObject wrapperObject = shotBank.getJSONObject(j);

            //IsoArray
            JSONArray isoShotArray = new JSONArray(wrapperObject.getString("iso"));

            // ExposureArray
            JSONArray exposureShotArray = new JSONArray(wrapperObject.getString("exposure"));

            //ApertureArray
            JSONArray apertureShotArray = new JSONArray(wrapperObject.getString("apertureShot"));

        }
        mShotBank = new Gson().fromJson(json,ShotBank.class);
    }

    @Nullable
    private String initializeJsonReader() {
        String json = null;
        try {
            InputStream inputStream = this.getResources().openRawResource(R.raw.lens);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

}


