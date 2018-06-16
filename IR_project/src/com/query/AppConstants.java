package com.query;


import java.util.ArrayList;
import org.apache.lucene.analysis.util.CharArraySet;

public class AppConstants {

	static String[] words = { "how", "why", "when", "what", "can", "use", "when", "to", "a", "an", "and", "are", "as",
			"at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such",
			"that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with", "which",
			"usually", "rarely", "sometimes", "do", "does", "don't", "doesn't", "thus", "my", "i", "you", "they", "he","just",
			"she", "them", "been", "i've", "we","hence","therefor" ,"often","else","elsewhere","instead","from"};

	static ArrayList<String> wordsArray = new ArrayList<String>();
	public final static String id = "id";
	public final static String body = "body";

	public static CharArraySet getStopWords() {
		for (String s : words) {
			wordsArray.add(s);
		}
		CharArraySet stopWords = new CharArraySet(wordsArray, true);
		return stopWords;
	}
}
