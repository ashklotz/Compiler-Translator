/*
Author: Trey Klotz
Date: 3/8/2021
Purpose: Practice with creation of a scanner using an FSA
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Scanner {

    /*
    * -1 = Character not allowed in token
    * -2 = Token does not begin with
    * 0 = initial state
    * 1001 = IDENTTOK
    * 1002 = DIGITTOK
    * 1003 = KEYTOK
    * 1004 = OPTOK
    * 1005 = EOFTOK
     */
    private static List buffer; //hold each character in the file
    private static int line = 1; //hold line numbers
    private static final String keywords[] ={"begin", "end", "loop", "whole", "void", "exit", "getter", "outter", "main", "if", "then", "assign", "data", "proc"}; //list of all reserved words
    private static final String legalChars = "\n =><:+-*/%.(),{};[]ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_"; //list of all possible characters in the file, corresponds with position in FSA table below
    private static final int Table[][] ={
            {0,0,4,-2,-2,4,3,3,3,3,3,3,3,3,3,3,3,3,3,3,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,-2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,1},
            {1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1001,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1001},
            {1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,1002,2,2,2,2,2,2,2,2,2,2,1002},
            {1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004},
            {1004,1004,5,5,5,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004},
            {1004,1004,6,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004},
            {1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004,1004},
    };

    public Scanner(String f) throws FileNotFoundException {
        File file = new File(f);
        java.util.Scanner scanner = new java.util.Scanner(file);
        String s = "";
        buffer = new ArrayList<String>();

        //create string to do some pre-processing in from the entire file
        while (scanner.hasNextLine()){
            s += scanner.nextLine();
            s += " \n";
        }

        //remove comments
        s = s.replaceAll("\\$\\$((\\s|.)*?)\\$\\$", "");

        //append all characters to buffer list
        for (int i = 0; i < s.length(); i++) {
            buffer.add(s.charAt(i));
        }
    }

    public static Token Scan(){

        int state = 0;
        int nextState = 0;
        Token token = new Token();
        String nextChar = " ";
        String s = "";

        while (state < 1000 && state >= 0){ //while state isn't an error or final

            if (getNextChar() == "EOF") {
                //found EOF, get tf out
                token = setTokenFinalState(1005, "EOF", line);
                return token;
            }

            try {
                nextState = Table[state][legalChars.indexOf(nextChar)];
            }
            catch (ArrayIndexOutOfBoundsException e){
                //out of bounds of legalchars, not a legal char
                //e.printStackTrace();
                nextState = -1;
            }

            if (nextState < 0) {//if error state
                token.tokenID = setError(nextState, nextChar, line);
                state = nextState;
                buffer.add(0,nextChar);
            }

            else if (nextState > 1000) {//if final state
                token = setTokenFinalState(nextState, s, line);
                state = nextState;

                if (!Character.isWhitespace(nextChar.charAt(0))) // this stupid if statement took 4 hours of debugging to add in
                    buffer.add(0, nextChar);
            }

            else{ //not final or error state
                state = nextState;
                s += nextChar;
                s = s.trim();
                nextChar = getNextChar();
                if (legalChars.indexOf(getNextChar()) == 0)
                    line++; //nextChar is '\n', line should be incremented
                buffer.remove(0);
            }
        }
        return token;
    }

    private static Token setTokenFinalState(int state, String s, int l) {
        Token t = new Token();
        t.tokenInstance = s;
        t.lineNum = l;

        switch (state) {
            case 1001:
                for (int i = 0; i < keywords.length; i++) {
                    if (keywords[i].compareTo(s) == 0)
                        t.tokenID = TokenID.KEYTOK;
                }
                break;
            case 1002:
                t.tokenID = TokenID.DIGITTOK;
                break;
            case 1004:
                t.tokenID = TokenID.OPTOK;
                break;
            case 1005:
                t.tokenID = TokenID.EOFTOK;
        }
        return t;
    }

    private static TokenID setError(int i, String c, int line) {

        if (i == -1)
            System.out.println("ERROR: no token contains \"" + c + "\"" + " line number: " + line);
        if (i == -2)
            System.out.println("ERROR: no token begins with \"" + c + "\"" + " line number: " + line);

        return TokenID.EOFTOK;
    }

    private static String getNextChar() {
        if (buffer.isEmpty()) return "EOF";
        return buffer.get(0).toString();
    }
}
