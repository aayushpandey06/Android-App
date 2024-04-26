package com.example.project;

// MCQAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MCQAdapter extends RecyclerView.Adapter<MCQAdapter.MCQViewHolder> {

    private List<MCQ> mcqList;
    private int correctCounter = 0;

    public MCQAdapter(List<MCQ> mcqList) {
        this.mcqList = mcqList;
    }

    @NonNull
    @Override
    public MCQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mcq_item, parent, false);
        return new MCQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MCQViewHolder holder, int position) {
        MCQ mcq = mcqList.get(position);
        holder.textViewQuestion.setText(mcq.getQuestion());

        String[] options = mcq.getOptions();
        for (int i = 0; i < options.length; i++) {
            RadioButton radioButton = (RadioButton) holder.radioGroupOptions.getChildAt(i);
            radioButton.setText(options[i]);
        }

        holder.radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton checkedRadioButton = group.findViewById(checkedId);
            int checkedIndex = group.indexOfChild(checkedRadioButton);
            if (checkedIndex == mcq.getCorrectOption()) {
                correctCounter++;
                Toast.makeText(holder.itemView.getContext(), "Correct! Your score: " + correctCounter, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mcqList.size();
    }

    static class MCQViewHolder extends RecyclerView.ViewHolder {
        TextView textViewQuestion;
        RadioGroup radioGroupOptions;

        public MCQViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewQuestion = itemView.findViewById(R.id.textViewQuestion);
            radioGroupOptions = itemView.findViewById(R.id.radioGroupOptions);
        }
    }
}
