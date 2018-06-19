//import com.sun.corba.se.impl.util.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import java.text.ParseException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.synonym.SynonymFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.analysis.util.CharArraySet;


public class Graph extends Analyzer 
{ 

  protected static TokenStreamComponents createComponents(String fieldName, Reader reader) throws ParseException{
      System.out.println("1");
    // TODO Auto-generated method stub
    Tokenizer source = new ClassicTokenizer();

    source.setReader(reader);
    TokenStream filter = new StandardFilter( source);

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

private static SynonymMap buildSynonym() throws IOException, ParseException
{    System.out.print("build");
    File file = new File("wn_s.pl");

    InputStream stream = new FileInputStream(file);

    Reader rulesReader = new InputStreamReader(stream); 
    SynonymMap.Builder parser = null;
    parser = new WordnetSynonymParser(true, true, new StandardAnalyzer(CharArraySet.EMPTY_SET));
    System.out.print(parser.toString());
   ((WordnetSynonymParser) parser).parse(rulesReader);  
    SynonymMap synonymMap = parser.build();
    return synonymMap;
}

public static void main (String[] args) throws UnsupportedEncodingException, IOException, ParseException
{
Reader reader = new FileReader("C:\\input.txt"); // here I have the queries that I want to expand 
TokenStreamComponents TSC = createComponents( "" , new StringReader("some text goes here")); 
//System.out.print(TSC); //How to get the result from TSC????**
}

    @Override
    protected TokenStreamComponents createComponents(String string) 
    {
      throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 } 