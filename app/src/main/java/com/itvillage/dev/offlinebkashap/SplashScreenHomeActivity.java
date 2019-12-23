package com.itvillage.dev.offlinebkashap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.itvillage.dev.offlinebkashap.adapter.IntroViewPagerAdaptor;
import com.itvillage.dev.offlinebkashap.model.ScreenItems;

import java.util.ArrayList;
import java.util.List;

public class SplashScreenHomeActivity extends AppCompatActivity {
    IntroViewPagerAdaptor introViewPagerAdaptor;
    private ViewPager screenPager;
    private Button getStart, next;
    private TabLayout tabInda;
    private int position;
    private Animation butAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final List<ScreenItems> mList = new ArrayList<>();
        mList.add(new ScreenItems("অফলাইন বিকাশ আ্যপ কি ?",
                "বিকাশে লেনদেন হবে এখন অফলাইন আ্যপের মাধ্যমে কোন ইন্টারনেট খরচ ছাড়াই,আরো সহজে।", R.drawable.logofinal));

        mList.add(new ScreenItems("সেন্ডমানি করুন",
                "আপনার পার্সনাল একাউন্ট থেকে পার্সনাল একাউন্টে সেন্ড মানি করুন আকর্ষণীয় আ্যপ ডিজাইনে খুব সহজে যেকোন সময়।", R.drawable.sentmoneyt));
        mList.add(new ScreenItems("ক্যাশআউট করুন",
                "নাম্বার ভুল হবার আর কোনো সম্ভবনাই নেই! বিকাশ আউটলেটে যান,ফোনবুকে সেভ করা এজেন্ট নাম্বারটি সিলেক্ট করুন,বুঝে নিন আপনার টাকা।", R.drawable.cashout));
        mList.add(new ScreenItems("বিকাশ ক্যালকুলেটর কি ?",
                "কত টাকা বিকাশে খরচ এটা নিয়ে চিন্তিত!! \"বিকাশ আ্যপ আপনাকে বলে দিবে আপনার খরচ তাও আবার এক ক্লিকে। ", R.drawable.calculator));
        mList.add(new ScreenItems("বিকাশ মোবাইল রির্চাজ করুন",
                "ঘরে বসেই করুন মোবাইল রির্চাজ,নাম্বার সিলেক্ট করুন আপনার ফোনবুক থেকেই।", R.drawable.mobilerecharge));
        mList.add(new ScreenItems("বিকাশে মাধ্যমে পেমেন্ট করুন",
                "পেমেন্ট সেবার মাধ্যমে, বিকাশ গ্রহণ করে এমন যেকোন মার্চেন্টকে আপনি পেমেন্ট করতে পারেন আপনার বিকাশ একাউন্ট থেকে। এখন আপনি দেশজুড়ে ৪৭,০০০-এর বেশি দোকানে কেনাকাটার পেমেন্ট বিকাশ করতে পারবেন।", R.drawable.paymentlogo));

        screenPager = findViewById(R.id.pageView);
        getStart = findViewById(R.id.getStart);
        next = findViewById(R.id.next);
        tabInda = findViewById(R.id.tabInda);

        butAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.get_start_animation);

        introViewPagerAdaptor = new IntroViewPagerAdaptor(this, mList);
        screenPager.setAdapter(introViewPagerAdaptor);

        tabInda.setupWithViewPager(screenPager);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }
                if (position == mList.size() - 1) {
                    loadLastScreen();
                }
            }
        });
        tabInda.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        getStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGetStartVideoDemo();
            }
        });
    }

    private void loadLastScreen() {
        getStart.setVisibility(View.VISIBLE);
        next.setVisibility(View.INVISIBLE);
        tabInda.setVisibility(View.INVISIBLE);
        getStart.setAnimation(butAnimation);
    }

    public void showGetStartVideoDemo() {

        Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/siyam_rupali.ttf");
        View view = getLayoutInflater().inflate(R.layout.dialog_show_getstart_video_demo, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);

        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.basic_tutorial_promo;
        Uri uri = Uri.parse(videoPath);

        Button skipbut = view.findViewById(R.id.getstart);
        VideoView videoView = view.findViewById(R.id.showDemo);
        TextView instrction = view.findViewById(R.id.instraction);

        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.start();

        instrction.setTypeface(typeface);
        instrction.setText("টিউটোরিয়াল ক্লিপ");
        skipbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.privecy_policy, null);
                AlertDialog alertDialog = new AlertDialog.Builder(SplashScreenHomeActivity.this).create();
                alertDialog.setCancelable(false);
                CheckBox checkBox = view.findViewById(R.id.checkBox);
                Button exit = view.findViewById(R.id.exit);
                final Button getstart = view.findViewById(R.id.getstart);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (((CompoundButton) view).isChecked()) {
                            getstart.setBackground(ContextCompat.getDrawable(SplashScreenHomeActivity.this, R.drawable.but_gradient));
                            getstart.setEnabled(true);
                        } else {
                            getstart.setEnabled(false);
                        }
                    }
                });
                getstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(SplashScreenHomeActivity.this, HomeActivity.class));
                    }
                });
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                        ActivityCompat.finishAffinity((Activity) SplashScreenHomeActivity.this);
                        int pid = android.os.Process.myPid();
                        android.os.Process.killProcess(pid);
                    }
                });
                alertDialog.setView(view);
                alertDialog.show();
                //
            }
        });

        alertDialog.setView(view);
        alertDialog.show();

    }
}
