package regextodfa;

import java.util.*;

public class RegexToDfa {

    private static Set<Integer>[] followPos;
    private static Node root;
    private static ArrayList<State> DStates;
    private static Set<String> input; // set of characters is used in input regex

    private static Set<Integer>[] followPos2;
    private static Node root2;
    private static ArrayList<State> DStates2;
    private static Set<String> input2;

    private static HashMap<Integer, String> symbNum;

    private static HashMap<Integer, String> symbNum2;

    public static void main(String[] args) {
        initialize();
    }

    public static void initialize() {
        Scanner in = new Scanner(System.in);
        // allocating
        DStates = new ArrayList<State>();
        input = new HashSet<String>();

        DStates2 = new ArrayList<State>();
        input2 = new HashSet<String>();

        String regex = getRegex(in);
        String regex2 = getRegex(in);

        getSymbols(regex);
        getSymbols2(regex2);

        /**
         * giving the regex to SyntaxTree class constructor and creating the
         * syntax tree of the regular expression in it
         */
        SyntaxTree st = new SyntaxTree(regex);
        SyntaxTree st2 = new SyntaxTree(regex2);

        root = st.getRoot(); // root of the syntax tree
        root2 = st2.getRoot(); // root of the syntax tree

        // st2.printSyntaxTree(st2.getRoot());
        // System.out.println(" ");
        followPos = st.getFollowPos(); // the followpos of the syntax tree
        followPos2 = st2.getFollowPos(); // the followpos of the syntax tree

        State q0 = createDFA();
        State p0 = createDFA2();

        DfaTraversal dfat = new DfaTraversal(q0, input);
        DfaTraversal dfat2 = new DfaTraversal(p0, input2);

        q0.printDFA();
        p0.printDFA();

     
        ArrayList<String> keymoves = new ArrayList<>();
        HashMap<String, State> moves = q0.getAllMoves();
        for (Map.Entry<String, State> entry : moves.entrySet()) {
            keymoves.add(entry.getKey());
        }
        State[][] table = new State[10][10];
        State[][] table2 = new State[10][10];
        table[0][0]=q0;
        table[0][1]=p0;
        int j=1;
        int h=1;
        int o=0;
        while (true){
            for (int i = 0; i < keymoves.size(); i++) {
                State one =table[o][0].getNextStateBySymbol(keymoves.get(i));
                State two =table[o][1].getNextStateBySymbol(keymoves.get(i));
                if(check(table,j,one,two)){
                    table[h][0]=one;
                    table[h][1]=two;
                    h++;
                    j++;
                }
            }
            o++;
            if(table[o][0]==null){
                break;
            }
        }
        if(check_equal(table,j)){
            System.out.println("two language is equal!!");
        }else{
            System.out.println("two language is not equal!!");
        }
        for (int i = 0; i < j; i++) {
            System.out.println("q"+table[i][0].getID()+", "+"p"+table[i][1].getID());
        }
        }
        public static boolean check_equal(State[][] x,int j){
            for (int i = 0; i < j; i++) {
                if(x[i][0].getIsAcceptable() && !x[i][1].getIsAcceptable()) {
                    return false ;
                }else if(!x[i][0].getIsAcceptable() && x[i][1].getIsAcceptable()){
                    return false ;
                }
            }
            return true;
        }
        public static boolean check(State[][] x, int j, State one, State two){
            for (int i = 0; i < j; i++) {
                if(x[i][0]==one && x[i][1]==two) {
                    return false ;
                }
            }
            return true;
        }
       
       //  String str = getStr(in);
        // boolean acc = false;
        // for (char c : str.toCharArray()) {
        // if (dfat.setCharacter(c)) {
        // acc = dfat.traverse();
        // } else {
        // System.out.println("WRONG CHARACTER!");
        // System.exit(0);
        // }
        // }
        // if (acc) {
        // System.out.println((char) 27 + "[32m" + "this string is acceptable by the        regex!");
        // } else {
        // System.out.println((char) 27 + "[31m" + "this string is not acceptable by the        regex!");
        // }
        // in.close();
    //}

    private static String getRegex(Scanner in) {
        System.out.print("Enter a regex: ");
        String regex = in.nextLine();
        // return regex;
        return regex +"#";
    }

    private static void getSymbols(String regex) {
        /**
         * op is a set of characters have operational meaning for example '*'
         * could be a closure operator
         */
        Set<Character> op = new HashSet<>();
        Character[] ch = { '(', ')', '?', '*', '|', '&', '.', '\\', '[', ']', '+' };
        op.addAll(Arrays.asList(ch));

        input = new HashSet<>();
        symbNum = new HashMap<>();
        int num = 1;
        for (int i = 0; i < regex.length(); i++) {
            char charAt = regex.charAt(i);

            /**
             * if a character which is also an operator, is followed up by
             * backslash ('\'), then we should consider it as a normal character
             * and not an operator
             */
            if (op.contains(charAt)) {
                if (i - 1 >= 0 && regex.charAt(i - 1) == '\\') {
                    input.add("\\" + charAt);
                    symbNum.put(num++, "\\" + charAt);
                }
            } else {
                input.add("" + charAt);
                symbNum.put(num++, "" + charAt);
            }
        }
    }

