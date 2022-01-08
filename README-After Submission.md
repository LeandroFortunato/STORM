(Main) Correction of message
----------------------------
Correction of error message for IOException.


(BookTopology) Increase of timeout connection period
----------------------------------------------------
Problem:
EndOfStreamException: Unable to read additional data from client, likely client has closed socket.

Cause: Zookeeper does not finish reading the consumer data and the connection is disconnected by the consumer.
Timeout period was too short (10.000 ms).

Solution:
Increase of timeout connection value to (40.000) ms


(WordSplitBolt) Word filtering correction
------------------------------------------
Problem: Representation of all words in small letters (not to separately count
words like "The" and "the") was causing proper names to be wrongfully
transformed (e.g. "Steve" into "steve").

Solution: Representation of all words using capital letters. By doing that,
words like "The" and "the" will be correctly counted as "THE" and proper names
will be fully written in capitals (e.g. "Steve" into "STEVE"), which
is also acceptable.

Other corrections:

1) Identification and preservation of hyphen for compound words (example: chat-room).
2) Identification and preservation of apostrophe for contractions (example: I'm, he'd).
3) Removal of all Roman Numerals, but "I" from the text ("I" can also be identified as a pronoun).
