/*
Author: Trey Klotz
Date: 3/8/2021
Purpose: provide a test method to run a standalone scanner
 */

import java.io.FileNotFoundException;

public class TestDriver {

    public static void main(String[] args) {

        String filePath = "";

        if (args.length > 0){
            filePath = args[0];
        }else{
            java.util.Scanner utilScanner = new java.util.Scanner(System.in);
            System.out.println("1: p2g1\n" +
                    "2: p2g2\n" +
                    "3: p2g3\n" +
                    "4: p2g4\n" +
                    "5: p2g5\n" +
                    "6: p2g6\n" +
                    "7: p2g7Jared");
            int i = utilScanner.nextInt();

            switch(i){
                case 1:
                    filePath = "src/p2g1";
                    break;
                case 2:
                    filePath = "src/p2g2";
                    break;
                case 3:
                    filePath = "src/p2g3";
                    break;
                case 4:
                    filePath = "src/p2g4";
                    break;
                case 5:
                    filePath = "src/p2g5";
                    break;
                case 6:
                    filePath = "src/p2g6";
                    break;
                case 7:
                    filePath = "src/p2g7Jared";
                    break;
            }
        }

        //make sure parser is created and file is read correctly
        Parser parser;
        try { parser = new Parser(filePath); }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        //catch any parse errors
        try { Node.printInorder(parser.parse(), 0); }
        catch(Exception e){ e.printStackTrace(); }

        Node.printVariables();
    }
}
