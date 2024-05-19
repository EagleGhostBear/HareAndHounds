package com.main.HareAndHounds;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.*;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.main.HareAndHounds.functions.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlayActivity extends AppCompatActivity
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
                            chk_turn();
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
        update_img();
    }

    public int dijkstra(int start) {
        int[] visited = new int[11];
        ArrayList<ArrayList<Integer>> path = new ArrayList<>();
        PriorityQueue<Node> pq =  new PriorityQueue<>();

        for (int i = 0; i < 11; i++) {
            path.add(new ArrayList<Integer>());
        }
        path.get(start).add(start);

        Arrays.fill(visited, INF);
        visited[start] = 0;
        pq.offer(nodes[start]);

        while(!pq.isEmpty()) {
            Node now = pq.poll();

            for(int i=0; i<11; i++){
                if(connect[now.id][i] == 1){ // 해당 보드와 연결된 보드중
                    if(i == token_loc[0] || i == token_loc[1] || i == token_loc[2]){ // 연결된 보드중 다른말이 이미 있는 경우
                        continue;
                    }
                    if(visited[now.id] + 1 >= visited[i]){
                        continue;
                    }
                    path.set(i, new ArrayList<>(path.get(now.id)));
                    path.get(i).add(i);
                    visited[i] = visited[now.id] + 1;
                    pq.offer(nodes[i]);
                }
            }
        }

        if(visited[0] > 99999){ // 만약 목적지까지 가는 경로가 없는 경우
            int max_val = -1;
            int max_index = -1;
            for (int i = 0; i < path.size(); i++) {
                int now_size = path.get(i).size();
                if (now_size > max_val) {
                    max_val = now_size;
                    max_index = i;
                }
            }
            if(path.get(max_index).size() == 1){ // Hounds들에게 갇혀서 못움직일 경우
                return path.get(max_index).get(0);
            }
            else{ // 현재까지의 최선의 경로로 일단 이동
                return path.get(max_index).get(1);
            }
        }
        else{ // 최단 경로로 이동
            return path.get(0).get(1);
        }
    }

    public void update_hare(){
        int next_loc = dijkstra(token_loc[3]);
        token_loc[3] = next_loc;
        chk_win();
        chk_turn();
        update_img();
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
        turn_count+=1;
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
        if(cnt1 == cnt2){
            hound_win();
        }
        else if(token_loc[3] == 0){
            hare_win();
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
        setContentView(R.layout.activity_play);

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
                    update_hound(nodes[global_i]);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            if(turn_type) {update_hare();}
                        }
                    }, 1000);
                }
            });
        }

        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node(i, boards[i]);
        }

        home_btn = findViewById(R.id.home_btn);
        houndturn = findViewById(R.id.houndTurn);
        hareturn = findViewById(R.id.hareTurn);
        count_turn = findViewById(R.id.cntTurn);
        popup_hounds_win = findViewById(R.id.hounds_win);
        popup_hare_win = findViewById(R.id.hare_win);

        nodes[0].img.setImageResource(R.drawable.houndboard);
        nodes[1].img.setImageResource(R.drawable.houndboard);
        nodes[3].img.setImageResource(R.drawable.houndboard);
        nodes[10].img.setImageResource(R.drawable.hareboard);

        popup_hounds_win.setVisibility(View.INVISIBLE);
        popup_hare_win.setVisibility(View.INVISIBLE);

        count_turn.setText(Integer.toString(turn_count));
        hareturn.setVisibility(View.INVISIBLE);

        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        popup_hounds_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        popup_hare_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}