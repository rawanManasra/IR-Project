package com.analyzer;
 
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
 
public class SimilarityCalculationDemo {
 
	private static ILexicalDatabase db = new NictWordNet();
	/*
	//available options of metrics
	private static RelatednessCalculator[] rcs = { new HirstStOnge(db),
			new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
			new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db) };
	*/
	public static double compute(String word1, String word2) {
		
		WS4JConfiguration.getInstance().setMFS(true);
		double s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
		return s;
	}
 
	public static void main(String[] args) {
		String[] words = {"dog","house"};
 
		for(int i=0; i<words.length-1; i++){
			for(int j=i+1; j<words.length; j++){
				double distance = compute(words[i], words[j]);
				System.out.println(words[i] +" -  " +  words[j] + " = " + distance);
			}
		}
	}
}