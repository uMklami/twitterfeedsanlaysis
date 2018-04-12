package org.twitterfeedsanlaysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class TermOntologyMatcher {
    private static Logger log = LogManager.getRootLogger();

    private String tweetsPath;
    private String ontologiesPath;
    private Set<String> conceptTweetTags = new HashSet<String>();
    private Map<String, int[]> tags = new HashMap<String, int[]>();

    public String getTweetsPath() {
        return tweetsPath;
    }

    public void setTweetsPath(String tweetsPath) {
        this.tweetsPath = tweetsPath;
    }

    public Set<String> getConceptTweetTags() {
        return conceptTweetTags;
    }

    public void setConceptTweetTags(Set<String> concepttweetTags) {
        conceptTweetTags = concepttweetTags;
    }

    public String getOntologiesPath() {
        return ontologiesPath;
    }

    public void setOntologiesPath(String ontologiesPath) {
        this.ontologiesPath = ontologiesPath;
    }

    public Map<String, int[]> getTags() {
        return tags;
    }

    public void setTags(Map<String, int[]> tags) {
        this.tags = tags;
    }

    public TermOntologyMatcher() {
    }

    public TermOntologyMatcher(String tweetsFilePath, String ontologiesFilePath) {
        this.tweetsPath = tweetsFilePath;
        this.ontologiesPath = ontologiesFilePath;
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> getOntology() {
        Map<String, Object> resultTerms = matchTerms(tweetsPath, ontologiesPath);
        List<Term> matchedTerms = (List<Term>) resultTerms.get("matched");
        List<String> unMatchedTerms = (List<String>) resultTerms.get("unMatched");
        /***
         * Preparing Map<String, int[]> from List<Term> to be visualize.
         * **/
       this.setTags(visualizeMap(matchedTerms));

        return printMatchedTerms(matchedTerms, unMatchedTerms);
    }

    public Map<String, Object> matchTerms(String tweeetsfile, String ontologiesfile) {

        List<Term> matched = new ArrayList<Term>();
        List<String> unMatched = new ArrayList<String>();
        Map<String, Object> matchTermResult = new HashMap<String, Object>(); 
        List<String> tweetTags = DataLoader.fetchTags(tweeetsfile);
        List<Map<String, String>> ontologies = DataLoader.fetchOntologies(ontologiesfile);

        for (String tag : tweetTags) {
            int rootCounter = 0;
            for (Map<String, String> ontology : ontologies) {
                String ontoTagsString = ontology.get("Tag").toLowerCase();
                String[] ontoTags = ontoTagsString.replaceAll(" ", "").split(",");
                for (String ontoTag : ontoTags) {
                    if (ontoTag.equals(tag)) {
                        rootCounter++;
                        addToRelation(tag, ontology, matched);
                        conceptTweetTags.add(tag);
                    }
                }
            }
            log.debug(tag + ", matched " + rootCounter + " times");
            for (Term term : matched) {
                int childIndex = -1;
                if ((childIndex = getMatchedChildIndex(term.getChilds(), tag)) != -1) {
                    term.getChilds().get(childIndex).setOverAllFrequency(rootCounter);
                }
            }
            if (rootCounter == 0) {
                unMatched.add(tag);
            }
        }
        matchTermResult.put("matched", finalHierarchy(matched));
        matchTermResult.put("unMatched", unMatched);

        return matchTermResult;
    }

    public int getMatchedChildIndex(List<Term> childs, String tag) {
        for (int j = 0; j < childs.size(); j++) {
            if (childs.get(j).getTerm().equals(tag)) {
                return j;
            }
        }
        return -1;
    }

    public List<Term> addToRelation(String term, Map<String, String> ontology, List<Term> matchedTerms) {
        for (int i = 0; i < matchedTerms.size(); i++) {
            if (matchedTerms.get(i).getTerm().equals(ontology.get("Parent").trim())) {
                matchedTerms.get(i).addChild(new Term(term, ontology.get("Title"), ontology.get("Body")
                				   .replaceAll("[\r\n]+", ""), ontology.get("Tag")));
                return matchedTerms;
            }
        }
        Term relation = new Term(ontology.get("Parent").trim());
        relation.addChild(new Term(term, ontology.get("Title"), ontology.get("Body")
        		.replaceAll("[\r\n]+", ""), ontology.get("Tag")));
        matchedTerms.add(relation);

        return matchedTerms;
    }

    public List<Term> finalHierarchy(List<Term> matchedTerms) {
    	for (int i = 0; i < matchedTerms.size(); i++) {
            Term relation = matchedTerms.get(i);
            String parent = relation.getTerm().toLowerCase();
            for (int k = 0; k < matchedTerms.size(); k++) {
                if (k != i) {
                    Term nextRelation = matchedTerms.get(k);
                    for (int j = 0; j < nextRelation.getChilds().size(); j++) {
                        if (parent.equals(nextRelation.getChilds().get(j).getTerm().toLowerCase())) {
                            nextRelation.getChilds().get(j).addChilds(relation.getChilds());
                            matchedTerms.remove(relation);
                        }
                    }
                }
            }
        }
        return matchedTerms;
    }

    public Map<String,Object> printMatchedTerms(List<Term> matchTerms, List<String> unMatchTerms) {
        int matchedCount = matchTerms.size();
        int unMatchedCount = unMatchTerms.size();
        log.info("matched [" + matchedCount + "]");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("matched", matchTerms);
        map.put("unmatched", unMatchTerms);
        log.info(percentage(matchedCount, unMatchedCount) + "% tags matched");
        return map;
    }
    
    public String percentage(int matched, int unMatched){
        float total = matched + unMatched;
        float percentage = 0.0F;
        if (total != 0) {
            percentage = ((matched * 100.0F) / total);
        }
         return String.format("%.2f",percentage);
    }
    
    // for creating map to be visualise
	public Map<String, int[]> visualizeMap(List<Term> terms) {
		Map<String, int[]> tags = new HashMap<String, int[]>();
		for (Term term : terms) {
			for (Term child : term.getChilds()) {
				int[] freqs = new int[2];
				String name = child.getTerm();
				freqs[0] = child.getOverAllFrequency();
				freqs[1] = child.getFrequency();
				tags.put(name, freqs);
			}
		}
		return tags;
	}
}