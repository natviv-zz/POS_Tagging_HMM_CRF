
import java.io.*;
import java.util.*;


public class POSTaggedFile {

    /** The name of the LDC POS file */
    public File file = null;
    /** The I/O reader for accessing the file */
    protected BufferedReader reader = null;

    /** Create an object for a given LDC POS tagged file */
    public POSTaggedFile(File file) {
	this.file = file;
	try {
	    this.reader = new BufferedReader(new FileReader(file));
	}
	catch (IOException e) {
	    System.out.println("\nCould not open POSTaggedFile: " + file);
	    System.exit(1);
	}
    }

    /** Return the next line of POS tagged tokens from this file.
        Returns "\n" if end of sentence and start of a new one. 
        Returns null if end of file */
    protected String getNextPOSLine() {
	String line = null;
	try {
	    do {
		// Read a line from the file
		line = reader.readLine();
		if (line == null) {
		    // End of file, no more tokens, return null
		    reader.close();
		    return null;
		}
		// Sentence boundary indicator
		if (line.startsWith("======="))
		    line = "\n";
		// If sentence number indicator for ATIS or comment for Brown, ignore it
		if (line.startsWith("[ @") || line.startsWith("*x*"))
		    line = "";
	    } while (line.equals(""));
	}
	catch (IOException e) {
	    System.out.println("\nCould not read from TextFileDocument: " + file);
	    System.exit(1);
	}
	return line;
    }

    /** Take a line from the file and return a list of String tokens in the line */    
    protected List<String> getTokens (String line) {
	List<String> tokenList = new ArrayList<String>();
	line = line.trim();
	// Use a tokenizer to extract token/POS pairs in line, 
	// ignore brackets indicating chunk boundaries
	StringTokenizer tokenizer = new StringTokenizer(line, " []");
	while (tokenizer.hasMoreTokens()) {
	    String tokenPos = tokenizer.nextToken();
	    tokenList.add(segmentToken(tokenPos));
	    // If last token in line has end of sentence tag ".", 
	    // add a sentence end token </S>
	    if (tokenPos.endsWith("/.") && !tokenizer.hasMoreTokens()) {
		tokenList.add("</S>");
		}
	}
	return tokenList;
    }

    /** Segment a token/POS string and return just the token */
    protected String segmentToken (String tokenPos) {
	// POS tag follows the last slash
	int slash = tokenPos.lastIndexOf("/");
	if (slash < 0)	    
	    return tokenPos;
	else
	    return tokenPos.substring(0,tokenPos.length());
    }

    /** Return a List of sentences each represented as a List of String tokens for 
        the sentences in this file */
    protected List<List<String>> tokenLists() {
	List<List<String>> sentences = new ArrayList<List<String>>();
	List<String> sentence = new ArrayList<String>();
	String line;
	while ((line=getNextPOSLine()) != null) {
	    // Newline line indicates new sentence
	    if (line.equals("\n")) {
		if (!sentence.isEmpty()) {
		    // Save completed sentence
		    sentences.add(sentence);
		    // and start a new sentence
		    sentence = new ArrayList<String>();
		}
	    }
	    else {
		// Get the tokens in the line
		List<String> tokens = getTokens(line);
		if (!tokens.isEmpty()) {
		    // If last token is an end-sentence token "</S>"
		    if (tokens.get(tokens.size()-1).equals("</S>")) {
			// Then remove it 
			tokens.remove(tokens.size()-1);
			// and add final sentence tokens
			sentence.addAll(tokens);
			// Save completed sentence
			sentences.add(sentence);
			// and start a new sentence
			sentence = new ArrayList<String>();
		    }
		    else {
			// Add the tokens in the line to the current sentence
			sentence.addAll(tokens);
		    }
		}
	    }
	}
	// File should always end at end of a sentence
	assert(sentence.isEmpty());
	return sentences;
    }


    /** Take a list of LDC tagged input files or directories and convert them to a List of sentences
       each represented as a List of token Strings */
    public static List<List<String>> convertToTokenLists(File[] files) { 
	List<List<String>> sentences = new ArrayList<List<String>>();
	for (int i = 0; i < files.length; i++) {
	    File file = files[i];
	    if (!file.isDirectory()) {
		if (!file.getName().contains("CHANGES.LOG"))
		    sentences.addAll(new POSTaggedFile(file).tokenLists());
	    }
	    else 
	    {
		File[] dirFiles = file.listFiles();
		sentences.addAll(convertToTokenLists(dirFiles));
	    }
		
	}          
	return sentences;
    }
	
