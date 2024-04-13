package com.main.HareAndHounds.functions;

import android.widget.ImageView;

public class Node
{
    public ImageView myimage;
    public int boardnum;
    public int deepth;
    public boolean hound;
    public boolean hare;
    public boolean select;
    public boolean connect;
    public Node up;
    public Node down;
    public Node left;
    public Node right;
    public Node upleft;
    public Node upright;
    public Node downleft;
    public Node downright;

    public Node()
    {
        ImageView myimage = null;

        int boardnum=0;
        int deepth=0;

        boolean hound = false;
        boolean hare = false;
        boolean select = false;
        boolean connect = false;

        Node up = null;
        Node down = null;
        Node left = null;
        Node right = null;
        Node upleft = null;
        Node upright = null;
        Node downleft = null;
        Node downright = null;
    }

    public void setvalue(ImageView myimage, int boardnum, int deepth, boolean hound, boolean hare, boolean select, boolean connect, Node up, Node down, Node left, Node right, Node upleft, Node upright, Node downleft, Node downright)
    {
        this.myimage = myimage;

        this.boardnum = boardnum;
        this.deepth = deepth;

        this.hound = hound;
        this.hare = hare;
        this.select = select;
        this.connect = connect;

        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.upleft = upleft;
        this.upright = upright;
        this.downleft = downleft;
        this.downright = downright;
    }
}