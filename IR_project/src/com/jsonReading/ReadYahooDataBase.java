package com.jsonReading;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public final class ReadYahooDataBase {
	public  static List<QAData> DataBase;
	public HashMap<Long,String> Curps = new HashMap<Long,String>();
	public ReadYahooDataBase() throws FileNotFoundException {
		final java.lang.reflect.Type QAType = new TypeToken<List<QAData>>() {
		}.getType();
		DataBase = new Gson().fromJson(new JsonReader(new FileReader("nfL6.json")), QAType); // contains the whole QA list
		Long i = (long) 0;
		for(QAData qa: DataBase) {
			for(String ans: qa.nbestanswers) {
				Curps.put(i, ans);
				i++;
			}
		}
	}
}
