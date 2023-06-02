package com.example.quiz.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.quiz.Models.QuestionModel;
import com.example.quiz.R;
import com.example.quiz.databinding.ActivityQuestionBinding;

import java.util.ArrayList;

    public class QuestionActivity extends AppCompatActivity {

        ArrayList<QuestionModel> list=new ArrayList<>();
        private int count=0;
        private int position=0;
        private int score=0;
        CountDownTimer timer;
        ActivityQuestionBinding binding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding=ActivityQuestionBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

                getSupportActionBar().hide();

                resetTimer();
                timer.start();

            String setName=getIntent().getStringExtra("set");
            if(setName.equals("SET-1"))
            {
                setOne();

            }
            else if(setName.equals("SET-2"))
            {
                setTwo();
            }

            for(int i=0;i<4;i++) {
                binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        checkAnswer((Button) view);

                    }
                });

            }

            playAnimation(binding.question,0,list.get(position).getQuestion());

            binding.btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(timer!=null){
                        timer.cancel();
                    }
                    timer.start();
                    binding.btnNext.setEnabled(false);
                    binding.btnNext.setAlpha((float) 0.3);
                    enableOption(true);
                    position ++;

                    if(position ==list.size()){
                        Intent intent=new Intent(QuestionActivity.this,ScoreActivity.class);
                        intent.putExtra("score",score);
                        intent.putExtra("total",list.size());
                        startActivity(intent);
                        finish();
                        return;
                    }


                    count=0;

                    playAnimation(binding.question,0,list.get(position).getQuestion());
                }
            });
        }

        private void resetTimer() {

            timer=new CountDownTimer(30000,1000) {
                @Override
                public void onTick(long l) {

                    binding.timer.setText(String.valueOf(l/1000));
                }

                @Override
                public void onFinish() {
                    Dialog dialog=new Dialog(QuestionActivity.this);
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    dialog.setContentView(R.layout.timeout_dialog);
                    dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent=new Intent(QuestionActivity.this,SetsActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                    dialog.show();


                }
            };
        }

        private void playAnimation(View view, int value, String data) {

            view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                    .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(@NonNull Animator animator) {

                            if(value ==0 && count<4){
                                String option = "";
                                if(count ==0){
                                    option =list.get(position).getOptionA();
                                }
                                else if(count==1){
                                    option=list.get(position).getOptionB();
                                }

                                else if(count==2){
                                    option=list.get(position).getOptionC();
                                }
                                else if(count==3){
                                    option=list.get(position).getOptionD();
                                }

                                playAnimation(binding.optionContainer.getChildAt(count),0,option);
                                count ++;

                            }

                        }
                        @Override
                        public void onAnimationEnd(@NonNull Animator animator) {

                            if(value==0){

                                try {
                                    ((TextView) view).setText(data);
                                    binding.totalQuestion.setText(String.format("%d/%d", position + 1, list.size()));

                                }catch(Exception e){
                                    ((Button)view).setText(data);

                                }
                                view.setTag(data);
                                playAnimation(view,1,data);

                            }

                        }
                        @Override
                        public void onAnimationCancel(@NonNull Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(@NonNull Animator animator) {

                        }
                    });

        }

        private void enableOption(boolean enable) {

            for(int i=0;i<4;i++){
                binding.optionContainer.getChildAt(i).setEnabled(enable);

                if(enable){
                    binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
                }

            }



        }


        private void checkAnswer(Button selectedOption) {

            if(timer!=null){
                timer.cancel();
            }


            binding.btnNext.setEnabled(true);
            binding.btnShare.setAlpha(1);

            if(selectedOption.getText().toString().equals(list.get(position).getCorrectAnswer())){

                score ++;
                selectedOption.setBackgroundResource(R.drawable.right_answ);
            }
            else{
                selectedOption.setBackgroundResource(R.drawable.wrong_answ);
                Button correctOption =(Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectAnswer());
                correctOption.setBackgroundResource(R.drawable.right_answ);
            }
        }

        private void setTwo() {
            list.add(new QuestionModel("How many trade points have been opened between Nepal and India ?" ,
                    "30", " 27","26","50","27"));

            list.add(new QuestionModel("How many heritage properties are listed in the World Heritage List? " , "25",
                    " 30","23","80","23"));

            list.add(new QuestionModel("How many trade points have been opened between Nepal and India ?" ,
                    "30", " 27","26","50","27"));

            list.add(new QuestionModel("How many heritage properties are listed in the World Heritage List? " , "25",
                    " 30","23","80","23"));


        }

        private void setOne() {

            list.add(new QuestionModel("1. The fundamental principal of surveying is to work from the " , "A. part to the whole",
                    "B. whole to the part","C. lower level to higher level","D. higher level to the lover level","B. whole to the part"));

            list.add(new QuestionModel("How many trade points have been opened between Nepal and India ?" ,
                    "30", "27","26","50","27"));

            list.add(new QuestionModel("How many heritage properties are listed in the World Heritage List? " , "25",
                    " 30","23","80","23"));

            list.add(new QuestionModel("How many trade points have been opened between Nepal and India ?" ,
                    "30", "27","26","50","27"));

        }
    }