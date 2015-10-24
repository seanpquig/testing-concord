package com.concord.kafka

import java.util

import com.concord.swift.Record
import com.concord._

import scala.util.Random


/**
 * Operator that consumes from Kafka
 */
class KafkaConsumer extends Computation {

  private val words = Array("foo", "bar", "baz", "fiz", "buzz")
  private val rand: Random = new Random()
  private def sample(): String  = {
    val idx = rand.nextInt(words.length)
    words(idx)
  }

  @Override
  def init(ctx: ComputationContext) {
    System.out.println(s"${this.getClass.getSimpleName} initialized")
    ctx.setTimer("loop", System.currentTimeMillis())
  }

  @Override
  def processRecord(ctx: ComputationContext, record: Record) {
    throw new RuntimeException("Method not implemented")
  }

  @Override
  def processTimer(ctx: ComputationContext, key: String, time: Long) {
    // Stream, key, value. Empty value, no need for val
    Range(0, 1024).foreach{ i =>
      ctx.produceRecord("words".getBytes(), sample().getBytes(), "-".getBytes())
    }

    ctx.setTimer(key, System.currentTimeMillis() + 5000)
  }

  @Override
  def metadata(): Metadata = {
    new Metadata("kafka-source", new util.HashSet[StreamTuple](),
      new util.HashSet[String](util.Arrays.asList("kafka")));
  }

}

object KafkaSource {
  def main(args: Array[String]) = {
    ServeComputation.serve(new KafkaConsumer())
  }
}
