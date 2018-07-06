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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import sblourg.sblourgraphy.R;
import sblourg.sblourgraphy.model.bean.LensBank;
import sblourg.sblourgraphy.model.bean.ObjectifBean;

import static java.lang.System.out;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProfondeurDeChampActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private LensBank mLensBank;
    private ObjectifBean objectifSelected;
    //GUI
    private TextView mTextViewFocaleMin, mTextViewFocaleMax, mTextViewFocaleSelected, mTextViewMapSelected, mTextViewMapMin,  mTextViewMinPDC, mTextViewPDC, mTextViewMaxPDC;
    private Spinner mSpinnerObjectif, mSpinnerAperture;
    private SeekBar mSeekBarFocale,mSeekBarMap;
    private View mViewPDC, mViewBeforePDC, mViewAfterPDC;
    private Button mButtonSubmit;

    private double mapMin;
    private int mapMinInt;

    private final static String MM = "mm";
    private final static String M = "m";
    private final static String CM = "cm)";
    private final static String INFINY ="∞";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //GUI
        setContentView(R.layout.pdc_activity);

        try {
            readFromJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        initializeGui();
        addListenerOnButton();
    }

    private void initializeGui() {
        mTextViewFocaleSelected = (TextView) findViewById(R.id.textViewFocaleSelected);
        mTextViewMapSelected = (TextView) findViewById(R.id.textViewMapSelected);
        mTextViewMapMin = (TextView) findViewById(R.id.textViewMAPMin);
        mTextViewFocaleMin = (TextView) findViewById(R.id.textViewFocaleMin);
        mTextViewFocaleMax = (TextView) findViewById(R.id.textViewFocaleMax);
        mTextViewMinPDC = (TextView) findViewById(R.id.textViewMinPDC);
        mTextViewMaxPDC = (TextView) findViewById(R.id.textViewMaxPDC);
        mTextViewPDC = (TextView) findViewById(R.id.textViewPDC);

        mSpinnerObjectif = (Spinner) findViewById(R.id.spinnerObjectif);
        mSpinnerAperture = (Spinner) findViewById(R.id.spinnerAperture);
        mSeekBarFocale = (SeekBar) findViewById(R.id.seekBarFocale);
        mSeekBarMap = (SeekBar) findViewById(R.id.seekBarMAP);

        mViewPDC = (View) findViewById(R.id.viewPDC);
        mViewBeforePDC = (View) findViewById(R.id.viewBeforePDC);
        mViewAfterPDC = (View) findViewById(R.id.viewAfterPDC);

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
                initializeMap(objectifSelected);
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

        mTextViewMinPDC.setVisibility(View.INVISIBLE);
        mTextViewMaxPDC.setVisibility(View.INVISIBLE);
        mTextViewPDC.setVisibility(View.INVISIBLE);
        mViewPDC.setVisibility(View.INVISIBLE);
        mViewBeforePDC.setVisibility(View.INVISIBLE);
        mViewAfterPDC.setVisibility(View.INVISIBLE);
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

    private void initializeMap(final ObjectifBean objectifSelected) {
        mapMin = objectifSelected.getMinMAP();
        objectifSelected.setMapSelected(mapMin);

        //min
        mTextViewMapMin.setText(mapMin+M);

        // drawing seekBar
        mSeekBarMap = (SeekBar) findViewById(R.id.seekBarMAP);
        mSeekBarMap.setProgress(0);
        mapMinInt = (int) (mapMin * 100);
        mSeekBarMap.setMax(1000-mapMinInt);

        mSeekBarMap.refreshDrawableState();
        mTextViewMapSelected.setText(mapMin+M);
        //Get value from SeekBar
        mSeekBarMap.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {

                mTextViewMapSelected.setText(String.valueOf((double)(mapMinInt + progress)/100)+M);
                objectifSelected.setMapSelected((double)(mapMinInt + progress)/100);

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

                mTextViewMaxPDC.setVisibility(View.VISIBLE);
                mTextViewMinPDC.setVisibility(View.VISIBLE);
                mTextViewPDC.setVisibility(View.VISIBLE);
                mViewPDC.setVisibility(View.VISIBLE);
                mViewBeforePDC.setVisibility(View.VISIBLE);


                double minPDC = objectifSelected.calculateDistanceMinPDC();
                double maxPDC = objectifSelected.calculateDistanceMaxPDC();
                double PDC = 0;
                if (maxPDC !=0) {
                    PDC = maxPDC - minPDC;
                    PDC = Math.round ( PDC *1000.0)/1000.0;
                    mTextViewMinPDC.setText(minPDC+M);
                    mTextViewMaxPDC.setText(maxPDC+M);
                    mTextViewPDC.setText(PDC + M +  " ("+ Math.round(PDC*100*100.0)/100.0 + CM);
                    mViewAfterPDC.setVisibility(View.VISIBLE);
                }
                else {
                    mTextViewMinPDC.setText(minPDC+M);
                    mTextViewMaxPDC.setText(INFINY);
                    mTextViewPDC.setText(INFINY);
                    mViewAfterPDC.setVisibility(View.INVISIBLE);
                }
            }

        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        out.println("HyperfocaleActivity::onStart()");

    }

    @Override
    protected void onResume() {
        super.onResume();

        out.println("HyperfocaleActivity::onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();

        out.println("HyperfocaleActivity::onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();

        out.println("HyperfocaleActivity::onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        out.println("HyperfocaleActivity::onDestroy()");
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
            String minMap = wrapperObject.getString("minMap");

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

