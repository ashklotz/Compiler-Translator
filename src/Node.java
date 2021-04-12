public class Node {
    public Node first;
    public Node second;
    public Node third;
    public Node fourth;
    public String data;

    public Node(){
        first = second = third = fourth = null;
        data = "";
    }
    public Node(String s){
        first = second = third = fourth = null;
        data = s + " ";
    }

    public static void printInorder(Node r, int indent){
        for (int i = 0; i < indent; i++) {
            System.out.print("  ");
        }
        System.out.println(r.data);
        if (r.first != null) printInorder(r.first, indent+1);
        if (r.second != null) printInorder(r.second, indent+1);
        if (r.third != null) printInorder(r.third, indent+1);
        if (r.fourth != null) printInorder(r.fourth, indent+1);
    }
}
