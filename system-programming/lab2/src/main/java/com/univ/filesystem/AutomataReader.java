package com.univ.filesystem;

import com.univ.automata.Automata;
import com.univ.state.Transition;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class AutomataReader {

    public static Automata read(Path path) {
        Automata automata = new Automata();

        try (Scanner sc = new Scanner(path.toFile())) {
            automata.setSymbolsNum(sc.nextInt());
            int statesNum = sc.nextInt();
            automata.setStatesNum(statesNum);
            automata.setStartState(sc.nextInt());
            int finalStatesNum = sc.nextInt();
            List<Integer> finalStates = new ArrayList<>();
            for (int i = 0; i < finalStatesNum; i++) {
                finalStates.add(sc.nextInt());
            }
            automata.setFinalStates(finalStates);
            for (int i = 0; i < statesNum; i++) {
                int from = sc.nextInt();
                char letter = sc.next(".").charAt(0);
                int to = sc.nextInt();
                Transition transition = Transition.builder()
                        .to(to).letter(letter).build();
                List<Integer> destinations = automata.getTransitions().get(transition);
                if (destinations == null) {
                    List<Integer> statesList = new ArrayList<>();
                    statesList.add(from);
                    automata.getTransitions().put(transition, statesList);
                } else {
                    automata.getTransitions().get(transition).add(from);
                }
                List<Integer> graphEdges = automata.getGraph().get(from);
                if (graphEdges == null) {
                    List<Integer> statesList = new ArrayList<>();
                    statesList.add(to);
                    automata.getGraph().put(from, statesList);
                } else {
                    automata.getGraph().get(from).add(to);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not read automata", e);
        }
        return automata;

    }

}
