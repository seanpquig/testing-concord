package com.concord.kafka

import java.util
import java.util.concurrent.ConcurrentLinkedQueue

import com.concord.swift.Record
import com.concord._


/**
 * Operator that consumes from Kafka
 */
class ConcordKafkaConsumer extends Computation {

  val queue = new ConcurrentLinkedQueue[String]()

  val numThreads = 5
  val consumerGroup = new ConsumerGroup("nginx_analytics_access_log", queue)
  consumerGroup.run(5)

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
    var recordsRead = 0
    var record: String = queue.poll()

    while(record != null) {
      recordsRead += 1
      ctx.produceRecord("kafka-stream".getBytes(), record.getBytes(), record.getBytes())

      record = queue.poll()
    }

    println(s"RECORDS READ: $recordsRead")
    ctx.setTimer(key, System.currentTimeMillis() + 1000)
  }

  @Override
  def metadata(): Metadata = {
    new Metadata("kafka-source", new util.HashSet[StreamTuple](),
      new util.HashSet[String](util.Arrays.asList("kafka")));
  }

}

object ConcordKafkaConsumer {
  def main(args: Array[String]) = {
    ServeComputation.serve(new ConcordKafkaConsumer())
  }
}
