package org.twitterfeedsanlaysis.visualization;

import java.util.Map;

import javafx.scene.Group;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class BubbleGraph {

private static Map<String, int[]> data_map;
	
	public BubbleGraph() {
	}

	public static void setMap(Map<String, int[]> map) {
		data_map = map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Group start() throws Exception {
		Group graph = new Group();
		final NumberAxis xAxis = new NumberAxis(0, 50, 2);
		final NumberAxis yAxis = new NumberAxis(0, 50, 2);
		final BubbleChart<Number, Number> bubbleChart = new BubbleChart<Number, Number>(xAxis, yAxis);
		yAxis.setLabel("Indivisual Frequency");
		xAxis.setLabel("Overall Frequency ");
		bubbleChart.setTitle("Tags Frequency");
		
		for (String key : data_map.keySet()) {
			XYChart.Series bubble = new XYChart.Series();
			bubble.setName(key);
			int[] frequences = data_map.get(key);
			bubble.getData().add(new XYChart.Data(frequences[0], frequences[1]));
			bubbleChart.getData().add(bubble);
		}
		
		bubbleChart.setMinSize(1200, 650);
		graph.getChildren().addAll(bubbleChart);
	    
		return graph;
	}
}
