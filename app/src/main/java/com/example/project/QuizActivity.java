package com.example.project;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_quiz);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<MCQ> mcqList = new ArrayList<>();
        mcqList.add(new MCQ("What is the capital of France?", new String[]{"Paris", "Berlin", "London", "Rome"}, 0));
        mcqList.add(new MCQ("Who wrote 'To Kill a Mockingbird'?", new String[]{"Stephen King", "Harper Lee", "J.K. Rowling", "Mark Twain"}, 1));
        mcqList.add(new MCQ("What is the chemical symbol for water?", new String[]{"H2O", "CO2", "NaCl", "O2"}, 0));
        mcqList.add(new MCQ("What should we do during avalange?",new String[]{"Run","Search for Safe Place"," Get on the Ground"},1));
        mcqList.add(new MCQ("What is the main cause of shrinking artic sea ice",new String[]{"Deforestation","Melting ice cap","Ocean Acidificatin","Green house gas emission"},3));
        MCQAdapter adapter = new MCQAdapter(mcqList);
        recyclerView.setAdapter(adapter);
    }
}

