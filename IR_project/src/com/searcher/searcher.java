package com.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermStatistics;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.Similarity.SimScorer;
import org.apache.lucene.search.similarities.Similarity.SimWeight;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.BytesRef;

import com.passage.Passage;
import com.passage.PassageScorer;
import com.passage.PassageSearcher;
import com.passage.TermVectorsPassageSearcher;
import com.passage.Utils;
import com.query.Answer;
import com.query.AppConstants;
import com.query.bestAnswers;

public class searcher {
	String question;

	public static bestAnswers searchQuery( String question,String id, Directory dir, Analyzer analyzer)
			throws ParseException, IOException {
		DirectoryReader reader = DirectoryReader.open(dir);
		final QueryParser qp = new QueryParser(AppConstants.body, analyzer);
		try {
		final Query q = qp.parse(question);
		// create index searcher for the directory reader, in order to search through it
		final IndexSearcher searcher = new IndexSearcher(reader);
		final TopDocs td = searcher.search(q, 5);
		final PassageSearcher passageSearcher = new TermVectorsPassageSearcher(searcher, AppConstants.body, 0.1,
				PassageScorer.DOC_SCORE_AND_QUERY_TF);
		final List<Passage> passages = passageSearcher.search(q, td, 5, 40000);
		ArrayList<Answer> answers = new ArrayList<Answer>();
		for(Passage passage: passages) {
			Answer ans = new Answer(passage.getText(),passage.getDocScore());
			answers.add(ans);
		}
			return new bestAnswers(id, answers);
		}
		catch (ParseException p) {
			System.out.println(question + " id " + id);
			return null;
		}
	}

}
