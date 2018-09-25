//Clarissa Simoyama David - RA: 21015712

package jukebox
import scala.collection.mutable.Map
import scala.collection.mutable.Queue
import akka.actor._
import javax.swing.JOptionPane

object Jukebox {
  
  //classes que serão utilizadas para os atores:
  //Pedido: recebe o pedido do cliente com o dinheiro atribuido,
  //repassando para a seleção
  //Selecionar: coloca a música escolhida na fila e repassa para tocar
  //Tocar: "toca" a música escolhida, imprimindo alguns versos dela
  case class Selecionar(numMusica: Int)
  case class Tocar(numMusica: Int, musica: Option[String])
  case class Pedido(numMusica: Int, dinheiro: Double)
  
  //ator cuja função é guarda a playlist das músicas, enfileirar músicas pedidas
  //e repassar para que elas possam ser tocadas
  class Controlador extends Actor{
    //playlist com algumas músicas que poderão ser escolhidas
    private val playlist = Map[Int, String]()
    playlist += (1 -> "Queen - Bohemian Rhapsody")
    playlist += (2 -> "Pink Floyd - Another Brick In The Wall")
    playlist += (3 -> "Beatles - All You Need Is Love")
    playlist += (4 -> "Black Sabbath - I Won't Cry For You")
    playlist += (5 -> "Wesley Safadão - Camarote")
    val queue = Queue[Int]()
    def receive = {
      case Selecionar(m) => queue.enqueue(m)
        sender() ! Tocar(queue.dequeue, playlist.get(m))
    }
  }
  
  //ator que possui a função de receber os pedidos dos clientes, repassando para
  //o controlador, e recebendo novamente a música que está na fila, tocando-a
  class Tocador(servidor: ActorRef) extends Actor{
    //listas com alguns versos das músicas
    private val bohemian = List("Is this the real life?", "Is this just fantasy?")
    private val brick = List("All in all you're just", "Another brick in the wall")
    private val beatles = List("All you need is love, love", "Love is all you need")
    private val black = List("So you lie awake and think about tomorrow", "And you try to justify the things you've done")
    private val camarote = List("Agora assista aí de camarote", "Eu bebendo gela, tomando Cîroc")
    
    def receive = {
      case Pedido(t, d) => if(d == 0.5) servidor ! Selecionar(t) else println("Valor incorreto")
      case Tocar(m, r @ Some(t)) => println(s"$m - $t")      
        if(m == 1) bohemian.foreach{println}
        else if(m == 2) brick.foreach{println}
        else if(m == 3) beatles.foreach{println}
        else if(m == 4) black.foreach{println}
        else camarote.foreach{println}
        
    }
  }
  
  //metodo principal, onde os clientes escolhem a música
  def main(args: Array[String]): Unit = {    
    JOptionPane.showMessageDialog(null, "1 - Queen - Bohemian Rhapsody\n2 - Pink Floyd - Another Brick In The Wall\n" +
        "3 - Beatles - All You Need Is Love\n4 - Black Sabbath - I Won't Cry For You\n5 - Wesley Safadão - Camarote")
 
    val m1 = JOptionPane.showInputDialog("Escolha a música")
    val musica1 = m1.toInt
    val m2 = JOptionPane.showInputDialog("Escolha a música")
    val musica2 = m2.toInt
    val m3 = JOptionPane.showInputDialog("Escolha a música")
    val musica3 = m3.toInt
    val m4 = JOptionPane.showInputDialog("Escolha a música")
    val musica4 = m4.toInt
    val m5 = JOptionPane.showInputDialog("Escolha a música")
    val musica5 = m5.toInt
    
    val system = ActorSystem("Jukebox")
    val servidor = system.actorOf(Props[Controlador])
    val cliente1 = system.actorOf(Props(classOf[Tocador], servidor))
    val cliente2 = system.actorOf(Props(classOf[Tocador], servidor))
    val cliente3 = system.actorOf(Props(classOf[Tocador], servidor))
    val cliente4 = system.actorOf(Props(classOf[Tocador], servidor))
    val cliente5 = system.actorOf(Props(classOf[Tocador], servidor))
    
    //cliente1 ! Pedido(musica1, 0.2)
    cliente1 ! Pedido(musica1, 0.5)
    Thread.sleep(2000)
    cliente2 ! Pedido(musica2, 0.5)
    Thread.sleep(2000)
    cliente3 ! Pedido(musica3, 0.5)
    Thread.sleep(2000)
    cliente4 ! Pedido(musica4, 0.5)
    Thread.sleep(2000)
    cliente5 ! Pedido(musica5, 0.5)
    
    
    system.shutdown()
 }
  
 
}