    private static void getSymbols2(String regex) {
        /**
         * op is a set of characters have operational meaning for example '*'
         * could be a closure operator
         */
        Set<Character> op = new HashSet<>();
        Character[] ch = { '(', ')', '?', '*', '|', '&', '.', '\\', '[', ']', '+' };
        op.addAll(Arrays.asList(ch));

        input2 = new HashSet<>();
        symbNum2 = new HashMap<>();
        int num = 1;
        for (int i = 0; i < regex.length(); i++) {
            char charAt = regex.charAt(i);

            /**
             * if a character which is also an operator, is followed up by
             * backslash ('\'), then we should consider it as a normal character
             * and not an operator
             */
            if (op.contains(charAt)) {
                if (i - 1 >= 0 && regex.charAt(i - 1) == '\\') {
                    input2.add("\\" + charAt);
                    symbNum2.put(num++, "\\" + charAt);
                }
            } else {
                input2.add("" + charAt);
                symbNum2.put(num++, "" + charAt);
            }
        }
    }

    private static State createDFA() {
        int id = 0;
        Set<Integer> firstpos_n0 = root.getFirstPos();

        State q0 = new State(id++);
        q0.addAllToName(firstpos_n0);
        if (q0.getName().contains(followPos.length)) {
            q0.setAccept();
        }
        DStates.clear();
        DStates.add(q0);

        while (true) {
            boolean exit = true;
            State s = null;
            for (State state : DStates) {
                if (!state.getIsMarked()) {
                    exit = false;
                    s = state;
                }
            }
            if (exit) {
                break;
            }

            if (s.getIsMarked()) {
                continue;
            }
            s.setIsMarked(true); // mark the state
            Set<Integer> name = s.getName();
            for (String a : input) {
                Set<Integer> U = new HashSet<>();
                for (int p : name) {
                    if (symbNum.get(p).equals(a)) {
                        U.addAll(followPos[p - 1]);
                    }
                }

                boolean flag = false;
                State tmp = null;
                for (State state : DStates) {
                    if (state.getName().equals(U)) {
                        tmp = state;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    State q = new State(id++);
                    q.addAllToName(U);
                    if (U.contains(followPos.length)) {
                        q.setAccept();
                    }
                    DStates.add(q);
                    tmp = q;
                }
                s.addMove(a, tmp);
            }
        }

        return q0;
    }

    private static State createDFA2() {
        int id = 0;
        Set<Integer> firstpos_n0 = root2.getFirstPos();

        State q0 = new State(id++);
        q0.addAllToName(firstpos_n0);
        if (q0.getName().contains(followPos2.length)) {
            q0.setAccept();
        }
        DStates2.clear();
        DStates2.add(q0);

        while (true) {
            boolean exit = true;
            State s = null;
            for (State state : DStates2) {
                if (!state.getIsMarked()) {
                    exit = false;
                    s = state;
                }
            }
            if (exit) {
                break;
            }

            if (s.getIsMarked()) {
                continue;
            }
            s.setIsMarked(true); // mark the state
            Set<Integer> name = s.getName();
            for (String a : input2) {
                Set<Integer> U = new HashSet<>();
                for (int p : name) {
                    if (symbNum2.get(p).equals(a)) {
                        U.addAll(followPos2[p - 1]);
                    }
                }

                boolean flag = false;
                State tmp = null;
                for (State state : DStates2) {
                    if (state.getName().equals(U)) {
                        tmp = state;
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    State q = new State(id++);
                    q.addAllToName(U);
                    if (U.contains(followPos2.length)) {
                        q.setAccept();
                    }
                    DStates2.add(q);
                    tmp = q;
                }
                s.addMove(a, tmp);
            }
        }

        return q0;
    }

    private static String getStr(Scanner in) {
        System.out.print("Enter a string: ");
        String str;
        str = in.nextLine();
        return str;
    }

    /*public static String[][] convertStatesToArray(State q0) {
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(q0);

        // Collect all unique symbols used in transitions
        Set<String> symbols = new HashSet<>();

        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!visited.contains(state)) {
                visited.add(state);
                HashMap<String, State> moves = state.getAllMoves();
                for (String symbol : moves.keySet()) {
                    symbols.add(symbol);
                }
                queue.addAll(moves.values());
            }
        }

        // Convert symbols set to an array
        String[] alphabet = symbols.toArray(new String[0]);
        Arrays.sort(alphabet);

        // Create the transitions array
        int numStates = visited.size();
        int numSymbols = alphabet.length;
        String[][] transitions = new String[numStates][numSymbols];

        // Initialize the transitions array
        visited.clear();
        queue.add(q0);
        visited.add(q0);

        // Process each state and its transitions
        while (!queue.isEmpty()) {
            State state = queue.poll();
            int currentStateIndex = state.getID();

            HashMap<String, State> moves = state.getAllMoves();
            for (Map.Entry<String, State> entry : moves.entrySet()) {
                String symbol = entry.getKey();
                State nextState = entry.getValue();
                int symbolIndex = Arrays.binarySearch(alphabet, symbol);
                int nextStateIndex = nextState.getID();
                transitions[currentStateIndex][symbolIndex] = "q" + nextStateIndex;
                if (!visited.contains(nextState)) {
                    visited.add(nextState);
                    queue.add(nextState);
                }

            }

        }

        // Print the transitions array
        // System.out.println("Transitions:");
        // for (int i = 0; i < numStates; i++) {
        // for (int j = 0; j < numSymbols; j++) {
        // String transition = transitions[i][j] != null ? transitions[i][j] : "-";
        // System.out.printf("q%d -> %s: %s\n", i, alphabet[j], transition);
        // }
        // }

        String[][] tr = new String[numStates][numSymbols - 1];
        for (int i = 0; i < numStates; i++) {
            for (int j = 1; j < numSymbols; j++) {
                tr[i][j - 1] = transitions[i][j];
            }
        }

        return tr;

    }*/

}
