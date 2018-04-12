package org.twitterfeedsanlaysis.visualization.models;

import javafx.scene.paint.Color;

public class JNode {
	 int id;
     String label;
     static int nodeCount = 0;
     Color color;
     /**
      * Creates node with unique id, and label
      * @param id
      * @param label
      */
     public JNode(String label) {
         this.id = nodeCount++;
         this.label = label;
     }
     public String toString() {
         return label;
     }
}