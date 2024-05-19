package com.main.HareAndHounds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    ImageView main_btn_start, main_btn_pause, main_btn_player1, main_btn_player2, main_btn_hounds, main_btn_hare, connecting;
    AnimationDrawable ani;
    TextView producer_text;
    int count, check_connect;
    boolean check_connect_honds, check_connect_hare, select_hounds, select_hare;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.hide();

        main_btn_start = findViewById(R.id.mainbtn_start);
        main_btn_pause = findViewById(R.id.mainbtn_pause);
        main_btn_player1 = findViewById(R.id.mainbtn_player1);
        main_btn_player2 = findViewById(R.id.mainbtn_player2);
        main_btn_hounds = findViewById(R.id.mainbtn_hounds);
        main_btn_hare = findViewById(R.id.mainbtn_hare);
        connecting = findViewById(R.id.connect_ani);
        producer_text = findViewById(R.id.producer);

        ani=(AnimationDrawable)connecting.getDrawable();
        ani.start();

        main_btn_player1.setVisibility(View.INVISIBLE);
        main_btn_player2.setVisibility(View.INVISIBLE);
        main_btn_hounds.setVisibility(View.INVISIBLE);
        main_btn_pause.setVisibility(View.INVISIBLE);
        main_btn_hare.setVisibility(View.INVISIBLE);
        producer_text.setVisibility(View.INVISIBLE);
        connecting.setVisibility(View.INVISIBLE);

        count=0;
        check_connect=0;

        check_connect_honds=false;
        check_connect_hare=false;
        select_hounds=false;
        select_hare=false;

        databaseReference.child("connect").removeValue();

        main_btn_start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                main_btn_start.setVisibility(View.INVISIBLE);

                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_player1);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_player2);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_pause);
                main_btn_player1.setVisibility(View.VISIBLE);
                main_btn_player2.setVisibility(View.VISIBLE);
                main_btn_pause.setVisibility(View.VISIBLE);

                if(count>15){producer_text.setVisibility(View.VISIBLE);}
                count+=1;
            }
        });

        main_btn_pause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                main_btn_player1.setVisibility(View.INVISIBLE);
                main_btn_player2.setVisibility(View.INVISIBLE);
                main_btn_hounds.setVisibility(View.INVISIBLE);
                main_btn_hare.setVisibility(View.INVISIBLE);
                main_btn_pause.setVisibility(View.INVISIBLE);

                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_start);
                main_btn_start.setVisibility(View.VISIBLE);

                if(count>15){producer_text.setVisibility(View.VISIBLE);}
                count+=1;
            }
        });

        main_btn_player1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                producer_text.setVisibility(View.INVISIBLE);
                count=0;

                Intent intent1 = new Intent(getApplicationContext(), PlayActivity.class);
                startActivity(intent1);
            }
        });

        main_btn_player2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                main_btn_player1.setVisibility(View.INVISIBLE);
                main_btn_player2.setVisibility(View.INVISIBLE);

                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_hounds);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_hare);
                main_btn_hounds.setVisibility(View.VISIBLE);
                main_btn_hare.setVisibility(View.VISIBLE);
            }
        });

        main_btn_hounds.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                producer_text.setVisibility(View.INVISIBLE);
                count=0;

                main_btn_hounds.setVisibility(View.INVISIBLE);
                main_btn_hare.setVisibility(View.INVISIBLE);
                main_btn_pause.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(connecting);
                connecting.setVisibility(View.VISIBLE);

                databaseReference.child("connect").child("hounds").setValue(1);
                select_hounds=true;
                select_hare=false;
            }
        });

        main_btn_hare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                producer_text.setVisibility(View.INVISIBLE);
                count=0;

                main_btn_hounds.setVisibility(View.INVISIBLE);
                main_btn_hare.setVisibility(View.INVISIBLE);
                main_btn_pause.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(connecting);
                connecting.setVisibility(View.VISIBLE);

                databaseReference.child("connect").child("hare").setValue(1);
                select_hounds=false;
                select_hare=true;
            }
        });

        connecting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                connecting.setVisibility(View.INVISIBLE);
                YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(main_btn_start);
                main_btn_start.setVisibility(View.VISIBLE);

                select_hounds=false;
                select_hare=false;
                check_connect_honds=false;
                check_connect_hare=false;
                databaseReference.child("connect").child("hounds").setValue(0);
                databaseReference.child("connect").child("hare").setValue(0);
            }
        });

        databaseReference.child("connect/hounds").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    check_connect=Integer.parseInt(rdata.toString());
                    if(check_connect==1)
                    {
                        check_connect_honds=true;
                    }
                    if(check_connect_honds && check_connect_hare)
                    {
                        databaseReference.child("connect").child("success").setValue(1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("connect/hare").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    check_connect=Integer.parseInt(rdata.toString());
                    if(check_connect==1)
                    {
                        check_connect_hare=true;
                    }
                    if(check_connect_honds && check_connect_hare)
                    {
                        databaseReference.child("connect").child("success").setValue(1);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("connect/success").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    check_connect=Integer.parseInt(rdata.toString());
                    if(check_connect==1)
                    {
                        check_connect_honds=false;
                        check_connect_hare=false;
                        if(select_hounds)
                        {
                            select_hounds=false;
                            select_hare=false;
                            connecting.setVisibility(View.INVISIBLE);
                            main_btn_start.setVisibility(View.VISIBLE);
                            Intent intent2 = new Intent(getApplicationContext(), PlayActivity2_hounds.class);
                            startActivity(intent2);
                        }
                        if(select_hare)
                        {
                            select_hounds=false;
                            select_hare=false;
                            connecting.setVisibility(View.INVISIBLE);
                            main_btn_start.setVisibility(View.VISIBLE);
                            Intent intent3 = new Intent(getApplicationContext(), PlayActivity2_hare.class);
                            startActivity(intent3);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });
    }
}