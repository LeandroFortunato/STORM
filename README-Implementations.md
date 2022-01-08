

(Main) Creation of an additional feature
----------------------------------------
- A configuration file was implemented (App.properties) to provide location/name for
the input and output files that are going to be used by the Storm Topology.


(BookTopology) Fixing and Implementation 
----------------------------------------
- Stream groupings of wordCount bolt was switched from shuffleGrouping to fieldsGrouping.

Problem: 

ShuffleGrouping is establishing even distribution of the tuples coming from wordSplitter
bolt (previous bolt) among the word count bolt parallels. By doing that, more than 1 parallel 
of the word count bolt ends up receiving the same word and establishing its own count for 
that specific word.

Solution: 

By using fields grouping, we tell storm to send tuples that have the same value to be
counted by only one single parallel of the word count bolt, providing the correct
global count  for that word.


- Stream groupings of printWordCount bolt was switched from shuffleGrouping to globalGrouping

Reason:

In order to have a consolidation for all tuples originated from word count bolt (containing
total counts of all words) we have to forward these tuples to only 1 task of
printWordCount bolt. That can be achieved by using globalGrouping.


(WordSplitBolt) Fixing and Implementation
----------------------------------------
- The method removeAllSpecialCharacters was created to eliminate special characters 
from each line, by retaining just letters, digits, apostrophes, hyphens and spaces,
rather than looking for special characters through the use of a long reference list.

Problem:

There were errors counting words due to the difference between small and capital letters. Also, 
apostrophe ('s) was recognized as a word.

Solution: 

All letters from each line were switched to lower case.


(PrinterBolt) Implementation
----------------------------------------
- Creation of an additional feature that prints on the console (on descending order)
the total number of occurrences found for each word that exists in the input file.
In order to do that, a Hashmap was created to accommodate the final counts of all
words, which is eventually printed (inside the cleanup method), just before the bolt
is terminated.