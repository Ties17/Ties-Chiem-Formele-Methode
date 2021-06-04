package ties;

import ties.Machine;
import static guru.nidi.graphviz.model.Factory.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import guru.nidi.graphviz.attribute.Attributes;
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

    public void GenerateGraphizContent(Machine<T> machine) {

        ArrayList<Node> nodes = createNodes(machine);

        Graph g = graph("example1").directed()
        .graphAttr().with(Rank.dir(RankDir.LEFT_TO_RIGHT))
        .nodeAttr().with(Font.name("arial"))
        .linkAttr().with("class", "link-class")
        .with(
            nodes
        );

        try {
            Graphviz.fromGraph(g).height(100).render(Format.DOT).toFile(new File("example/ex1.dot"));
        }
        catch(IOException ex) {
        }
    }

    private ArrayList<Node> createNodes(Machine<T> machine) {
        ArrayList<Node> nodes = new ArrayList<>();
        for(Transition<T> trans : machine.transitions) {
            //nothing -> q0

            Node node = node(trans.fromState + "").link(to(node(trans.toState + "")).with((Label.of(trans.acceptor + "")))); //.with(Shape.DOUBLE_CIRCLE);

            for(Object o : machine.endStates) {
                if(o.toString().equals(node.name().toString())) {
                    node = node.with(Shape.DOUBLE_CIRCLE);
                }
            }

            nodes.add(node);
        }
        return nodes;
    }
}