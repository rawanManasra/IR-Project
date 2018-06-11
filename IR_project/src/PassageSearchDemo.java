
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
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.jsonReading.ReadYahooDataBase;

public class PassageSearchDemo {

	private static final String BODY_FIELD = "body";

	private static final FieldType TERM_VECTOR_TYPE;
	static {
		TERM_VECTOR_TYPE = new FieldType(TextField.TYPE_STORED);
		TERM_VECTOR_TYPE.setStoreTermVectors(true);
		TERM_VECTOR_TYPE.setStoreTermVectorPositions(true);
		TERM_VECTOR_TYPE.setStoreTermVectorOffsets(true);
		TERM_VECTOR_TYPE.freeze();
	}
//data to search
	
	public static void main(String[] args) throws Exception {
		//create directory and create analyzer
		ReadYahooDataBase rd = new ReadYahooDataBase();
		
		try (Directory dir = newDirectory(); Analyzer analyzer = newAnalyzer()) {
			// create index writer for the analyzer and the created directory.
			try (IndexWriter writer = new IndexWriter(dir, newIndexWriterConfig(analyzer))) {
				//iterate over the data base
				for (Entry<Long, String> pair : rd.Curps.entrySet()) {
					String answer = pair.getValue();
					Long id = pair.getKey();
				
					//create document
					final Document doc = new Document();
					//add id field to the created document, n=and store it.
					doc.add(new StringField("id", String.valueOf(id), Store.YES));
					// add the body field for the created document
					doc.add(new Field(BODY_FIELD, answer, TERM_VECTOR_TYPE));
					//add the document to the index writer
					writer.addDocument(doc);
				}
			}

			// create a directory reader to read the created directory
			try (DirectoryReader reader = DirectoryReader.open(dir)) {
				// create a query parser to parse the query according to English analyzer
				final QueryParser qp = new QueryParser(BODY_FIELD, analyzer);
				//parse "jimmy hollywood" into the query parser
				final Query q = qp.parse("substring");
				//create index searcher for the directory reader, in order to search through it
				final IndexSearcher searcher = new IndexSearcher(reader);
				//find the top 10 hits of the query
				final TopDocs td = searcher.search(q, 5);
				//
				final PassageSearcher passageSearcher = new TermVectorsPassageSearcher(searcher, BODY_FIELD, 0.1,
						PassageScorer.DOC_SCORE_AND_QUERY_TF);

				final List<Passage> passages = passageSearcher.search(q, td, 5, 100);
				for (final Passage passage : passages) {
					System.out.println(Utils.format(
							"doc = %s, doc_score=%.4f, psg_score=%.4f, query_terms=%s, offsets=(%d,%d)\n%s\n",
							passage.getDocID(), passage.getDocScore(), passage.getScore(), passage.getQueryTerms(),
							passage.getStartOffset(), passage.getEndOffset(), passage.getText()));
				}
			}
		}

	}
	/**
	 * create directory
	 * @return path to the created directory
	 * @throws IOException
	 */
	private static Directory newDirectory() throws IOException {
		return FSDirectory.open(new File("d:/tmp/ir-class/search").toPath());
	}
	
	/**
	 * create English Analyzer
	 * @return the created analyzer
	 */
	private static Analyzer newAnalyzer() {
		return new EnglishAnalyzer();
	}

	/**
	 * 
	 * @param analyzer: 
	 * @return
	 */
	private static IndexWriterConfig newIndexWriterConfig(Analyzer analyzer) {
		return new IndexWriterConfig(analyzer).setOpenMode(OpenMode.CREATE).setCommitOnClose(true);
	}

}
