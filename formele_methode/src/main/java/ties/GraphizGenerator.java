package ties;

import ties.Machine;
import static guru.nidi.graphviz.model.Factory.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import guru.nidi.graphviz.attribute.Attributes;
import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.ForLink;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class GraphizGenerator<T extends Comparable<T>> {

    private Machine<T> m;

    GraphizGenerator(Machine<T> m) {
        this.m = m;
    }

    public Graph GenerateGraphizContent() {

        ArrayList<Node> nodes = createNodes(this.m);

        Graph g = graph("example1").directed().graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT)).nodeAttr()
                .with(Font.name("Arial")).linkAttr().with("class", "link-class").with(nodes);

        return g;
    }

    public void GenerateImage(String imageName) {
        try {
            Graphviz.fromGraph(GenerateGraphizContent()).height(500).render(Format.PNG)
                    .toFile(new File("example/" + imageName + ".png"));
        } catch (IOException ex) {
        }
    }

    private ArrayList<Node> createNodes(Machine<T> machine) {
        ArrayList<Node> nodes = new ArrayList<>();
        HashMap<T, Node> nodeMap = new HashMap<>();

        for (T state : machine.getStates()) {
            Node n = node(state.toString()).with(Shape.CIRCLE);
            if (machine.beginStates.contains(state)) {
                n = n.with(Color.LIGHTSEAGREEN);
            }
            if (machine.endStates.contains(state)) {
                n = n.with(Shape.DOUBLE_CIRCLE);
            }
            nodeMap.put(state, n);
        }

        for (Transition<T> trans : machine.transitions) {
            Node n = nodeMap.get(trans.fromState)
                    .link(to(nodeMap.get(trans.toState)).with(Label.of(trans.getAcceptorChar() + "")));
                    nodes.add(n);
        }

        return nodes;
    }
}
