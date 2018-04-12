package org.twitterfeedsanlaysis.visualization.models;

public class JEdge {
	int id;
	double capacity = 1.0;
    double weight = 1.0;
    static int edgeCounter = 0;
    
    /**
     * Creates edge with weight and capacity
     * @param weight
     * @param capacity
     */
    public JEdge(double weight, double capacity) {
        this.id = edgeCounter++;
        this.weight = weight;
        this.capacity = capacity;
    } 
     public JEdge(){
    	 this.id = edgeCounter++;
     }

    public String toString() {
        return "E"+id;
    }
}