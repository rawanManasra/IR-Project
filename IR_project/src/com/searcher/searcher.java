package com.searcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;

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
		//com.query.Query qur = new com.query.Query("0", question);
		//System.out.println(question);
		final Query q = qp.parse(question);
		// create index searcher for the directory reader, in order to search through it
		final IndexSearcher searcher = new IndexSearcher(reader);
		// find the top 10 hits of the query
		final TopDocs td = searcher.search(q, 5);
		final PassageSearcher passageSearcher = new TermVectorsPassageSearcher(searcher, AppConstants.body, 0.1,
				PassageScorer.DOC_SCORE_AND_QUERY_TF);
		final List<Passage> passages = passageSearcher.search(q, td, 5, 40000);
		ArrayList<Answer> answers = new ArrayList<Answer>();
		for(Passage passage: passages) {
			Answer ans = new Answer(passage.getText(),passage.getDocScore());
			answers.add(ans);
		}
		return new bestAnswers(id,answers);
//		for (final Passage passage : passages) {
//			System.out.println(
//					Utils.format("doc = %s, doc_score=%.4f, psg_score=%.4f, query_terms=%s, offsets=(%d,%d)\n%s\n",
//							passage.getDocID(), passage.getDocScore(), passage.getScore(), passage.getQueryTerms(),
//							passage.getStartOffset(), passage.getEndOffset(), passage.getText()));
//		}
	}

}
