package org.twitterfeedsanlaysis;

import org.twitterfeedsanlaysis.TermOntologyMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * Hello world!
 *
 */
public class App 
{
	private static Logger log = LogManager.getRootLogger();
    public static void main( String[] args )
    {
    	log.info("Twitter Feed Anaylsis...");
//    	if (args.length != 2) {
//			log.error("Please provide two arguments, e.g tweets.txt ontologies.json");
//			System.exit(0);
//		}
    	
		log.info("Initializing ontologies!");
		(new TermOntologyMatcher("mytweets.txt", "myontology.json")).matchTerms();
		log.info("Done!");
    }
}
