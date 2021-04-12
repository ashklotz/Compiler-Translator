/*
Author: Trey Klotz
Date: 3/8/2021
Purpose: Data types to be used with scanner
 */

enum TokenID{
    IDENTTOK, //identifier
    DIGITTOK, //digit
    KEYTOK, //keyword
    OPTOK, //operator
    EOFTOK //End of File
    }

public class Token {
    TokenID tokenID;
    String tokenInstance;
    int lineNum;

    Token(){
        tokenID = TokenID.IDENTTOK;
        tokenInstance = "";
        lineNum = 0;
    }

    Token(TokenID tkID, String tkInst, int line){
        tokenID = tkID;
        tokenInstance = tkInst;
        lineNum = line;
    }

    @Override
    public String toString() {
        return "{ " +
                "tokenID= " + tokenID +
                ", tokenInstance= '" + tokenInstance + '\'' +
                ", lineNum= " + lineNum +
                " }";
    }
}
