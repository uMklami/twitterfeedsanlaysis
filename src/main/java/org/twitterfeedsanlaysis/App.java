package org.twitterfeedsanlaysis;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.twitterfeedsanlaysis.visualization.JavaFX;
import org.twitterfeedsanlaysis.visualization.TabbedApp;

/**
 * Hello world!
 *
 */
public class App {
	private static Logger log = LogManager.getRootLogger();

	// public static Set<String> tweetTags;
	static TermOntologyMatcher termOntologyMatcher;

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// if (args.length != 2) {
		// log.error("Please provide two arguments, e.g tweets.txt
		// ontologies.json");
		// System.exit(0);
		// }
		// starting ontology data processing..
		log.info("Initializing ontologies!");
		termOntologyMatcher = new TermOntologyMatcher("allTweet.txt", "myontology.json");
		Map<String, Object> onotolgy = termOntologyMatcher.getOntology();
		// OntoConceptResultGenerator.generatFile("ontology_result", onotolgy);
		log.info("Done with Ontology!");

		// set data for visualisation...
		JavaFX.setData(termOntologyMatcher.getTags(), (List<Term>) onotolgy.get("matched"));
		TabbedApp.launch(TabbedApp.class);
	}
}
