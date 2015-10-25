package com.concord.kafka

import java.util.concurrent.ConcurrentLinkedQueue

import kafka.consumer.KafkaStream

/**
 * Created by squigley on 10/24/15.
 */
class ConsumerThread(
    kafkaStream: KafkaStream[Array[Byte], Array[Byte]],
    threadNum: Int,
    intermediateQueue: ConcurrentLinkedQueue[String]) extends Runnable
{

  def run() = {
    val it = kafkaStream.iterator()
    while (it.hasNext()) {
      val avroMsg = it.next().message().toString
      intermediateQueue.add(avroMsg)
    }

  }

}
