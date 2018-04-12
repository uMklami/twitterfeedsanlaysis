package org.twitterfeedsanlaysis.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import org.apache.commons.collections15.functors.ConstantTransformer;
import org.twitterfeedsanlaysis.Term;
import org.twitterfeedsanlaysis.visualization.models.JEdge;
import org.twitterfeedsanlaysis.visualization.models.JNode;

import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import javafx.embed.swing.SwingNode;

public class TreeGraph {

	private static List<Term> ontoData;
	public static List<Term> getOntoData() {
		return ontoData;
	}
	public static void setOntoData(List<Term> ontoData) {
		TreeGraph.ontoData = ontoData;
	}

	/**
	 * the graph
	 */
	GraphZoomScrollPane panel;
	Forest<JNode, JEdge> graph;
	
	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<JNode, JEdge> vv;
	TreeLayout<JNode, JEdge> layout;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TreeGraph() {

		// create a simple graph for ontology data
		graph = new DelegateForest<JNode, JEdge>();
		this.addData();
		layout = new TreeLayout<JNode, JEdge>(graph);
		
		vv = new VisualizationViewer<JNode, JEdge>(layout, new Dimension(1500, 700));
		vv.setBackground(Color.WHITE);
		vv.setToolTipText("<html><center>Hierarchical Ontology</center></html>");
		
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<JNode, JEdge>());
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderContext().setEdgeDrawPaintTransformer(new ConstantTransformer(Color.DARK_GRAY));
		vv.getRenderContext().setVertexFillPaintTransformer(new ConstantTransformer(Color.DARK_GRAY));	
/*
		// Custom Visualisation...
		vv.getRenderContext().setVertexFillPaintTransformer(new VertexPaintTransformer());
		vv.getRenderContext().setVertexShapeTransformer(new VertexShapTransformer());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
*/			
		panel = new GraphZoomScrollPane(vv);
		final DefaultModalGraphMouse<?, ?> graphMouse = new DefaultModalGraphMouse();
		vv.setGraphMouse(graphMouse);
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
	}
/*
 //Static class of random colours for JNode..
     private static class VertexPaintTransformer implements Transformer<JNode,Paint> {
    	 
        @Override
        public Paint transform(JNode jNode) {
            Color color = null;
            Random random = new Random();
            int r = random.nextInt(256);  
            int g = random.nextInt(200);
            int b = random.nextInt(250);
            color = new Color(r, g, b);
            return color;
        }
    }
    
 // Static class for random Shape Transformer according to JNode label..
     private static class VertexShapTransformer implements Transformer<JNode, Shape> {
		@Override
		public Shape transform(JNode jNode) {
			Shape shape = null;
			float width = jNode.toString().length()*10;
	        shape = new Ellipse2D.Double(-(width/2), -12.5, width, 25);
	        return shape;
		} 
     }
*/
	/**
	 * Adding data to graph
	 * 
	 * @param data
	 */
	public void addData(){
//		JNode root = new JNode("ontology");
//		graph.addVertex(root);
		for (Term term : ontoData) {
			JNode jchild = new JNode(term.getTerm());
			graph.addVertex(jchild);
//			graph.addEdge(new JEdge(), root, jchild , EdgeType.DIRECTED);
			if (term.getChilds() != null) {
				for (Term child : term.getChilds()) {
					JNode jsubchild = new JNode(child.getTerm());
					graph.addEdge(new JEdge(), jchild, jsubchild);
					if (child.getChilds() != null) {
						for (Term subchild : child.getChilds()) {
							graph.addEdge(new JEdge(), jsubchild, new JNode(subchild.getTerm()));
						}
					}
				}
			}
		}
	}

	/**
	 * A driver for this Graph
	 */
	public SwingNode start() {
		final SwingNode swingNode = new SwingNode();
		swingNode.setContent(panel);
		return swingNode;
	}
}