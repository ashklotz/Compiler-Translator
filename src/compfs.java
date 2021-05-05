/*
Author: Trey Klotz
Date: 3/8/2021
Purpose: provide a test method to run a standalone scanner
 */

import java.io.FileNotFoundException;

public class compfs {

    public static void main(String[] args) {

        String filePath = "";

        if (args.length > 0){
            filePath = args[0];
        }else{
            java.util.Scanner utilScanner = new java.util.Scanner(System.in);
            System.out.println("1: prog1\n" +
                    "2: prog2\n" +
                    "3: prog3\n" +
                    "4: prog4\n" +
                    "5: prog5\n" +
                    "6: prog6\n" +
                    "7: prog7");
            int i = utilScanner.nextInt();

            switch(i){
                case 1:
                    filePath = "testFiles/prog1";
                    break;
                case 2:
                    filePath = "testFiles/prog2";
                    break;
                case 3:
                    filePath = "testFiles/prog3";
                    break;
                case 4:
                    filePath = "testFiles/prog4";
                    break;
                case 5:
                    filePath = "testFiles/prog5";
                    break;
                case 6:
                    filePath = "testFiles/prog6";
                    break;
                case 7:
                    filePath = "testFiles/prog7";
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
