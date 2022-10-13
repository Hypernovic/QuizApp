package com.example.quizapp.Adapters;


import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.LayoutInflater;

import com.example.quizapp.Models.QAs.Answer;
import com.example.quizapp.Models.QAs.QuestionAnswers;
import com.example.quizapp.R;

import java.util.ArrayList;
import java.util.List;


public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.CustomViewHolder> {

    private Context context;
    private List<Answer> Answers;
    private ArrayList<Integer> selectCheck = new ArrayList<>();
    private QuestionAnswers Question;
    private int changed=0;


    public AnswerAdapter(Context context, List<Answer> answers, QuestionAnswers question) {
        this.context = context;
        this.Answers = answers;
        this.Question = question;
        for (int i = 0; i < answers.size(); i++) {
            selectCheck.add(2);
        }

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.answerticket, parent, false));
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.itemAnswer.setText(Answers.get(position).getAnswer());

        if (selectCheck.get(position) == 1) {
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.correct_option_border_bg));
        }else if(selectCheck.get(position) == 2){
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.default_option_border_bg));
        }else if(selectCheck.get(position) == 3){
            holder.layout.setBackground(ContextCompat.getDrawable(context, R.drawable.wrong_option_border_bg));
        }




            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(changed==0){
                    if (Answers.get(position).getRightChoice()==1){
                        Question.setCorrectChoice(1);
                        selectCheck.set(position,1);
                    }else {
                        selectCheck.set(position,3);
                        Question.setCorrectChoice(2);
                        for (int k=0; k<Answers.size(); k++){
                            if(Answers.get(k).getRightChoice()==1){
                                selectCheck.set(k,1);
                            }
                        }
                    }
                    changed=1;
                    notifyDataSetChanged();}
                }
            });





    }

    @Override
    public int getItemCount() {
        return Answers.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView itemAnswer;
        private LinearLayout layout;

        public CustomViewHolder(View view) {
            super(view);
            itemAnswer = view.findViewById(R.id.item_answer);
            layout=view.findViewById(R.id.answerLayout);
        }
    }
}
