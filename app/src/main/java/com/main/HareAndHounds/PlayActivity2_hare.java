package com.main.HareAndHounds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.concurrent.TimeUnit;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.main.HareAndHounds.functions.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PlayActivity2_hare extends AppCompatActivity
{
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

    ImageView tree_board11, tree_board21, tree_board22, tree_board23, tree_board31, tree_board32, tree_board33, tree_board41, tree_board42, tree_board43, tree_board51, houndturn, hareturn, home_btn, popup_hounds_win, popup_hare_win;
    TextView count_turn;
    Node save_node, hound_node, hare_node, min_deepth_node, back_deepth_node;
    int turn_count, min_deepth, hound_min_deepth, check_victory, check_rdata1, check_rdata2;
    boolean possible_move, hound_movecheck, hare_movecheck, hound_turn, hare_turn;

    public void check_route(Node inputnode) // 1.해당 노드가 선택된 상태인지 아닌지를 판별 2.이미 선택된 노드라면 선택을 꺼줌 3. 선택이 안되어 있다면 이동 가능한 노드를 찾아서 보여줌
    {
        if(inputnode.select) // 이미 선택 되었던 보드라면
        {
            if(inputnode.up != null && inputnode.up.connect)
            {
                inputnode.up.connect=false;
                inputnode.up.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.down != null && inputnode.down.connect)
            {
                inputnode.down.connect=false;
                inputnode.down.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.left != null && inputnode.left.connect)
            {
                inputnode.left.connect=false;
                inputnode.left.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.right != null && inputnode.right.connect)
            {
                inputnode.right.connect=false;
                inputnode.right.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.upleft != null && inputnode.upleft.connect)
            {
                inputnode.upleft.connect=false;
                inputnode.upleft.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.upright != null && inputnode.upright.connect)
            {
                inputnode.upright.connect=false;
                inputnode.upright.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.downleft != null && inputnode.downleft.connect)
            {
                inputnode.downleft.connect=false;
                inputnode.downleft.myimage.setImageResource(R.drawable.board);
            }
            if(inputnode.downright != null && inputnode.downright.connect)
            {
                inputnode.downright.connect=false;
                inputnode.downright.myimage.setImageResource(R.drawable.board);
            }
            inputnode.myimage.setImageResource(R.drawable.hareboard);
            inputnode.select=false;
            save_node=null;
            return;
        }
        else // 선택 안되어있는 보드
        {
            if(inputnode.connect) // 말을 선택한뒤 이동할 보드 선택
            {
                save_node.select=false;
                if(save_node.up != null)
                {
                    if(save_node.up.connect)
                    {
                        save_node.up.connect=false;
                        save_node.up.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.down != null)
                {
                    if(save_node.down.connect)
                    {
                        save_node.down.connect=false;
                        save_node.down.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.left != null)
                {
                    if(save_node.left.connect)
                    {
                        save_node.left.connect=false;
                        save_node.left.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.right != null)
                {
                    if(save_node.right.connect)
                    {
                        save_node.right.connect=false;
                        save_node.right.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.upleft != null)
                {
                    if(save_node.upleft.connect)
                    {
                        save_node.upleft.connect=false;
                        save_node.upleft.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.upright != null)
                {
                    if(save_node.upright.connect)
                    {
                        save_node.upright.connect=false;
                        save_node.upright.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.downleft != null)
                {
                    if(save_node.downleft.connect)
                    {
                        save_node.downleft.connect=false;
                        save_node.downleft.myimage.setImageResource(R.drawable.board);
                    }
                }
                if(save_node.downright != null)
                {
                    if(save_node.downright.connect)
                    {
                        save_node.downright.connect=false;
                        save_node.downright.myimage.setImageResource(R.drawable.board);
                    }
                }
                databaseReference.child("hare").child("move").setValue(inputnode.boardnum);
                hare_node=inputnode;
                hare_turn=false;
                save_node.hare=false;
                save_node.myimage.setImageResource(R.drawable.board);
                inputnode.hare=true;
                inputnode.myimage.setImageResource(R.drawable.hareboard);
                save_node=null;
                hare_movecheck=true;
                turn_count+=1;
                check_turn();
                return;
            }
            else // 첫번째 선택
            {
                if(save_node != null && save_node.select)
                {
                    return;
                }
                else
                {
                    if(inputnode.hare) // 늑대 or 토끼가 있는 칸
                    {
                        if(inputnode.up != null)
                        {
                            if(inputnode.up.hound == false && inputnode.up.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.up.connect=true;
                                inputnode.up.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.down != null)
                        {
                            if(inputnode.down.hound == false && inputnode.down.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.down.connect=true;
                                inputnode.down.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.left != null)
                        {
                            if(inputnode.left.hound == false && inputnode.left.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.left.connect=true;
                                inputnode.left.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.right != null)
                        {
                            if(inputnode.right.hound == false && inputnode.right.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.right.connect=true;
                                inputnode.right.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.upleft != null)
                        {
                            if(inputnode.upleft.hound == false && inputnode.upleft.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.upleft.connect=true;
                                inputnode.upleft.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.upright != null)
                        {
                            if(inputnode.upright.hound == false && inputnode.upright.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.upright.connect=true;
                                inputnode.upright.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.downleft != null)
                        {
                            if(inputnode.downleft.hound == false && inputnode.downleft.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.downleft.connect=true;
                                inputnode.downleft.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        if(inputnode.downright != null)
                        {
                            if(inputnode.downright.hound == false && inputnode.downright.hare == false) // 해당칸에 hound 와 hare이 없다.
                            {
                                inputnode.select=true;
                                inputnode.downright.connect=true;
                                inputnode.downright.myimage.setImageResource(R.drawable.select);
                            }
                        }
                        inputnode.myimage.setImageResource(R.drawable.selecthare);
                        save_node=inputnode;
                        return;
                    }
                }
            }
        }
    }

    public void check_turn()
    {
        if(turn_count % 2 == 1) // 홀수턴 일때
        {
            houndturn.setVisibility(View.VISIBLE);
            hareturn.setVisibility(View.INVISIBLE);
            String tmp = Integer.toString(turn_count);
            count_turn.setText(tmp);
        }
        else // 짝수턴 일때
        {
            houndturn.setVisibility(View.INVISIBLE);
            hareturn.setVisibility(View.VISIBLE);
            String tmp = Integer.toString(turn_count);
            count_turn.setText(tmp);
        }
    }

    public void hound_win()
    {
        turn_count=1;
        YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(popup_hounds_win);
        popup_hounds_win.setVisibility(View.VISIBLE);
    }

    public void hare_win()
    {
        turn_count=1;
        YoYo.with(Techniques.FadeIn).duration(600).repeat(0).playOn(popup_hare_win);
        popup_hare_win.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play2_hare);

        tree_board11 = findViewById(R.id.board11);
        tree_board21 = findViewById(R.id.board21);
        tree_board22 = findViewById(R.id.board22);
        tree_board23 = findViewById(R.id.board23);
        tree_board31 = findViewById(R.id.board31);
        tree_board32 = findViewById(R.id.board32);
        tree_board33 = findViewById(R.id.board33);
        tree_board41 = findViewById(R.id.board41);
        tree_board42 = findViewById(R.id.board42);
        tree_board43 = findViewById(R.id.board43);
        tree_board51 = findViewById(R.id.board51);
        home_btn = findViewById(R.id.home_btn);
        houndturn = findViewById(R.id.houndTurn);
        hareturn = findViewById(R.id.hareTurn);
        count_turn = findViewById(R.id.cntTurn);
        popup_hounds_win = findViewById(R.id.hounds_win);
        popup_hare_win = findViewById(R.id.hare_win);

        Node node11 = new Node();
        Node node21 = new Node();
        Node node22 = new Node();
        Node node23 = new Node();
        Node node31 = new Node();
        Node node32 = new Node();
        Node node33 = new Node();
        Node node41 = new Node();
        Node node42 = new Node();
        Node node43 = new Node();
        Node node51 = new Node();

        node11.setvalue(tree_board11, 1, 1, true, false, false, false, null, null, null, node22, null, node21, null, node23);
        node21.setvalue(tree_board21, 2, 2, true, false, false, false, null, node22, null, node31, null, null, node11, node32);
        node22.setvalue(tree_board22, 3, 2, false, false, false, false, node21, node23, node11, node32, null, null, null, null);
        node23.setvalue(tree_board23, 4, 2, true, false, false, false, node22, null, null, node33, node11, node32, null, null);
        node31.setvalue(tree_board31, 5, 3, false, false, false, false, null, node32, node21, node41, null, null, null, null);
        node32.setvalue(tree_board32, 6, 3, false, false, false, false, node31, node33, node22, node42, node21, node41, node23, node43);
        node33.setvalue(tree_board33, 7, 3, false, false, false, false, node32, null, node23, node43, null, null, null, null);
        node41.setvalue(tree_board41, 8, 4, false, false, false, false, null, node42, node31, null, null, null, node32, node51);
        node42.setvalue(tree_board42, 9, 4, false, false, false, false, node41, node43, node32, node51, null, null, null, null);
        node43.setvalue(tree_board43, 10, 4, false, false, false, false, node42, null, node33, null, node32, node51, null, null);
        node51.setvalue(tree_board51, 11, 5, false, true, false, false, null, null, node42, null, node41, null, node43, null);

        node11.myimage.setImageResource(R.drawable.houndboard);
        node21.myimage.setImageResource(R.drawable.houndboard);
        node23.myimage.setImageResource(R.drawable.houndboard);
        node51.myimage.setImageResource(R.drawable.hareboard);

        popup_hounds_win.setVisibility(View.INVISIBLE);
        popup_hare_win.setVisibility(View.INVISIBLE);

        hound_node=node11;
        hare_node=node51;
        min_deepth=5;
        hound_min_deepth=1;
        turn_count=1;
        check_victory=0;
        check_rdata1=0;
        check_rdata2=0;
        possible_move=false;
        hound_movecheck=false;
        hare_movecheck=false;
        hare_turn=false;
        String tmp = Integer.toString(turn_count);
        count_turn.setText(tmp);
        hareturn.setVisibility(View.INVISIBLE);
        databaseReference.child("hounds").child("current").setValue(0);
        databaseReference.child("hounds").child("move").setValue(0);
        databaseReference.child("hare").child("move").setValue(0);
        databaseReference.child("result").child("hounds").setValue(0);
        databaseReference.child("result").child("hare").setValue(0);
        databaseReference.child("result").child("restart").setValue(0);
        databaseReference.child("connect").child("hare").setValue(0);
        databaseReference.child("connect").child("success").setValue(0);

        databaseReference.child("result/restart").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Object rdata = snapshot.getValue();
                check_victory=Integer.parseInt(rdata.toString());
                if(check_victory==1)
                {
                    databaseReference.child("result").child("restart").setValue(0);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        databaseReference.child("result/hounds").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Object rdata = snapshot.getValue();
                check_victory=Integer.parseInt(rdata.toString());
                if(check_victory==1)
                {
                    databaseReference.child("result").child("hounds").setValue(0);
                    hound_win();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        databaseReference.child("result/hare").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Object rdata = snapshot.getValue();
                check_victory=Integer.parseInt(rdata.toString());
                if(check_victory==1)
                {
                    databaseReference.child("result").child("hare").setValue(0);
                    hare_win();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        databaseReference.child("hounds/current").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Object rdata = snapshot.getValue();
                check_rdata1=Integer.parseInt(rdata.toString());

                if(check_rdata1 != 0)
                {
                    if(check_rdata1==1)
                    {
                        hound_node=node11;
                    }
                    if(check_rdata1==2)
                    {
                        hound_node=node21;
                    }
                    if(check_rdata1==3)
                    {
                        hound_node=node22;
                    }
                    if(check_rdata1==4)
                    {
                        hound_node=node23;
                    }
                    if(check_rdata1==5)
                    {
                        hound_node=node31;
                    }
                    if(check_rdata1==6)
                    {
                        hound_node=node32;
                    }
                    if(check_rdata1==7)
                    {
                        hound_node=node33;
                    }
                    if(check_rdata1==8)
                    {
                        hound_node=node41;
                    }
                    if(check_rdata1==9)
                    {
                        hound_node=node42;
                    }
                    if(check_rdata1==10)
                    {
                        hound_node=node43;
                    }
                    if(check_rdata1==11)
                    {
                        hound_node=node51;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        databaseReference.child("hounds/move").addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Object rdata = snapshot.getValue();
                check_rdata2=Integer.parseInt(rdata.toString());

                if(check_rdata2 != 0)
                {
                    if(check_rdata2==1)
                    {
                        node11.hound=true;
                        node11.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node11;
                    }
                    if(check_rdata2==2)
                    {
                        node21.hound=true;
                        node21.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node21;
                    }
                    if(check_rdata2==3)
                    {
                        node22.hound=true;
                        node22.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node22;
                    }
                    if(check_rdata2==4)
                    {
                        node23.hound=true;
                        node23.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node23;
                    }
                    if(check_rdata2==5)
                    {
                        node31.hound=true;
                        node31.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node31;
                    }
                    if(check_rdata2==6)
                    {
                        node32.hound=true;
                        node32.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node32;
                    }
                    if(check_rdata2==7)
                    {
                        node33.hound=true;
                        node33.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node33;
                    }
                    if(check_rdata2==8)
                    {
                        node41.hound=true;
                        node41.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node41;
                    }
                    if(check_rdata2==9)
                    {
                        node42.hound=true;
                        node42.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node42;
                    }
                    if(check_rdata2==10)
                    {
                        node43.hound=true;
                        node43.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node43;
                    }
                    if(check_rdata2==11)
                    {
                        node51.hound=true;
                        node51.myimage.setImageResource(R.drawable.houndboard);
                        hound_node.hound=false;
                        hound_node.myimage.setImageResource(R.drawable.board);
                        hound_node=node51;
                    }
                    check_rdata2=0;
                    hare_turn=true;
                    databaseReference.child("hounds").child("move").setValue(0);
                    turn_count+=1;
                    check_turn();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        home_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                databaseReference.child("result").child("restart").setValue(1);
            }
        });

        tree_board11.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node11);
                }
            }
        });

        tree_board21.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node21);
                }
            }
        });

        tree_board22.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node22);
                }
            }
        });

        tree_board23.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node23);
                }
            }
        });

        tree_board31.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node31);
                }
            }
        });

        tree_board32.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node32);
                }
            }
        });

        tree_board33.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node33);
                }
            }
        });

        tree_board41.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node41);
                }
            }
        });

        tree_board42.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node42);
                }
            }
        });

        tree_board43.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node43);
                }
            }
        });

        tree_board51.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (hare_turn) {
                    check_route(node51);
                }
            }
        });

        popup_hounds_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity2_hare.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                //finish();
                databaseReference.child("result").child("restart").setValue(1);
            }
        });

        popup_hare_win.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(PlayActivity2_hare.this,"처음화면으로 이동합니다.",Toast.LENGTH_SHORT).show();
                //finish();
                databaseReference.child("result").child("restart").setValue(1);
            }
        });
    }
}
