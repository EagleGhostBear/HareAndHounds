package com.main.HareAndHounds.functions;

import android.widget.ImageView;

public class Node implements Comparable<Node> {
    // 내가 몇번 보드인지 ex) 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    public int id;

    // 보드의 이미지
    public ImageView img;

    // 보드가 몇번째 라인인지, Hounds는 뒤로 갈 수 없기 때문에 이를 파악하기 위해 사용
    public int depth;

    public Node(int id, ImageView img) {
        this.id = id;
        this.img = img;
        if(id < 1){
            depth = 0;
        }
        else if (id < 4) {
            depth = 1;
        } else if (id < 7) {
            depth = 2;
        } else if (id < 10) {
            depth = 3;
        } else {
            depth = 4;
        }
    }

    @Override
    public int compareTo(Node o) {
        int depthCompare = Integer.compare(this.depth, o.depth);
        if (depthCompare == 0) {
            return Integer.compare(this.id, o.id);
        }
        return depthCompare;
    }
}