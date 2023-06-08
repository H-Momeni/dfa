/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regextodfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
    
    public State(int ID){
        this.ID = ID;
        move = new HashMap<>();
        name = new HashSet<>();
        IsAcceptable = false;
        IsMarked = false;
    }
    
    public void addMove(String symbol, State s){
        move.put(symbol, s);
    }
    
    public void addToName(int number){
        name.add(number);
    }
    public void addAllToName(Set<Integer> number){
        name.addAll(number);
    }
    
    public void setIsMarked(boolean bool){
        IsMarked = bool;
    }
    
    public boolean getIsMarked(){
        return IsMarked;
    }
    
    public Set<Integer> getName(){
        return name;
    }

    public void setAccept() {
        IsAcceptable = true;
    }
    
    public boolean getIsAcceptable(){
        return  IsAcceptable;
    }
    
    public State getNextStateBySymbol(String str){
        return this.move.get(str);
    }
    
    public HashMap<String, State> getAllMoves(){
        return move;
    }


    
    
    // Other methods
    
    public int getID() {
        return ID;
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
