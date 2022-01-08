package com.detica.cyberreveal.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Arrays;
import java.util.Objects;

/**
 * A storm bolt which splits a line into words.
 */
public class WordSplitBolt extends BaseBasicBolt {

	private static final long serialVersionUID = 1990152678196466476L;

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {
		String line = tuple.getStringByField("line");

/************************************** Changed block - Start ***********************************
 Change: -  The method removeAllSpecialCharacters was created down this page. It eliminates
 			special characters by retaining just letters, digits, apostrophes, hyphens and spaces
 			from lines, rather than looking for special characters through the use of a long
 			reference list.
         - All letters in the line were also switched to lower case.

 Problems: - Error counting words due to the difference between small and capital letters.
 		  - apostrophe ('s) being recognized as a word.*/

/************************************** AFTER SUBMISSION - Changed block - Start **********************/
		String[] words = removeAllSpecialCharacters(line.replaceAll("--"," ").toUpperCase())
											.split(" "); // but not Roman Numerals

		String regEx = "^M{0,3}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$"; // will use to remove Roman N's

		Arrays.stream(words).filter(word-> !word.isEmpty() &&
									(word.equals("I") || !word.matches(regEx)))
				.forEach(word->collector.emit(new Values(word.trim())));
	}
/************************************** AFTER SUBMISSION - Changed block - End ************************/
/************************************** Previous block - Start ***********************************
 		String[] words = removeAllSpecialCharacters(line.toLowerCase()).split(" ");

		Arrays.stream(words).filter(word-> !word.isEmpty())
 				.forEach(word->collector.emit(new Values(word.trim())));
 }
 ************************************** Previous block - End ************************************/


/************************************** Changed block - End ************************************/

/************************************** Old block - Start *************************************
	 // split line by whitespace and punctuation characters
	 String[] words = line
	 .split("\\s|[\\.,\\?!:;'\"£$%^&\\*\\(\\)\\-\\=\\_\\+\\[\\]\\{\\}@\\#\\~\\>\\<]");
	 for (int i = 0; i < words.length; i++) {
	 String word = words[i].toLowerCase().trim();
	 if (word.length() > 0) {
	 collector.emit(new Values(word));}}
 *************************************** Old block - End ************************************/
	@Override
	public void declareOutputFields(final OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}


/************************************** AFTER SUBMISSION - Changed block - Start **********************/
private static String removeAllSpecialCharacters(String fullLine)
{
	StringBuilder stringBuilder = new StringBuilder();

	for (int i = 0; i < fullLine.length(); i++)
	{

		// Condition below allows only letters and spaces
		if (Character.isLetter(fullLine.charAt(i)) || fullLine.charAt(i) == ' ')// A-Z or space
		{
			{stringBuilder.append(fullLine.charAt(i));}
		}

		/* Condition below allows: letter-hyphen-letter     (compound words, example chat-room)
		                           letter-apostrophe-letter (contractions, example You're)*/

		else if ( (fullLine.charAt(i) == '-' || Character.toString(fullLine.charAt(i)).contains("'")) && // (- OR ')
				  (i > 0 && i < fullLine.length()-1) && // NOT first and NOT last character in the line)}
			      Character.isLetter(fullLine.charAt(i-1)) && //character BEFORE is a letter
		  		  Character.isLetter(fullLine.charAt(i+1)) ) //character AFTER is a letter

			{stringBuilder.append(fullLine.charAt(i));}

	}
	return stringBuilder.toString();
}
/************************************** AFTER SUBMISSION - Changed block - End ************************/


	/************************************** Previous block - Start ***********************************
	private static String removeAllSpecialCharacters(String string)
	{
		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);

			// Allow only letters, digits, and spaces.
			if (Character.isLetterOrDigit(ch) ||
					ch == ' ' ||
					ch == '-' ||
					Character.toString(ch).contains("'"))
			{
				stringBuilder.append(ch);
			}
		}
		return stringBuilder.toString();
	}
	************************************** Previous block - End ************************************/
}
