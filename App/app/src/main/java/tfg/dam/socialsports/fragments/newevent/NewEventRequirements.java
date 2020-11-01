package tfg.dam.socialsports.fragments.newevent;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TableRow;
import android.widget.TextView;

import tfg.dam.socialsports.R;

public class NewEventRequirements extends Fragment {

    private TextView textDescrip;
    private CheckBox checkBoxMinAge;
    private CheckBox checkBoxMaxAge;
    private CheckBox checkBoxGender;
    private CheckBox checkBoxReputation;
    private TableRow rowMinAge;
    private TableRow rowMaxAge;
    private SeekBar barMinAge;
    private SeekBar barMaxAge;
    private RadioGroup groupGender;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private TextView textMinAge;
    private TextView textMaxAge;
    private TextView textReputation;
    private RatingBar ratingReputation;
    public static final int MinAge = 1;
    public static final int MaxAge = 100;

    public NewEventRequirements() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_event_requirements, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        textDescrip = getActivity().findViewById(R.id.textRequirementsDescription);
        checkBoxMinAge = getActivity().findViewById(R.id.checkRequirementsMinAge);
        checkBoxMaxAge = getActivity().findViewById(R.id.checkRequirementsMaxAge);
        checkBoxGender = getActivity().findViewById(R.id.checkRequirementsGender);
        checkBoxReputation = getActivity().findViewById(R.id.checkRequirementsReputation);
        rowMinAge = getActivity().findViewById(R.id.rowRequirementsMinAge);
        rowMaxAge = getActivity().findViewById(R.id.rowRequirementsMaxAge);
        barMinAge = getActivity().findViewById(R.id.seekBarRequirementsMinAge);
        barMaxAge = getActivity().findViewById(R.id.seekBarRequirementsMaxAge);
        groupGender = getActivity().findViewById(R.id.groupRequirementsGender);
        radioMale = getActivity().findViewById(R.id.radioRequirementsMale);
        radioFemale = getActivity().findViewById(R.id.radioRequirementsFemale);
        textMinAge = getActivity().findViewById(R.id.textRequirementsMinAge);
        textMaxAge = getActivity().findViewById(R.id.textRequirementsMaxAge);
        textReputation = getActivity().findViewById(R.id.textRequirementsReputation);
        ratingReputation = getActivity().findViewById(R.id.ratingRequirementsReputation);
        barMaxAge.setProgress(MaxAge);
        barMinAge.setProgress(MinAge);
        rowMinAge.setVisibility(View.GONE);
        rowMaxAge.setVisibility(View.GONE);
        groupGender.setVisibility(View.GONE);
        ratingReputation.setEnabled(false);

        checkBoxMinAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rowMinAge.setVisibility(View.VISIBLE);
                    textMinAge.setText(Integer.toString(barMinAge.getProgress()));
                }
                else {
                    rowMinAge.setVisibility(View.GONE);
                    barMinAge.setProgress(MinAge);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        barMaxAge.setMin(MinAge);
                    }
                }
            }
        });
        checkBoxMaxAge.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rowMaxAge.setVisibility(View.VISIBLE);
                    textMaxAge.setText(Integer.toString(barMaxAge.getProgress()));
                }
                else {
                    rowMaxAge.setVisibility(View.GONE);
                    barMaxAge.setProgress(MaxAge);
                    barMinAge.setMax(MaxAge);
                }
            }
        });
        barMinAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMinAge.setText(Integer.toString(progress));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    barMaxAge.setMin(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        barMaxAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textMaxAge.setText(Integer.toString(progress));
                barMinAge.setMax(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        checkBoxGender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    groupGender.setVisibility(View.VISIBLE);
                else
                    groupGender.setVisibility(View.GONE);
            }
        });
        checkBoxReputation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ratingReputation.setEnabled(true);
                    textReputation.setText(Float.toString(ratingReputation.getRating()));
                }
                else {
                    ratingReputation.setEnabled(false);
                    textReputation.setText("");
                }
            }
        });
        ratingReputation.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textReputation.setText(Float.toString(ratingBar.getRating()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        textDescrip.setFocusable(true);
        textDescrip.setFocusableInTouchMode(true);
        textDescrip.requestFocus();
        textDescrip.setFocusable(false);
        textDescrip.setFocusableInTouchMode(false);
    }

    public int getEdadMinima() {
        if (checkBoxMinAge.isChecked())
            return barMinAge.getProgress();
        return -1;
    }

    public int getEdadMaxima() {
        if (checkBoxMaxAge.isChecked())
            return barMaxAge.getProgress();
        return -1;
    }

    public String getGenero() {
        if (checkBoxGender.isChecked()) {
            if (radioMale.isChecked())
                return ("Male").toUpperCase();
            if (radioFemale.isChecked())
                return ("Female").toUpperCase();
        }
        return "";
    }

    public float getReputacion() {
        if (checkBoxReputation.isChecked())
            return ratingReputation.getRating();
        return -1;
    }
}
