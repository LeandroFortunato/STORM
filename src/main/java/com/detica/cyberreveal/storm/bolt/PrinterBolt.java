package com.detica.cyberreveal.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.*;

/**
 * A Storm Bolt which prints all received tuples to System.out
 */
public class PrinterBolt extends BaseBasicBolt {

	private static final long serialVersionUID = -5237229359039158290L;

	/************************************** Changed block - Start ************************************
 	Reason: Creation of an additional feature that prints on the console (on descending order)
	        the total number of occurrences found for each word that exists in the input file.
	        In order to do that, a Hashmap was created to accommodate the final counts of all
	        words, which is eventually printed (inside the cleanup method, below this page),
	        just before the bolt is terminated.*/

	private final Map<String, Long> counts = new HashMap<String, Long>();

	@Override
	public void execute(final Tuple tuple, final BasicOutputCollector collector) {

		String word = tuple.getStringByField("word");
		Long count = tuple.getLongByField("count");
		this.counts.put(word, count);
	}/************************************** Changed block - End ************************************/

	/************************************** Old block - Start   ************************************
	 @Override
	 public void execute(final Tuple tuple, final BasicOutputCollector collector) {
	 System.out.println(tuple);}
	 *************************************** Old block - End **************************************/


	/************************************** New block - Start ***********************************/
	@Override
	public void cleanup() {

		if (counts.size()!=0){
			System.out.println("--- COUNTS BY WORD / DESCENDING ORDER---");

			this.counts.entrySet().stream()
					.sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
					.forEach(k -> System.out.println(k.getKey() + ": " + k.getValue()));
				System.out.println("--------------");
		};
	}/************************************** New block - End ************************************/

	@Override
	public void declareOutputFields(final OutputFieldsDeclarer declarer) {

	}

}
