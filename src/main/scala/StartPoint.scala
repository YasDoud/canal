import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Sink}
import akka.util.ByteString
import com.rabbitmq.client.ConnectionFactory

import scala.concurrent.ExecutionContext


object StartPoint extends App {


  override def main(args: Array[String]): Unit = {

    // NOTICE
    println (" [*****] To use this program, please change all values in Tools class with your own rabbitMq instance [*****]")

    /*
      implicit for reading file ...
     */
    implicit val system = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContext = ExecutionContext.global
    val QUEUE_NAME = "MovieQueue"
    /*
      Queue ...
     */
    println (s"starting open connection and channel queue : $QUEUE_NAME")
    val factory = new ConnectionFactory()
    factory.setHost(Tools.RABBIT_MQ_HOST_NAME)
    factory.setVirtualHost(Tools.RABBIT_MQ_VIRTUAL_HOST)
    factory.setPort(Tools.RABBIT_MQ_HOST_PORT)
    factory.setUsername(Tools.RABBIT_MQ_USER_NAME)
    factory.setPassword(Tools.RABBIT_MQ_PASSWORD)
    val connection = factory.newConnection()
    val channel = connection.createChannel()
    channel.queueDeclare(QUEUE_NAME, false, false, false, null)
    println ("channel has been opened !")

    FileIO.fromPath(Paths.get("src/main/resources/data.tsv"))
        .via(Framing.delimiter(ByteString("\n"), 256, true).map(_.utf8String))
        .map(x => MovieBean(x.toString))
        .runForeach (x => {
          // check MoveBiean type and category
          if ("Comedy".equalsIgnoreCase(x.genres) && "movie".equalsIgnoreCase(x.titleType))
          {
            // sending message
            println (s"sending message .... $x")

            channel.basicPublish("", QUEUE_NAME, null, x.toString.getBytes("UTF-8"))

            println(s" [x] Sent '$x'")
          }
        })

    channel.close()
    connection.close()



  }

}
