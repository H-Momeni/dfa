/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regextodfa;

import java.util.*;

/**
 *
 * @author @ALIREZA_KAY
 */
public class State {

    private int ID;
    private Set<Integer> name;
    private HashMap<String, State> move;

    private boolean IsAcceptable;
    private boolean IsMarked;

    public State(int ID) {
        this.ID = ID;
        move = new HashMap<>();
        name = new HashSet<>();
        IsAcceptable = false;
        IsMarked = false;
    }

    public void addMove(String symbol, State s) {
        move.put(symbol, s);
    }

    public void addToName(int number) {
        name.add(number);
    }

    public void addAllToName(Set<Integer> number) {
        name.addAll(number);
    }

    public void setIsMarked(boolean bool) {
        IsMarked = bool;
    }

    public boolean getIsMarked() {
        return IsMarked;
    }

    public Set<Integer> getName() {
        return name;
    }

    public void setAccept() {
        IsAcceptable = true;
    }

    public boolean getIsAcceptable() {
        return IsAcceptable;
    }

    public State getNextStateBySymbol(String str) {
        return this.move.get(str);
    }

    public HashMap<String, State> getAllMoves() {
        return move;
    }

    // Other methods

    public int getID() {
        return ID;
    }

    public String[] getAccept() {
        List<String> list = new LinkedList<String>();
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!visited.contains(state)) {
                visited.add(state);
                if (state.getIsAcceptable()) {
                    String str = "q" + state.getID();
                    list.add(str);
                }

                HashMap<String, State> moves = state.getAllMoves();
                for (Map.Entry<String, State> entry : moves.entrySet()) {
                    State nextState = entry.getValue();
                    queue.add(nextState);
                }

            }
        }
        String[] arr = list.toArray(new String[0]);
        return arr;
    }

    public void equalRegex(String[] accept1, String[] accept2, String[][] move1, String[][] move2) {//halati ke namad barabar vali namosavi darand mone masalan ab cd
        int symbol = move2[0].length;
        int symbol2=move1[0].length;
        List<String> acc1 = Arrays.asList(accept1);
        List<String> acc2 = Arrays.asList(accept2);
        List<List<String>> bigList = new ArrayList<>();
        

        if(symbol!=symbol2)
            System.out.println("not equal!");
        else
            System.out.println(" equal!");
            
        for (int i = 0; i < symbol; i++){
            List<String> sublist = new ArrayList<>();

        }
        


        // for (int i = 0; i < symbol; i++) {

        //     if ((acc1.contains(move1[0][i]) && !acc2.contains(move2[0][i]))
        //             || (!acc1.contains(move1[0][i]) && acc2.contains(move2[0][i]))) {
        //         System.out.println("not equal!");
        //     } else {
        //         store.add(move1[0][i]);
        //         store.add(move2[0][i]);
        //     }
        //     loop: while (true) {
        //         if ((acc1.contains(store.get(0)) && !acc2.contains(store.get(1)))
        //                 || (!acc1.contains(store.get(0)) && acc2.contains(store.get(1)))) {
        //             System.out.println("not equal!");
        //         }
        //     }

        // }

    }

    public void printDFA() {
        Set<State> visited = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(this);

        System.out.println("DFA:");
        while (!queue.isEmpty()) {
            State state = queue.poll();
            if (!visited.contains(state)) {
                visited.add(state);
                System.out.print("State " + state.getID() + ": ");
                if (state.getIsAcceptable()) {
                    System.out.print("(Accepting State) ");
                }
                System.out.print("Transitions: ");

                HashMap<String, State> moves = state.getAllMoves();
                for (Map.Entry<String, State> entry : moves.entrySet()) {
                    String symbol = entry.getKey();
                    State nextState = entry.getValue();
                    System.out.print("(" + symbol + " -> State " + nextState.getID() + ") ");
                    queue.add(nextState);
                }

                System.out.println();
            }
        }
    }

}
