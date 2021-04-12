import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Parser {
    static ArrayList<Token> tokenList;
    private static final int current = 0;

    public Parser(String f) throws FileNotFoundException {
        Token t = new Token();
        Scanner scanner = new Scanner(f);
        tokenList = new ArrayList<>();

        //read all tokens into a list
        while (t.tokenID != TokenID.EOFTOK){
            t = scanner.Scan();
            tokenList.add(t);
        }

        //print all token data
        //for (int i = 0; i < tokenList.size(); i++) { System.out.println(tokenList.get(i)); }
    }

    public static Node parse() throws Exception{
        Node r = new Node();
        if (tokenList.get(current).tokenInstance.compareTo("main") == 0 || tokenList.get(current).tokenInstance.compareTo("data") == 0)
            r = Program();
        else throwException();

        return r;
    }

    private static void throwException() throws Exception{
        //TODO: probably some more backend stuff to make exception easier to understand for user
        throw new Exception("Parse Error at: " + tokenList.get(current));
    }

    private static Node Program() throws Exception{
        Node r = new Node("<program>");
        r.first = Vars();

        if (tokenList.get(current).tokenInstance.compareTo("main") == 0) {
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();

        r.second = Block();

        if (tokenList.get(current).tokenID == TokenID.EOFTOK && tokenList.size() == 1){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();

        return r;
    }

    private static Node Block() throws Exception{
        Node r = new Node("<block>");
        if (tokenList.get(current).tokenInstance.compareTo("begin") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();

        r.first = Vars();
        r.second = Stats();

        if (tokenList.get(current).tokenInstance.compareTo("end") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();

        return r;
    }

    private static Node Vars() throws Exception{
        Node r = new Node("<vars>");
        if (tokenList.get(current).tokenInstance.compareTo("data") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
                r.data += tokenList.get(current);
                tokenList.remove(current);

                if (tokenList.get(current).tokenInstance.compareTo(":=") == 0){
                    r.data += tokenList.get(current);
                    tokenList.remove(current);

                    if (tokenList.get(current).tokenID == TokenID.DIGITTOK){
                        r.data += tokenList.get(current);
                        tokenList.remove(current);
                        if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                            r.data += tokenList.get(current);
                            tokenList.remove(current);

                            r.first = Vars();
                        } else throwException();
                    } else throwException();
                } else throwException();
            } else throwException();
        } else r = null;
        return r;
    }

    private static Node Expr() throws Exception {
        Node r = new Node("<expr>");

        r.first = N();

        if (tokenList.get(current).tokenInstance.compareTo("-") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            r.second = Expr();
        }
        return r;
    }

    private static Node N() throws Exception {
        Node r = new Node("<N>");
        r.first = A();

        if (tokenList.get(current).tokenInstance.compareTo("/") == 0 || tokenList.get(current).tokenInstance.compareTo("*") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            r.second = N();
        }
        return r;
    }

    private static Node A() throws Exception {
        Node r = new Node("<A>");
        r.first = M();

        if (tokenList.get(current).tokenInstance.compareTo("+") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            r.second = A();
        }
        return r;
    }

    private static Node M() throws Exception {
        Node r = new Node("<M>");

        if (tokenList.get(current).tokenInstance.compareTo("*") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            r.first = M();
        }
        else r.first = R();

        return r;
    }

    private static Node R() throws Exception {
        Node r = new Node("<R>");

        if (tokenList.get(current).tokenInstance.compareTo("(") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            r.first = Expr();

            if (tokenList.get(current).tokenInstance.compareTo(")") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        }
        else if (tokenList.get(current).tokenID == TokenID.DIGITTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();

        return r;
    }

    private static Node Stats() throws Exception {
        Node r = new Node("<stats>");
        r.first = Stat();
        r.second = mStat();
        return r;
    }

    private static Node mStat() throws Exception {
        Node r = new Node("<mStat>");
        if (tokenList.get(current).tokenInstance.compareTo("getter") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("outter") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("begin") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("if") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("loop") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("assign") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("proc") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("Node") == 0){
            r.first = Stat();
            r.second = mStat();
        } else r = null;
        return r;
    }

    private static Node Stat() throws Exception {
        Node r = new Node("<stat>");
        if (tokenList.get(current).tokenInstance.compareTo("getter") == 0){
            r.first = In();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("outter") == 0){
            r.first = Out();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("begin") == 0){
            r.first = Block();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("if") == 0){
            r.first = If();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("loop") == 0){
            r.first = Loop();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("assign") == 0){
            r.first = Assign();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("proc") == 0){
            r.first = Goto();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        }
        else if (tokenList.get(current).tokenInstance.compareTo("void") == 0){
            r.first = Label();
            if (tokenList.get(current).tokenInstance.compareTo(";") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
            } else throwException();
        } else throwException();
        return r;
    }

    private static Node In() throws Exception {
        Node r = new Node("<in>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();
        return r;
    }

    private static Node Out() throws Exception {
        Node r = new Node("<out>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        r.first = Expr();

        return r;
    }

    private static Node If() throws Exception {
        Node r = new Node("<if>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenInstance.compareTo("[") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);
            r.first = Expr();
            r.second = RO();
            r.third = Expr();

            if (tokenList.get(current).tokenInstance.compareTo("]") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);

                if (tokenList.get(current).tokenInstance.compareTo("then") == 0){
                    r.data += tokenList.get(current);
                    tokenList.remove(current);

                    r.fourth = Stat();
                } else throwException();
            } else throwException();
        } else throwException();
        return r;
    }

    private static Node Loop() throws Exception {
        Node r = new Node("<loop>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenInstance.compareTo("[") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);
            r.first = Expr();
            r.second = RO();
            r.third = Expr();

            if (tokenList.get(current).tokenInstance.compareTo("]") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);
                r.fourth = Stat();
            } else throwException();
        } else throwException();
        return r;
    }

    private static Node Assign() throws Exception {
        Node r = new Node("<assign>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            if (tokenList.get(current).tokenInstance.compareTo(":=") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);

                r.first = Expr();
            } else throwException();
        } else throwException();
        return r;
    }

    private static Node RO() throws Exception {
        Node r = new Node("<RO>");

        if (tokenList.get(current).tokenInstance.compareTo("=>") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("=<") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("==") == 0 ||
                tokenList.get(current).tokenInstance.compareTo("%") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        }
        else if (tokenList.get(current).tokenInstance.compareTo("[") == 0){
            r.data += tokenList.get(current);
            tokenList.remove(current);

            if (tokenList.get(current).tokenInstance.compareTo("==") == 0){
                r.data += tokenList.get(current);
                tokenList.remove(current);

                if (tokenList.get(current).tokenInstance.compareTo("]") == 0){
                    r.data += tokenList.get(current);
                    tokenList.remove(current);

                    /*
                    Let it be known, that my reading comprehension is so bad that I thought in the grammar
                    where it said '[==] (three tokens)', that I was supposed to just consume three tokens,
                    so thats what I did. I spent three stupid hours going back and forth at 1AM trying to fix this.

                    if (tokenList.get(current).tokenID != TokenID.EOFTOK){
                        System.out.println("<RO> " + tokenList.get(current));
                        tokenList.remove(current);

                        if (tokenList.get(current).tokenID != TokenID.EOFTOK){
                            System.out.println("<RO> " + tokenList.get(current));
                            tokenList.remove(current);

                            if (tokenList.get(current).tokenID != TokenID.EOFTOK){
                                System.out.println("<RO> " + tokenList.get(current));
                                tokenList.remove(current);
                            } else throwException();
                        } else throwException();
                    } else throwException();
                    */
                } else throwException();
            } else throwException();
        }else throwException();
        return r;
    }

    private static Node Label() throws Exception {
        Node r = new Node("<label>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();
        return r;
    }

    private static Node Goto() throws Exception {
        Node r = new Node("<goto>");

        r.data += tokenList.get(current);
        tokenList.remove(current);

        if (tokenList.get(current).tokenID == TokenID.IDENTTOK){
            r.data += tokenList.get(current);
            tokenList.remove(current);
        } else throwException();
        return r;
    }
}
