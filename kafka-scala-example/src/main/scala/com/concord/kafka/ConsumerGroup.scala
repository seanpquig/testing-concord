package com.concord.kafka

import java.util.Properties
import java.util.concurrent.{ConcurrentLinkedQueue, Executors, TimeUnit, ExecutorService}

import kafka.consumer.{KafkaStream, ConsumerConfig, ConsumerConnector}

/**
 * Created by squigley on 10/24/15.
 */
class ConsumerGroup(topic: String, intermediateQueue: ConcurrentLinkedQueue[String]) {
  private val groupId = "Concord-consumer"
  private val consumer: ConsumerConnector = kafka.consumer.Consumer.create(buildConsumerConfig(groupId))
  private var executor: ExecutorService = null

  def buildConsumerConfig(groupId: String): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", System.getenv("ZOOKEEPER_SERVERS"))
    props.put("group.id", groupId)
    new ConsumerConfig(props)
  }

  def run(numThreads: Int) = {
    val topicThreadMap = Map[String, Int](topic -> numThreads)
    val consumerMap = consumer.createMessageStreams(topicThreadMap)
    val streams = consumerMap.get(topic)

    executor = Executors.newFixedThreadPool(numThreads)

    streams.get.zipWithIndex.foreach {
      case (stream: KafkaStream[Array[Byte], Array[Byte]], thread: Int) =>
        executor.submit(new ConsumerThread(stream, thread, intermediateQueue))
    }
  }

  def shutDown() = {
    consumer.shutdown()
    if (executor != null) executor.shutdown()
    try {
      if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
        System.out.println("Timed out waiting for consumer threads to shut down, exiting uncleanly");
      }
    } catch {
      case e: InterruptedException =>
        System.out.println("Interrupted during shutdown, exiting uncleanly");
    }
  }

}
