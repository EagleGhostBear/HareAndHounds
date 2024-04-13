package com.main.HareAndHounds;

public class testprint
{
    public static void main(String[] args)
    {
        /*Treemgt totalnode = new Treemgt();
        Node node11 = totalnode.createNode();
        Node node21 = totalnode.createNode();
        Node node22 = totalnode.createNode();
        Node node23 = totalnode.createNode();
        Node node31 = totalnode.createNode();
        Node node32 = totalnode.createNode();
        Node node33 = totalnode.createNode();
        Node node41 = totalnode.createNode();
        Node node42 = totalnode.createNode();
        Node node43 = totalnode.createNode();
        Node node51 = totalnode.createNode();*/

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

        node11.setvalue(1, 1, null, null, null, node22, null, node21, null, node23);
        node21.setvalue(2, 2, null, node22, null, node31, null, null, node11, node32);
        node22.setvalue(3, 2, node21, node23, node11, node32, null, null, null, null);
        node23.setvalue(4, 2, node22, null, null, node33, node11, node32, null, null);
        node31.setvalue(5, 3, null, node32, node21, node41, null, null, null, null);
        node32.setvalue(6, 3, node31, node33, node22, node42, node21, node41, node23, node43);
        node33.setvalue(7, 3, node32, null, node23, node43, null, null, null, null);
        node41.setvalue(8, 4, null, node42, node31, null, null, null, node32, node51);
        node42.setvalue(9, 4, node41, node43, node32, node51, null, null, null, null);
        node43.setvalue(10, 4, node42, null, node33, null, node32, node51, null, null);
        node51.setvalue(11, 5, null, null, node42, null, node41, null, node43, null);

        System.out.println("출력 값: "+node32.right.data);
    }
}
