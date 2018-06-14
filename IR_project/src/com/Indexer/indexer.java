package com.Indexer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.jsonReading.ReadYahooDataBase;
import com.query.AppConstants;

public class indexer {
	private IndexWriter writer;
	 indexer() throws IOException {
		Directory dir = FSDirectory.open(new File("index.txt").toPath());
		Analyzer analyzer = new EnglishAnalyzer(AppConstants.stopWords);
		IndexWriterConfig indCon = new IndexWriterConfig(analyzer).setOpenMode(OpenMode.CREATE).setCommitOnClose(true);
		writer = new IndexWriter(dir, indCon);

	}
	 void close() throws IOException {
		writer.close();
	}
	
	 void createIndex() throws IOException {
		HashMap<Long,String> Curpus = ReadYahooDataBase.readDataBase();
		for (Entry<Long, String> pair : Curpus
				.entrySet()) {
			String answer = pair.getValue();
			Long id = pair.getKey();				
			//create document
			final Document doc = new Document();
			//add id field to the created document, n=and store it.
			doc.add(new StringField(AppConstants.id, String.valueOf(id), Store.YES));
			// add the body field for the created document
			FieldType field = new FieldType();
			field.setStoreTermVectorOffsets(true);
			field.setStoreTermVectors(true);
			field.setStoreTermVectorPositions(true);
			field.freeze();
			doc.add(new Field(AppConstants.body, answer, field));
			//add the document to the index writer
			writer.addDocument(doc);
		}
	}
	
	public static void indixing() throws IOException {
		indexer index = new indexer();
		index.createIndex();
	}
}
