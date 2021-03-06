package com.example.moodswings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


public class HomeFragment extends Fragment {
    BarChart barchart;
    private ArrayList<Survey> mySurveys = new ArrayList<>();
    private SurveyAdapter surveyAdapter;
    Survey survey = new Survey();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DBAdapter db = new DBAdapter(getActivity());

        final Button today_button = view.findViewById(R.id.TodayMoodBtnID_2);
        final Button tomorrow_button = view.findViewById(R.id.TodayMoodBtnID_3);

        barchart = view.findViewById(R.id.moodgraph);
        final ArrayList<BarEntry> barEntries = new ArrayList<>();

        today_button.setBackgroundResource(R.drawable.noinput);
        tomorrow_button.setBackgroundResource(R.drawable.noinput);

        Task<QuerySnapshot> retrieveSurvey = db.getSurvey(survey.todaysDate());
        retrieveSurvey.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    survey = null;
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            survey = document.toObject(Survey.class);
                            // mySurveys.add(survey);
                            int mood = survey.getMood();

                            if (mood != 0) {
                                if (mood == 1) {
                                    today_button.setBackgroundResource(R.drawable.circle_angry_toggle);
                                }
                                if (mood == 2) {
                                    today_button.setBackgroundResource(R.drawable.circle_misery_toggle);
                                }
                                if (mood == 3) {
                                    today_button.setBackgroundResource(R.drawable.circle_sad_toggle);
                                }
                                if (mood == 4) {
                                    today_button.setBackgroundResource(R.drawable.circle_okay_toggle);
                                }
                                if (mood == 5) {
                                    today_button.setBackgroundResource(R.drawable.circle_good_toggle);
                                }
                                if (mood == 6) {
                                    today_button.setBackgroundResource(R.drawable.circle_happy_toggle);
                                }
                            }
                        }
                    }
                }
            }
        });

        Task<QuerySnapshot> retrieveTomorrowSurvey = db.getSurvey(survey.tomorrowsDate());
        retrieveTomorrowSurvey.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    survey = null;
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        if (document.exists()) {
                            survey = document.toObject(Survey.class);
                            Task<QuerySnapshot> retrieveTomorrowSurveyPrediction = db.getLatestSurveyForActivity(survey.getActivities());
                            retrieveTomorrowSurveyPrediction.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (Objects.requireNonNull(task.getResult()).size() > 0) {
                                            survey = null;
                                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                                if (document.exists()) {
                                                    survey = document.toObject(Survey.class);
                                                    // mySurveys.add(survey);
                                                    int mood = survey.getMood();

                                                    if (mood != 0) {
                                                        if (mood == 1) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_angry_toggle);
                                                        }
                                                        if (mood == 2) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_misery_toggle);
                                                        }
                                                        if (mood == 3) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_sad_toggle);
                                                        }
                                                        if (mood == 4) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_okay_toggle);
                                                        }
                                                        if (mood == 5) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_good_toggle);
                                                        }
                                                        if (mood == 6) {
                                                            tomorrow_button.setBackgroundResource(R.drawable.circle_happy_toggle);
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            tomorrow_button.setBackgroundResource(R.drawable.circle_okay_toggle);
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });


        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,-6);
        final ArrayList<String> days = new ArrayList<>();

            retrieveSurvey = db.getWeekSurveys();

            retrieveSurvey.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        Survey temp;
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            if (document.exists()) {
                                String diaryDate = document.getString("diaryDate");
                                Integer moodEnum = Objects.requireNonNull(document.getDouble("mood")).intValue();
                                String diaryEntry = document.getString("diaryEntry");
                                Integer activities = Objects.requireNonNull(document.getDouble("activities")).intValue();
                                temp = new Survey(moodEnum, diaryEntry, activities, diaryDate);
                                mySurveys.add(temp);
                            }
                        }
                        String date = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
                                Calendar.LONG,
                                Locale.getDefault());
                        String date5 = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
                                Calendar.LONG,
                                Locale.getDefault()) +
                                ", " + (calendar.get(Calendar.MONTH) + 1) + "/"
                                + calendar.get(Calendar.DATE) + "/"
                                + calendar.get(Calendar.YEAR);
                        calendar.add(Calendar.DAY_OF_WEEK, 1);
                        int arrayListIndex = mySurveys.size()-1;
                        for(int finalI = 0; finalI < 7; finalI++){
                            days.add(date);
                            if(mySurveys != null
                                    && arrayListIndex >= 0
                                    && date5.equals(mySurveys.get(arrayListIndex).getDiaryDate())){
                                switch (mySurveys.get(arrayListIndex).getMood()){
                                    case 1:
                                        barEntries.add(new BarEntry(1f, finalI));
                                        break;
                                    case 2:
                                        barEntries.add(new BarEntry(2f, finalI));
                                        break;
                                    case 3:
                                        barEntries.add(new BarEntry(3f, finalI));
                                        break;
                                    case 4:
                                        barEntries.add(new BarEntry(4f, finalI));
                                        break;
                                    case 5:
                                        barEntries.add(new BarEntry(5f, finalI));
                                        break;
                                    case 6:
                                        barEntries.add(new BarEntry(6f, finalI));
                                        break;
                                    default:
                                        barEntries.add(new BarEntry(0f, finalI));
                                        break;
                                }
                                BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

                                /*hide legend*/
                                Legend legend = barchart.getLegend();
                                legend.setEnabled(false);
                                barchart.invalidate();
                                XAxis xAxis = barchart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setDrawGridLines(false);
                                barchart.setDescription(null);
                                BarData theData = new BarData(days, barDataSet);
                                barchart.setData(theData);
                                barchart.animateY(2000);
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                barchart.setTouchEnabled(true);
                                barchart.setDragEnabled(false);
                                barchart.setScaleEnabled(false);

                                if(surveyAdapter == null) surveyAdapter =
                                        new SurveyAdapter(getActivity(), mySurveys);
                                ListView surveysList = view.findViewById(R.id.list_home);
                                surveysList.setAdapter(surveyAdapter);

                                arrayListIndex--;
                            }else{
                                barEntries.add(new BarEntry(0f, finalI));
                                BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
// hide legend
                                Legend legend = barchart.getLegend();
                                legend.setEnabled(false);
                                barchart.invalidate();
                                XAxis xAxis = barchart.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setDrawGridLines(false);
                                barchart.setDescription(null);
                                BarData theData = new BarData(days, barDataSet);
                                barchart.setData(theData);
                                barchart.animateY(2000);
                                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                                barchart.setTouchEnabled(true);
                                barchart.setDragEnabled(false);
                                barchart.setScaleEnabled(false);
                            }
                            date = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
                                    Calendar.LONG,
                                    Locale.getDefault());
                            date5 = calendar.getDisplayName(Calendar.DAY_OF_WEEK,
                                    Calendar.LONG,
                                    Locale.getDefault()) +
                                    ", " + (calendar.get(Calendar.MONTH) + 1) + "/"
                                    + calendar.get(Calendar.DATE) + "/"
                                    + calendar.get(Calendar.YEAR);
                            calendar.add(Calendar.DAY_OF_WEEK, 1);
                        }
                    }
                }
            });
        }
}