
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required byOCP applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.Indexer.indexer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.query.AppConstants;
import com.query.Query;
import com.query.bestAnswers;
import com.searcher.searcher;

public class PassageSearchDemo {
	private static final FieldType TERM_VECTOR_TYPE;
	static {
		TERM_VECTOR_TYPE = new FieldType(TextField.TYPE_STORED);
		TERM_VECTOR_TYPE.setStoreTermVectors(true);
		TERM_VECTOR_TYPE.setStoreTermVectorPositions(true);
		TERM_VECTOR_TYPE.setStoreTermVectorOffsets(true);
		TERM_VECTOR_TYPE.freeze();
	}
	// data to search

	public static void main(String[] args) throws Exception {
		// create directory and create analyzer
		try (Directory dir = newDirectory(); EnglishAnalyzer analyzer = newAnalyzer()) {
			// indexer.indixing(dir, analyzer);
			ArrayList<Query> questions = readQuestions("finalEval.txt");
			ArrayList<bestAnswers> bestAns = new ArrayList<bestAnswers>();
			for (Query question : questions) {
				bestAns.add(searcher.searchQuery(removeStopWords(question.getQuestion(), analyzer), question.getId(),
						dir, analyzer));
			}
			Writer writer = new FileWriter("Output.json");
			Gson gson = new  GsonBuilder().setPrettyPrinting().create();
			gson.toJson(bestAns,writer);		
			writer.flush();
			writer.close();
		}
	}

	public static String removeStopWords(String textFile, EnglishAnalyzer analyzer) throws Exception {
		StandardTokenizer stdToken = new StandardTokenizer();
		stdToken.setReader(new StringReader(textFile));
		TokenStream tokenStream = new StopFilter(
				new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))), analyzer.getStopwordSet());
		StringBuilder sb = new StringBuilder();
		tokenStream.reset();
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			String term = charTermAttribute.toString();
			sb.append(term + " ");
		}
		tokenStream.close();
		if(sb.length() == 0) {
			sb.append("verb noun");
		}
		return sb.toString();
	}

	/**
	 * create directory
	 * 
	 * @return path to the created directory
	 * @throws IOException
	 */
	private static Directory newDirectory() throws IOException {
		return FSDirectory.open(new File("index").toPath());
	}

	
	/**
	 * create English Analyzer
	 * 
	 * @return the created analyzer
	 */

	private static EnglishAnalyzer newAnalyzer() {
		return new EnglishAnalyzer(AppConstants.getStopWords());
	}

	private static ArrayList<Query> readQuestions(String fileName) throws IOException {
		FileReader fr = new FileReader(fileName);
		BufferedReader br = new BufferedReader(fr);
		String sCurrentLine;
		ArrayList<Query> questions = new ArrayList<Query>();
		while ((sCurrentLine = br.readLine()) != null) {
			String[] line = sCurrentLine.split("\t");
			//System.out.println(line[0]);
			//System.out.println(line[0]+"   "+line[1]);
			Query query = new Query(line[0], line[1]);
			questions.add(query);

		}
		br.close();
		return questions;
	}

}
