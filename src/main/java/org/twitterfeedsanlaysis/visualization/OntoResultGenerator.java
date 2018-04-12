package org.twitterfeedsanlaysis.visualization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

public class OntoResultGenerator {
	
	public static void generatFile(String fileName, Object object){
        FileWriter fw = null;
        try {
        fw = new FileWriter(new File(fileName+".txt"));
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(fw, object);
        fw.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	}
}
