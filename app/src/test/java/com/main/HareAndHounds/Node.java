package com.main.HareAndHounds;

public class Node
{
    int data;
    int deepth;
    public Node up;
    public Node down;
    public Node left;
    public Node right;
    public Node upleft;
    public Node upright;
    public Node downleft;
    public Node downright;

    Node()
    {
        int data=0;
        int deepth=0;
        Node up = null;
        Node down = null;
        Node left = null;
        Node right = null;
        Node upleft = null;
        Node upright = null;
        Node downleft = null;
        Node downright = null;
    }

    void setvalue(int data, int deepth, Node up, Node down, Node left, Node right, Node upleft, Node upright, Node downleft, Node downright)
    {
        this.data = data;
        this.deepth = deepth;
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
