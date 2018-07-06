package sblourg.sblourgraphy.controller;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import sblourg.sblourgraphy.R;

import sblourg.sblourgraphy.model.bean.LensBank;
import sblourg.sblourgraphy.model.bean.ObjectifBean;



import static java.lang.System.out;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HyperfocaleActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LensBank mLensBank;
    private ObjectifBean objectifSelected;
    //GUI
    private TextView mTextViewFocaleMin, mTextViewFocaleMax, mTextViewFocaleSelected, mTextViewHyperfocaleResult, mTextViewHyperfocale;
    private Spinner mSpinnerObjectif, mSpinnerAperture;
    private SeekBar mSeekBarFocale;
    private Button mButtonSubmit;

    private final static String MM = "mm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GUI
        setContentView(R.layout.hyperfocale_activity);

        try {
            readFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeGui();
        addListenerOnButton();
    }

    private void initializeGui() {
        mTextViewHyperfocale = (TextView) findViewById(R.id.textViewHyperfocale);
        mTextViewHyperfocaleResult = (TextView) findViewById(R.id.textViewHyperfocaleResult);
        mTextViewFocaleSelected = (TextView) findViewById(R.id.textViewFocaleSelected);
        mTextViewFocaleMin = (TextView) findViewById(R.id.textViewFocaleMin);
        mTextViewFocaleMax = (TextView) findViewById(R.id.textViewFocaleMax);
        mSpinnerObjectif = (Spinner) findViewById(R.id.spinnerObjectif);
        mSpinnerAperture = (Spinner) findViewById(R.id.spinnerAperture);
        mSeekBarFocale = (SeekBar) findViewById(R.id.seekBarFocale);

        // Spinner objectif
        ArrayAdapter<String> adapterObjectif = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mLensBank.getLensListName());
        adapterObjectif.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerObjectif.setAdapter(adapterObjectif);
        mSpinnerObjectif.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //objectifSelected = String.valueOf (adapterView.getItemAtPosition(i));

                objectifSelected = mLensBank.getLensBank().get(i);

                initializeFocale(objectifSelected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initializeFocale( ObjectifBean objectifSelected) {

        ArrayAdapter<Double> adapterAperture = new ArrayAdapter<Double>(this,android.R.layout.simple_spinner_item, objectifSelected.aperture);
        createApertureSpinner(adapterAperture);
        mTextViewFocaleSelected.setText(String.valueOf(objectifSelected.focale)+MM);
        setFieldsInvisible(objectifSelected.focaleFixe);
        if (objectifSelected.focaleFixe==false){
            focaleFromZoom(objectifSelected.minFocaleZoom,objectifSelected.maxFocaleZoom);
        }

    }

    private void createApertureSpinner(ArrayAdapter<Double> adapterAperture) {
        adapterAperture.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerAperture.setAdapter(adapterAperture);
        mSpinnerAperture.setOnItemSelectedListener(this);

    }

    private void setFieldsInvisible (boolean invisible) {
        if (invisible){
            mTextViewFocaleMin.setVisibility(View.INVISIBLE);
            mTextViewFocaleMax.setVisibility(View.INVISIBLE);
            mSeekBarFocale.setVisibility(View.INVISIBLE);
        } else {
            mTextViewFocaleMin.setVisibility(View.VISIBLE);
            mTextViewFocaleMax.setVisibility(View.VISIBLE);
            mSeekBarFocale.setVisibility(View.VISIBLE);
        }
        mTextViewHyperfocaleResult.setVisibility(View.INVISIBLE);
        mTextViewHyperfocale.setVisibility(View.INVISIBLE);
    }

    private void focaleFromZoom(final int focaleMin, int focaleMax) {
        //min
        mTextViewFocaleMin.setText(focaleMin+MM);
        //max
        mTextViewFocaleMax.setText(focaleMax+MM);
        // drawing seekBar
        mSeekBarFocale = (SeekBar) findViewById(R.id.seekBarFocale);
        mSeekBarFocale.setProgress(0);
        mSeekBarFocale.setMax(focaleMax - focaleMin);
        mSeekBarFocale.refreshDrawableState();
        mTextViewFocaleSelected.setText(focaleMin+MM);
        //Get value from SeekBar
        mSeekBarFocale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mTextViewFocaleSelected.setText(String.valueOf(focaleMin + progress)+MM);
                objectifSelected.setFocale(focaleMin + progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    // get the selected dropdown list value
    public void addListenerOnButton() {
        mButtonSubmit = (Button) findViewById(R.id.buttonSubmit);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                objectifSelected.setApertureSelected((Double) mSpinnerAperture.getSelectedItem());

                /*Toast.makeText(HyperfocaleActivity.this,
                        "OnClickListener : " +
                                "\nObjectif : "+ String.valueOf(mSpinnerObjectif.getSelectedItem()) +
                                "\nAperture : "+ String.valueOf(mSpinnerAperture.getSelectedItem()),
                        Toast.LENGTH_SHORT).show();*/



                mTextViewHyperfocaleResult.setVisibility(View.VISIBLE);
                mTextViewHyperfocale.setVisibility(View.VISIBLE);
                double hyperfocale = objectifSelected.calculateHyperfocale();
                hyperfocale = Math.round(hyperfocale*100.0)/100.0;
                mTextViewHyperfocaleResult.setText(hyperfocale+"m");
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void readFromJson() throws JSONException {
        String json = initializeJsonReader();
        populateLensBank(json);
    }

    private LensBank populateLensBank(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        JSONArray lensBank = jsonObject.getJSONArray("lens");

        for (int j=0; j < lensBank.length(); j++) {
            JSONObject wrapperObject = lensBank.getJSONObject(j);
            String lensName = wrapperObject.getString("lensName");
            String focaleFixe = wrapperObject.getString("focaleFixe");
            String focaleSelected = wrapperObject.getString("focaleSelected");
            String focaleMin = wrapperObject.getString("focaleMin");
            String focaleMax = wrapperObject.getString("focaleMax");

            JSONArray apertureArray = new JSONArray(wrapperObject.getString("aperture"));
            double[] apertureObjectifBean = new double[apertureArray.length()];

// Extract numbers from JSON array.
            for (int i = 0; i < apertureArray.length(); ++i) {
                apertureObjectifBean[i] = apertureArray.optInt(i);
            }

        }
        mLensBank = new Gson().fromJson(json,LensBank.class);
        return mLensBank;
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
