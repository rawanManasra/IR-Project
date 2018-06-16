package com.analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.queryparser.classic.ParseException;

import com.query.AppConstants;

public class graph extends Analyzer {

	protected static TokenStreamComponents createComponents(String arg0, Reader reader) throws ParseException, java.text.ParseException {
		System.out.println("1");
		// TODO Auto-generated method stub
		Tokenizer source = new ClassicTokenizer();

		source.setReader(reader);
		TokenStream filter = new StandardFilter(source);

		filter = new LowerCaseFilter(filter);
		SynonymMap mySynonymMap = null;

		try {

			mySynonymMap = buildSynonym();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filter = new SynonymFilter(filter, mySynonymMap, false);

		return new TokenStreamComponents(source, filter);

	}

	private static SynonymMap buildSynonym() throws IOException, ParseException, java.text.ParseException {
		System.out.print("build");
		File file = new File("wn_s.pl");

		InputStream stream = new FileInputStream(file);
		Reader rulesReader = new InputStreamReader(stream);
		SynonymMap.Builder parser = null;
		parser = new WordnetSynonymParser(true, true, new EnglishAnalyzer(AppConstants.getStopWords()));
		System.out.print(parser.toString());
		((WordnetSynonymParser) parser).parse(rulesReader);
		SynonymMap synonymMap = parser.build();
		return synonymMap;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, IOException, ParseException, java.text.ParseException {
		//Reader reader = new FileReader("C:\\input.txt"); // here I have the queries
		// that I want to expand
		 TokenStreamComponents TSC = createComponents( "" , new StringReader("How can I get something from nothing"));
		// **System.out.print(TSC); //How to get the result from TSC????**
		 TokenStream stream = TSC.getTokenStream();
		 CharTermAttribute termattr = stream.addAttribute(CharTermAttribute.class);
		 stream.reset();
		 while (stream.incrementToken()) {
		     System.out.println(termattr.toString());
		 }
	}

	@Override
	protected TokenStreamComponents createComponents(String string) {
		throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose																	// Tools | Templates.
	}
}
