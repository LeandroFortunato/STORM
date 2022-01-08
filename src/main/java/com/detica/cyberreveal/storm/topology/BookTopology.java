package com.detica.cyberreveal.storm.topology;

import java.io.File;
import java.io.IOException;

import backtype.storm.tuple.Fields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

import com.detica.cyberreveal.storm.bolt.FilePrinterBolt;
import com.detica.cyberreveal.storm.bolt.PrinterBolt;
import com.detica.cyberreveal.storm.bolt.WordCountBolt;
import com.detica.cyberreveal.storm.bolt.WordSplitBolt;
import com.detica.cyberreveal.storm.spout.BookLineSpout;

/**
 * The Class TestTopology.
 */
public final class BookTopology implements Runnable {

	private final String topologyName;
	private static final Logger LOG = LoggerFactory
			.getLogger(BookTopology.class);
	private final String inputFile;
	private final File wordCountOutputFile;

	/**
	 * Instantiates a new book topology.
	 * 
	 * @param topologyName
	 *            the topology name
	 */
	public BookTopology(String topologyName, String inputFile,
			File wordCountOutputFile) {
		this.topologyName = topologyName;
		this.inputFile = inputFile;
		this.wordCountOutputFile = wordCountOutputFile;

	}

	@Override
	public void run() {
		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("line", new BookLineSpout());

		builder.setBolt("wordSplitter", new WordSplitBolt(),2)
				.shuffleGrouping("line");

		/************************************** Changed block - Start *******************************
		 Change 1: stream groupings of wordCount bolt was switched from shuffleGrouping to fieldsGrouping

		 Problem: shuffleGrouping is establishing even distribution of the tuples coming from wordSplitter
		 		  bolt (previous bolt) among the word count bolt parallels (This flow can be seen
		          on read.me file).  By doing that, more than 1 parallel of the word count bolt ends up
		          receiving the same word and establishing its own count for that specific word.

		 Solution: By using fields grouping, we tell storm to send tuples that have the same value to be
	          	   counted by only one single parallel of the word count bolt, providing the correct
		           global count  for that word.

		 Change 2: stream groupings of printWordCount bolt was switched from shuffleGrouping to globalGrouping

		 Reason: In order to have a consolidation for all tuples originated from word count bolt (containing
		 		 total counts of all words) we have to forward these tuples to only 1 task of
		         printWordCount bolt. That can be achieved by using globalGrouping.
		 ********************************************************************************************/
		 builder.setBolt("wordCount", new WordCountBolt(),2).fieldsGrouping(
				"wordSplitter", new Fields("word"));

		 builder.setBolt("printWordCount", new PrinterBolt(),2)
				.globalGrouping("wordCount");
		 /************************************** Changed block - End ************************************/

		 /************************************** Old block - Start   ************************************
		 builder.setBolt("wordCount", new WordCountBolt(), 2).shuffleGrouping(
		 "wordSplitter");

		 builder.setBolt("printWordCount", new PrinterBolt(), 2)
		 .shuffleGrouping("wordCount");
		 *************************************** Old block - End **************************************/

		try {
			builder.setBolt("printWordCountToFile",
							new FilePrinterBolt(this.wordCountOutputFile),2)
					.shuffleGrouping("wordCount");
		} catch (IOException e) {
			e.printStackTrace();
		}

		Config conf = new Config();

		conf.setDebug(true);
		conf.put("inputFile", this.inputFile);

		if (this.topologyName != null) {
			conf.setNumWorkers(20);

			try {
				StormSubmitter.submitTopology(this.topologyName, conf,
						builder.createTopology());
			} catch (AlreadyAliveException e) {
				LOG.error("Error submitting topology");
			} catch (InvalidTopologyException e) {
				LOG.error("Error submitting topology", e);
			}
		} else {


			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("test", conf, builder.createTopology());


			/************************************** AFTER SUBMISSION - Changed block - Start **********************/
			Utils.sleep(40000);
			/************************************** AFTER SUBMISSION - Changed block - End ************************/

			/************************************** AFTER SUBMISSION - Old block - Start **************************
 			Utils.sleep(10000);
			***************************************** AFTER SUBMISSION - Old block - End *************************/

			cluster.killTopology("test");
			cluster.shutdown();
		}
	}
}