    /** Convert LDC POS tagged files to just lists of tokens for each sentences 
     *  and print them out. */
    public static void main(String[] args) throws IOException {
	File[] files = new File[args.length];
	for (int i = 0; i < files.length; i++) 
	    files[i] = new File(args[i]);
	List<List<String>> sentences = 	convertToTokenLists(files);	
	System.out.println("# Sentences=" + sentences.size());	
	//System.out.println(sentences.get(0));
	
	for(List<String> sentence:sentences)
	{
		for(String token: sentence)
		{
			int slash = token.lastIndexOf("/");
			System.out.print(token.substring(0,slash));
			String word = token.substring(0,slash);
			
			//Adding suffix features - Uncomment to add suffix features
			if(slash>=3 && word.substring(slash-3,slash).equals("ing"))
			    System.out.print(" ing");
			if(slash>=1 && word.substring(slash-1,slash).equals("s"))
			    System.out.print(" s");
			if(slash>=2 && word.substring(slash-2,slash).equals("ly"))
                            System.out.print(" ly");
			if(slash>=4 && word.substring(slash-4,slash).equals("able"))
                            System.out.print(" able");
			if(slash>=4 && word.substring(slash-4,slash).equals("ible"))
                            System.out.print(" ible");			
			if(slash>=2 && word.substring(slash-2,slash).equals("ed"))
                            System.out.print(" ed");
			if(slash>=3 && word.substring(slash-3,slash).equals("ant"))
                            System.out.print(" ant");
			if(slash>=3 && word.substring(slash-3,slash).equals("ent"))
                            System.out.print(" ent");
			if(slash>=4 && word.substring(slash-4,slash).equals("ious"))
                            System.out.print(" ious");
			if(slash>=3 && word.substring(slash-3,slash).equals("ous"))
                            System.out.print(" ous");
			if(slash>=2 && word.substring(slash-2,slash).equals("ic"))
                            System.out.print(" ic");
			if(slash>=3 && word.substring(slash-3,slash).equals("ful"))
                            System.out.print(" ful");
			if(slash>=3 && word.substring(slash-3,slash).equals("ive"))
                            System.out.print(" ive");

			//Adding prefix features - uncomment to add prefix features
			if(word.length()>=2 && word.substring(0,2).equals("re"))
                System.out.print(" re");
			if(word.length()>=2 && word.substring(0,2).equals("in"))
                System.out.print(" in");
			if(word.length()>=2 && word.substring(0,2).equals("im"))
                System.out.print(" im");			
			if(word.length()>=3 && word.substring(0,3).equals("pre"))
                System.out.print(" pre");
			if(word.length()>=3 && word.substring(0,3).equals("mis"))
                System.out.print(" mis");
			if(word.length()>=3 && word.substring(0,3).equals("dis"))
                System.out.print(" dis");
			if(word.length()>=2 && word.substring(0,2).equals("un"))
                System.out.print(" un");
			
			//Adding morphological features - Uncomment to add morphological features
			 		
            if(Character.isUpperCase(word.charAt(0)))
			    System.out.print(" caps");
			if(word.indexOf("-")!=-1)
			    System.out.print(" -");
			if(Character.isDigit(word.charAt(0)))
			    System.out.print(" 0");
			if(word.length()<=5)
				System.out.print(" small");
			if(word.indexOf(".")!=-1)
			    System.out.print(" .");
			if(word.indexOf("'")!=-1)
			    System.out.print(" '");
			
	
			System.out.print(" "+token.substring(slash+1)+"\n");
			
			
		}
		
		System.out.print("\n");
		
	}
	
	
	/* Uncomment to run backward model
	for(List<String> sentence:sentences)
	{
		for(int i = sentence.size()-1; i>=0;i--)
		{
			String token = sentence.get(i);
			int slash = token.lastIndexOf("/");
			System.out.print(token.substring(0,slash));
			
			System.out.print(" "+token.substring(slash+1)+"\n");
			
		}
		
		System.out.print("\n");
	}
	
	*/
	
	
	
	
	
    }

}
