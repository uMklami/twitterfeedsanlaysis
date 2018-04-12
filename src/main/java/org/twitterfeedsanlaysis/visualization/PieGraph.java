package org.twitterfeedsanlaysis.visualization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class PieGraph {

	List<PieChart.Data> pieDatalist = new ArrayList<PieChart.Data>();
	private static Map<String, int[]> pieData;
	public int total_freqs = 0;

	public Map<String, int[]> getPieData() {
		return pieData;
	}

	public static void setMap(Map<String, int[]> map) {
		pieData = map;
	}

	public Group start() {
		Group pie = new Group();
		ObservableList<PieChart.Data> pieChartData = null;

		for (Entry<String, int[]> entry : pieData.entrySet()) {
			int freq = entry.getValue()[0];
			total_freqs += freq;
			pieDatalist.add(new PieChart.Data(entry.getKey(), freq));
		}

		pieChartData = FXCollections.observableArrayList(pieDatalist);

		final PieChart chart = new PieChart(pieChartData);
		chart.setTitle("Matched Tags");
		chart.setLegendSide(Side.LEFT);
		chart.setMinSize(1200, 650);

		// . adding mouse Event
		final Label caption = new Label("");
        caption.setStyle("-fx-font: 24 arial;");
        for (final PieChart.Data data : chart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
//            	@Override
                    public void handle(MouseEvent e) {
                        caption.setTranslateX(e.getSceneX());
                        caption.setTranslateY(e.getSceneY());
                        double percent = (data.getPieValue()/total_freqs)*100F;
                        caption.setText(String.format("%.2f",percent)+ "%");
                     }
                });
        }
        pie.getChildren().addAll(chart,caption);
		return pie;
	}
}
