package org.twitterfeedsanlaysis.visualization;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class TabbedApp extends Application {
	BubbleChart<Number, Number> bubbleChart;
    Group root = new Group();
    TabPane tabPane = new TabPane();

    @SuppressWarnings("unchecked")
	public void init() throws Exception {
        // Prepare tab pane with set of tabs
        BorderPane borderPane = new BorderPane();
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Group frequencyGraph = new BubbleGraph().start();
        /**	for labels inside bubbles in bubble chart**/
        bubbleChart = (BubbleChart<Number, Number>) frequencyGraph.getChildren().get(0);
        
        Group pieChart = new PieGraph().start();
        SwingNode treeGraph = new TreeGraph().start();
        
        /** tab for pie graph **/
        final Tab pie = new Tab("Matched Tags");
        pie.setId("1");
        pie.setContent(pieChart);
        /** ends here  **/
        
        /** tab for bubble graph **/
        final Tab bubble = new Tab("Tags Frequency");
        bubble.setId("2");
        bubble.setContent(frequencyGraph);
        /** ends here  **/
        
        /**	tab for tree graph **/
        final Tab tree = new Tab("Hierarchical Result");
        tree.setId("3");
        tree.setContent(treeGraph);
        /** ends here**/
        
        tabPane.getTabs().addAll( pie,bubble, tree);
        borderPane.setCenter(tabPane);
        root.getChildren().addAll(borderPane);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(root);
        
        scene.getStylesheets().add("style.css");
        //System.out.println(getClass().getResource("stylesheet/style.css").toString());
        primaryStage.setTitle("Ontology Visualization");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        /* For Bubble Chart, to show label inside bubble*/
		for (XYChart.Series<Number, Number> series : bubbleChart.getData()) {
	        for (XYChart.Data<Number, Number> data : series.getData()) {
	            Node bubble = data.getNode();
	            if (bubble != null && bubble instanceof StackPane) {
	                StackPane region = (StackPane) bubble;
	                if (region.getShape() != null && region.getShape() instanceof Ellipse) {
	                    Ellipse ellipse = (Ellipse) region.getShape(); 
	                    DoubleProperty fontSize = new SimpleDoubleProperty(20);
	                    Label label = new Label(series.getName());
	                    label.setAlignment(Pos.CENTER);
	                    ellipse.setRadiusX(series.getName().length()+30);
	                    ellipse.setRadiusY(label.getHeight()+10);
	                    label.minWidthProperty().bind(ellipse.radiusXProperty());
//	                    fontSize.bind(Bindings.when(ellipse.radiusXProperty().lessThan(40)).then(6).otherwise(10));
	                    fontSize.bind(Bindings.divide(ellipse.radiusXProperty(), 10));
	                    fontSize.bind(ellipse.radiusXProperty());
	                    region.getChildren().add(label);
	                }
	            }
	        }
	    }
        /**/
    }

    public void navigateTo(String tab) {
        for (Tab t : tabPane.getTabs()) {
            if (tab.equals("#" + t.getId())) {
                tabPane.getSelectionModel().select(t);
                return;
            }
        }
    }
}