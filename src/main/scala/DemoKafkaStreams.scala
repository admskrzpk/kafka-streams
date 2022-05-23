import java.util.Properties

object DemoKafkaStreams extends App {

  import org.apache.kafka.common.serialization.Serdes
  import org.apache.kafka.streams.{KafkaStreams, StreamsBuilder, StreamsConfig}

  val builder = new StreamsBuilder
  builder
    .stream[String, String]("topic-demo-ks")
    .mapValues((value: String) => String.valueOf(value.toUpperCase))
    .to("new-demo-topic")

  val props = new Properties()
  props.put(StreamsConfig.APPLICATION_ID_CONFIG, "demo-ks")
  props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
  props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String.getClass)
  props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String.getClass)


  val streams = new KafkaStreams(builder.build, props)
  streams.start()
}
