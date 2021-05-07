import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public Node first;
    public Node second;
    public Node third;
    public Node fourth;
    public String data;

    private static List<String> variables = new ArrayList<>();

    public Node(){
        first = second = third = fourth = null;
        data = "";
    }
    public Node(String s){
        first = second = third = fourth = null;
        data = s + " ";
    }

    public static boolean manageVariables(String op, Token var) {
        boolean successful;

        switch (op){
            case "add":
                successful = variables.add(var.tokenInstance);
                break;
            case "end":
                //remove variables from the currently ending block of code
                try{
                    while (variables.remove(variables.size()-1).compareTo("begin") != 0);
                    successful = true;
                }
                catch(Exception e){ successful = false; }
                break;
            case "check":
                //ensure variables have been declared before usage
                successful = variables.contains(var.tokenInstance);
                break;
            default:
                successful = false;
        }
        return successful;
    }

    //print the list of variables
    public static void printVariables(){ System.out.println(variables); }

    public static List<String> getVariables(){ return variables; }

    //print all nodes in order
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
