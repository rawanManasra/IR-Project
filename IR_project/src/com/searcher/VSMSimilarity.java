package com.searcher;
import java.io.IOException;

import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.DefaultSimilarity;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.similarities.Similarity;

public class VSMSimilarity extends Similarity{

    // Weighting codes
    public boolean doBasic     = true;  // Basic tf-idf
    public boolean doSublinear = false; // Sublinear tf-idf
    public boolean doBoolean   = false; // Boolean

    //Scoring codes
    public boolean doCosine    = true;
    public boolean doOverlap   = false;
    // term frequency in document = 
    // measure of how often a term appears in the document
    public float tf(int freq) {     
        // Sublinear tf weighting. Equation taken from [1], pg 127, eq 6.13.
        if (doSublinear){
            if (freq > 0){
                return 1 + (float)Math.log(freq);
            } else {
                return 0;
            }
        } else if (doBoolean){
            return 1;
        }
        // else: doBasic
        // The default behaviour of Lucene is sqrt(freq), 
        // but we are implementing the basic VSM model
        return freq;
    }

    // inverse document frequency = 
    // measure of how often the term appears across the index
    public float idf(int docFreq, int numDocs) {
        if (doBoolean || doOverlap){
            return 1;
        }
        // The default behaviour of Lucene is 
        // 1 + log (numDocs/(docFreq+1)), 
        // which is what we want (default VSM model)
        return idf(docFreq, numDocs); 
    }

    // normalization factor so that queries can be compared 
    public float queryNorm(float sumOfSquaredWeights){
        if (doOverlap){
            return 1;
        } else if (doCosine){
            return super.queryNorm(sumOfSquaredWeights);
        }
        // else: can't get here
        return super.queryNorm(sumOfSquaredWeights);
    }

    // number of terms in the query that were found in the document
    public float coord(int overlap, int maxOverlap) {
        if (doOverlap){
            return 1;
        } else if (doCosine){
            return 1;
        }
        // else: can't get here
        return super.coord(overlap, maxOverlap);
    }

	@Override
	public long computeNorm(FieldInvertState state) {
		 if (doOverlap){
	            return 1;
	        } else if (doCosine){
	            return computeNorm(state);
	        }
	        // else: can't get here
	        return computeNorm(state);
	}

	@Override
	public SimWeight computeWeight(CollectionStatistics collectionStats, TermStatistics... termStats) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SimScorer simScorer(SimWeight weight, LeafReaderContext context) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
}