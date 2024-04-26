package com.example.project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private Button weatherBtn;
    private EditText editCityName;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editCityName = findViewById(R.id.editCityName);
        weatherBtn = findViewById(R.id.weatherBtn);
        Button quizBtn = findViewById(R.id.QuizBtn);
        Button graph = findViewById(R.id.graph);
        barChart = findViewById(R.id.barChart);

        weatherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
                intent.putExtra("cityName", editCityName.getText().toString());
                startActivity(intent);
            }
        });

        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });

        graph.setOnClickListener(v -> {
            Context context = MainActivity.this;
            MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();

            // Here we fetch the municipality data in a background service, so that we do not disturb the UI
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    MunicipalityDataRetriever.getMunicipalityCodesMap();
                    ArrayList<MunicipalityData> municipalityDataArrayList = municipalityDataRetriever.getData(context, editCityName.getText().toString());

                    if (municipalityDataArrayList == null) {
                        return;
                    }

                    Map<Integer, Integer> Population = new HashMap<>();
                    for (MunicipalityData data : municipalityDataArrayList) {
                        if (data.getYear() > 2014) {
                            Population.put(data.getYear(), data.getPopulation());
                        }
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<BarEntry> PopulationData = new ArrayList<>();
                            for (Map.Entry<Integer, Integer> entry : Population.entrySet()) {
                                int year = entry.getKey();
                                int population = entry.getValue();
                                PopulationData.add(new BarEntry(year, population));
                            }

                            BarDataSet barDataSet = new BarDataSet(PopulationData, "Population");
                            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(16f);

                            BarData barData = new BarData(barDataSet);

                            barChart.setFitBars(true);
                            barChart.setData(barData);
                            barChart.invalidate(); // Ensure chart is updated
                        }
                    });
                }
            });
        });
    }
}
