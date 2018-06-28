package com.Indexer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.SnapshotDeletionPolicy;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;

import com.jsonReading.ReadYahooDataBase;
import com.query.AppConstants;

public class indexer {
	private static IndexWriter writer;

	void close() throws IOException {
		writer.close();
	}

	static void createIndex(Directory dir, Analyzer analyzer) throws IOException {
		IndexWriterConfig indCon = new IndexWriterConfig(analyzer).setOpenMode(OpenMode.CREATE).setCommitOnClose(true);
		final SnapshotDeletionPolicy snapshotDeletionPolicy = new SnapshotDeletionPolicy(
		indCon.getIndexDeletionPolicy());
		indCon.setIndexDeletionPolicy(snapshotDeletionPolicy);
		writer = new IndexWriter(dir, indCon);
		HashMap<Long, String> Curpus = ReadYahooDataBase.readDataBase();
		for (Entry<Long, String> pair : Curpus.entrySet()) {
			String answer = pair.getValue();
			Long id = pair.getKey();
			// create document
			final Document doc = new Document();
			// add id field to the created document, n=and store it.
			doc.add(new StringField(AppConstants.id, String.valueOf(id), Store.YES));
			// add the body field for the created document
			FieldType field = new FieldType(TextField.TYPE_STORED);
			field.setStoreTermVectorOffsets(true);
			field.setStoreTermVectors(true);
			field.setStoreTermVectorPositions(true);
			field.freeze();
			doc.add(new Field(AppConstants.body, answer, field));
			// add the document to the index writer
			writer.addDocument(doc);
		}
		writer.close();
	}

	public static void indixing(Directory dir, Analyzer analyzer) throws IOException {
		createIndex(dir, analyzer);
	}
}
