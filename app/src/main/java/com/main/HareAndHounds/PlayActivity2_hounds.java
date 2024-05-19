package com.main.HareAndHounds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.main.HareAndHounds.functions.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class PlayActivity2_hounds extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    Node[] nodes = new Node[11];
    ImageView[] boards = new ImageView[11];
    ImageView houndturn, hareturn, home_btn, popup_hounds_win, popup_hare_win;
    TextView count_turn;
    final int INF = Integer.MAX_VALUE;
    int turn_count = 1;
    boolean turn_type = false; // Ture: Hare, False: Hound

    // 0 ~ 10번까지의 보드 연결 정보
    int[][] connect = {
            // 0번 보드 연결 정보
            {0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            // 1번 보드 연결 정보
            {1, 0, 1, 0, 1, 1, 0, 0, 0, 0, 0},
            // 2번 보드 연결 정보
            {1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0},
            // 3번 보드 연결 정보
            {1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0},
            // 4번 보드 연결 정보
            {0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 0},
            // 5번 보드 연결 정보
            {0, 1, 1, 1, 1, 0, 1, 1, 1, 1, 0},
            // 6번 보드 연결 정보
            {0, 0, 0, 1, 0, 1, 0, 0, 0, 1, 0},
            // 7번 보드 연결 정보
            {0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1},
            // 8번 보드 연결 정보
            {0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1},
            // 9번 보드 연결 정보
            {0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1},
            // 10번 보드 연결 정보
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0}
    };

    // 말들의 위치 관리 배열, 0~2 = Hound, 3 = Hare
    int[] token_loc = {0, 1, 3, 10};

    // 현재 선택된 보드
    int select_board = -1;

    public void upload_state(){
        // int 배열을 List<Integer>로 변환
        List<Integer> token_list = new ArrayList<>();
        for (int value : token_loc) {
            token_list.add(value);
        }
        
        // Hare, Hound 말들의 상태를 업로드
        databaseReference.child("token").setValue(token_list)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // 데이터가 성공적으로 업로드됨
                        Log.d("Firebase", "Data uploaded successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // 데이터 업로드 실패
                        Log.e("Firebase", "Data upload failed.", e);
                    }
                });

        // 현재 게임 턴수 업로드
        databaseReference.child("turn").setValue(turn_count + 1);
    }

    public void update_hound(Node select_node){ // hounds 보드냐? 아니냐?
        if(select_board != -1){ // 선택된 보드가 있을 경우
            if(select_node.id == select_board){ // 선택된 보드를 눌렀을때 다시 초기화
                select_board = -1;
            }
            else{ // 이동 가능한 보드를 눌렀을 경우
                // 1. 연결 여부, 2. 후진 불가 판단, 3. 다른 말이 있는 곳은 불가
                if(connect[select_board][select_node.id] == 1 && nodes[select_node.id].depth >= nodes[select_board].depth && !(select_node.id == token_loc[0] || select_node.id == token_loc[1] || select_node.id == token_loc[2] || select_node.id == token_loc[3])){
                    for(int j=0; j<3; j++){ // toeken_loc에서 이동한 Hound index를 찾아 업데이트
                        if(token_loc[j] == select_board){
                            token_loc[j] = select_node.id;
                            select_board = -1;
//                            chk_win();
                            upload_state();
                        }
                    }
                }
            }
        }
        else{ // 선택된 보드가 없는 경우, Hound만 터치가능
            for(int i=0; i<3; i++){
                if(select_node.id == token_loc[i]){ // Hound 보드를 눌렀을 경우
                    select_board = select_node.id; // select_board 선택된 보드로 업데이트
                    break;
                }
            }
        }
        update_img(); // select 파악을 위해 호출
    }

    public void update_img(){ // 현재 보드 상태를 업데이트
        for(int i=0; i<11; i++){ // 모든 보드 상태 초기화
            nodes[i].img.setImageResource(R.drawable.board);
        }

        if(select_board != -1){ // 선택된 Hound가 있을 경우
            nodes[select_board].img.setImageResource(R.drawable.selecthound);
            for(int i=0; i<11; i++){ // 선택된 보드 기준 이동 가능 보드 표시, Hound는 후진 불가이기 때문에 depth 조건문 추가
                if(connect[select_board][i] == 1 && nodes[i].depth >= nodes[select_board].depth){
                    nodes[i].img.setImageResource(R.drawable.select);
                }
            }
        }
        for(int i=0; i<3; i++){ // Hound 보드 표기
            if(token_loc[i] == select_board){continue;}
            nodes[token_loc[i]].img.setImageResource(R.drawable.houndboard);
        }
        nodes[token_loc[3]].img.setImageResource(R.drawable.hareboard);
    }

    public void chk_turn()
    {
        if(turn_count % 2 == 1) // 홀수턴 일때
        {
            houndturn.setVisibility(View.VISIBLE);
            hareturn.setVisibility(View.INVISIBLE);
            count_turn.setText(Integer.toString(turn_count));
        }
        else // 짝수턴 일때
        {
            houndturn.setVisibility(View.INVISIBLE);
            hareturn.setVisibility(View.VISIBLE);
            count_turn.setText(Integer.toString(turn_count));
        }
        turn_type = !turn_type;
    }

    public void chk_win(){
        int cnt1 = token_loc[0] + token_loc[1] + token_loc[2];
        int cnt2 = 0;
        for(int i=0; i<11; i++){
            if(connect[token_loc[3]][i] == 1){
                cnt2 += i;
            }
        }

        databaseReference.child("connect").removeValue();
        if(cnt1 == cnt2){ // Hound Win
            databaseReference.child("result").child("hounds").setValue(1);
        }
        else if(token_loc[3] == 0){ // Hare Win
            databaseReference.child("result").child("hare").setValue(1);
        }
    }

    public void hound_win()
    {
        YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(popup_hounds_win);
        popup_hounds_win.setVisibility(View.VISIBLE);
    }

    public void hare_win()
    {
        YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(popup_hare_win);
        popup_hare_win.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2_hounds);

        // 반복문을 통해 각 보드를 찾아서 할당
        for (int i = 0; i < 11; i++) {
            final int global_i = i;
            int resourceId = getResources().getIdentifier("board" + i, "id", getPackageName());
            boards[i] = findViewById(resourceId);

            boards[i].setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (!turn_type) { // false 일때 hounds turn
                        update_hound(nodes[global_i]);
                    }
                }
            });
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(i, boards[i]);
        }

        nodes[0].img.setImageResource(R.drawable.houndboard);
        nodes[1].img.setImageResource(R.drawable.houndboard);
        nodes[3].img.setImageResource(R.drawable.houndboard);
        nodes[10].img.setImageResource(R.drawable.hareboard);

        home_btn = findViewById(R.id.home_btn);
        houndturn = findViewById(R.id.houndTurn);
        hareturn = findViewById(R.id.hareTurn);
        count_turn = findViewById(R.id.cntTurn);
        popup_hounds_win = findViewById(R.id.hounds_win);
        popup_hare_win = findViewById(R.id.hare_win);

        popup_hounds_win.setVisibility(View.INVISIBLE);
        popup_hare_win.setVisibility(View.INVISIBLE);

        count_turn.setText(Integer.toString(turn_count));
        hareturn.setVisibility(View.INVISIBLE);

        databaseReference.child("token").removeValue();
        databaseReference.child("turn").removeValue();

        databaseReference.child("result/restart").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    int check_victory=Integer.parseInt(rdata.toString());
                    if(check_victory==1)
                    {
                        databaseReference.child("result").removeValue();
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("result/hounds").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    int check_victory=Integer.parseInt(rdata.toString());
                    if(check_victory==1)
                    {
                        hound_win();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("result/hare").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    int check_victory=Integer.parseInt(rdata.toString());
                    if(check_victory==1)
                    {
                        hare_win();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("token").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    // 데이터를 List<Integer>로 변환
                    List<Integer> token_list = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Integer value = snapshot.getValue(Integer.class);
                        if (value != null) {
                            token_list.add(value);
                        }
                    }
                    // db로 부터 token_loc 업데이트
                    for (int i = 0; i < token_list.size(); i++) {
                        token_loc[i] = token_list.get(i);
                    }
                    update_img();
                    chk_win();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        databaseReference.child("turn").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists()){ // onDataChange null 참초 에러 방지
                    Object rdata = dataSnapshot.getValue();
                    turn_count=Integer.parseInt(rdata.toString());
                    chk_turn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 읽기 실패
                Log.e("Firebase", "Failed to read data.", databaseError.toException());
            }
        });

        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("result").child("restart").setValue(1);
            }
        });

        popup_hounds_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity2_hounds.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                databaseReference.child("result").child("restart").setValue(1);
            }
        });

        popup_hare_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity2_hounds.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                databaseReference.child("result").child("restart").setValue(1);
            }
        });
    }
}
