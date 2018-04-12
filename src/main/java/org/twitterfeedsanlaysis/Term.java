package org.twitterfeedsanlaysis;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Term {

    private String title;
    private String term;
    private String tags;
    private String description = null;
    private int frequency = 1;
    private int overAllFrequency = 1;
    @JsonIgnore
    private List<Term> childs;

    public Term() {
    }

    public Term(String term) {
        this.term = term;
    }

    public Term(String term, String title, String description, String tags) {
        this.term = term;
        this.title = title;
        /* minizing description */
        if(description.length()> 32){
        this.description = description.substring(0, 32) + "...";
        }else{
            this.description = description;
        }
        this.tags = tags;
    }

    public List<Term> getChilds() {
        return childs;
    }

    public void setChilds(List<Term> childs) {
        this.childs = childs;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrequency() {
        return frequency;
    }

    public void incrementFrequency() {
        frequency++;
    }

    public void setOverAllFrequency(int overAllFrequency) {
        this.overAllFrequency = overAllFrequency;
    }

    public int getOverAllFrequency() {
        return overAllFrequency;
    }

    public void incrementoverAllFrequency() {
        overAllFrequency++;
    }

    public void addChild(Term child) {
        if (childs == null) {
            childs = new ArrayList<Term>();
        }
        if (!contains(child)) {
            childs.add(child);
        }
    }
    
    public void addChilds(List<Term> childs){
    	for(Term child : childs){
    		addChild(child);
    	}
    }

    public boolean contains(Term child) {
        for (Term oldChild : childs) {
            if (oldChild.getTerm().equalsIgnoreCase(child.getTerm())) {
                oldChild.incrementFrequency();
                return true;
            }
        }
        return false;
    }
    

    @Override
    public String toString() {
        return "term[" + term + "], title[" + title
                + "], description[...], frequency[" + frequency
                + "], overallFrequency[" + overAllFrequency + "]";
    }
}