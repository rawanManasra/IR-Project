package com.query;

import java.util.ArrayList;

import org.apache.lucene.analysis.CharArraySet;

public class AppConstants {

	static String[] words = { "how", "why", "when", "what", "can", "use", "when", "to", "a", "an", "and", "are", "as",
			"at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
			"that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with", "which",
			"do", "does", "don't", "doesn't", "thus" };
	static ArrayList<String> wordsCol = new ArrayList<String>();
	public static CharArraySet stopWords = new CharArraySet(wordsCol, true);
	public final static String id = "id";
	public final static String body = "body";
}
