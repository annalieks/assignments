package com.univ.automata;

import com.univ.state.Transition;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class Automata {

    private int statesNum;
    private int symbolsNum;
    private int startState;
    private List<Integer> finalStates;
    private Map<Transition, List<Integer>> transitions = new HashMap<>();
    private Map<Integer, List<Integer>> graph = new HashMap<>();
    private Set<Integer> reachableStates = new HashSet<>();

    public boolean isValid(String word) {
        for (int finalState : finalStates) {
            if (iterate(word, finalState)) {
                return true;
            }
        }
        return false;
    }

    private boolean iterate(String word, int currentState) {
        if (word.length() == 0) {
            return checkWayFromStartState(currentState);
        }
        Transition transition = Transition.builder()
                .to(currentState)
                .letter(word.charAt(word.length() - 1))
                .build();

        if (transitions.get(transition) == null) {
            return false;
        }
        for (Integer from : transitions.get(transition)) {
            if (iterate(word.substring(0, word.length() - 1), from)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWayFromStartState(int state) {
        return reachableStates.contains(state);
    }

    public void determineReachableStates() {
        reachableStates.add(startState);
        List<Integer> states = graph.get(startState);
        dfs(startState, states);
    }

    private void dfs(int currentState, List<Integer> availableStates) {
        reachableStates.add(currentState);
        if (availableStates == null) {
            return;
        }
        for (int state : availableStates) {
            dfs(state, graph.get(state));
        }
    }

}
