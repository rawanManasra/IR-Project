
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;


import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.standard.ClassicFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.Indexer.indexer;
import com.query.AppConstants;
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
			long startTime = System.currentTimeMillis();
			//indexer.indixing(dir, analyzer);
			String question = "How expensive are the activities is cancun?";
			searcher.searchQuery(removeStopWords(question, analyzer), dir, analyzer);
			long endTime = System.currentTimeMillis();
			System.out.println(endTime-startTime);
		}
	}

	public static String removeStopWords(String textFile,EnglishAnalyzer analyzer) throws Exception {
		StandardTokenizer stdToken =  new StandardTokenizer();
		stdToken.setReader(new StringReader(textFile));
		TokenStream tokenStream = new StopFilter(new ASCIIFoldingFilter(new ClassicFilter(new LowerCaseFilter(stdToken))),analyzer.getStopwordSet());
		StringBuilder sb = new StringBuilder();
		tokenStream.reset();
		CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
		while (tokenStream.incrementToken()) {
			String term = charTermAttribute.toString();
			sb.append(term + " ");
		}
		tokenStream.close();
		return sb.toString();
	}

	/**
	 * create directory
	 * 
	 * @return path to the created directory
	 * @throws IOException
	 */
	private static Directory newDirectory() throws IOException {
		return FSDirectory.open(new File("d:/tmp/ir-class/search").toPath());
	}

//	public static void Initializer(){
//	    try {
//	        JWNL.initialize(new FileInputStream("file_properties.xml"));
//	        dictionary = Dictionary.getInstance();
//	        morphPro = dictionary.getMorphologicalProcessor();
//	    }
//	    catch(FileNotFoundException e){
//	        e.printStackTrace();
//
//	    } catch (JWNLException e) {
//	        e.printStackTrace();
//	    }
//	}
	/**
	 * create English Analyzer
	 * 
	 * @return the created analyzer
	 */
	
	private static EnglishAnalyzer newAnalyzer() {
		return new EnglishAnalyzer(AppConstants.getStopWords());
	}

}
