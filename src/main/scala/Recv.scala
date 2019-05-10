import com.rabbitmq.client.{ConnectionFactory, DeliverCallback}

object Recv {

  def main(argv: Array[String]) {
    // NOTICE
    println (" [*****] To use this program, please change all values in Tools class with your own rabbitMq instance [*****]")

    val QUEUE_NAME = "MovieQueue"
    val factory = new ConnectionFactory()

    factory.setHost(Tools.RABBIT_MQ_HOST_NAME)
    factory.setVirtualHost(Tools.RABBIT_MQ_VIRTUAL_HOST)
    factory.setPort(Tools.RABBIT_MQ_HOST_PORT)
    factory.setUsername(Tools.RABBIT_MQ_USER_NAME)
    factory.setPassword(Tools.RABBIT_MQ_PASSWORD)

    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    println(" [*] Waiting for messages. To exit press CTRL+C")
    val deliverCallback: DeliverCallback = (_, delivery) => {
      val message = new String(delivery.getBody, "UTF-8")
      println(" [x] Received '" + message + "'")
    }
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, _ => { })
  }
}