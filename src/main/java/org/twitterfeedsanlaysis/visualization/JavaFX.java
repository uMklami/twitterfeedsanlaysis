package org.twitterfeedsanlaysis.visualization;

import java.util.List;
import java.util.Map;

import org.twitterfeedsanlaysis.Term;

import javafx.application.Application;

public abstract class JavaFX extends Application {

    public static void main(String[] args) {
    }
    
    public static void setData( Map<String, int[]> data, List<Term> hierarchicalData){
    	BubbleGraph.setMap(data);
    	PieGraph.setMap(data);
    	TreeGraph.setOntoData(hierarchicalData);
    }
}