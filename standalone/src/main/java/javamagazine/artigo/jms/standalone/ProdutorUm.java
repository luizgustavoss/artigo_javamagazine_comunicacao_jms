package javamagazine.artigo.jms.standalone;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ProdutorUm implements ProdutorDeMensagens {

  private ConnectionFactory connectionFactory;
  private Queue fila;
  private Connection connection = null;

  private static DateFormat dateFormat = new SimpleDateFormat(
      "dd/MM/yyyy HH:mm:ss");

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ProdutorUm() throws NamingException, JMSException {

    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,
        "org.jboss.naming.remote.client.InitialContextFactory");
    env.put(Context.PROVIDER_URL, "remote://localhost:4447");

    Context jndiContext = new InitialContext(env);
    connectionFactory = (ConnectionFactory) jndiContext
        .lookup("jms/RemoteConnectionFactory");
    fila = (Queue) jndiContext.lookup("jms/queue/FilaUm");
    connection = connectionFactory.createConnection();
  }

  public void finalizar() {
    try {
      /* Finalizando produtor de mensagens... */
      connection.close();
    }
    catch (JMSException e) {
      /* tratar */
    }
  }

  public void produzirMensagem(String mensagem) {

    MessageProducer messageProducer;
    TextMessage textMessage;

    try {

      Session session = connection.createSession(false,
          Session.AUTO_ACKNOWLEDGE);
      messageProducer = session.createProducer(fila);
      textMessage = session.createTextMessage();

      StringBuilder msg = new StringBuilder();
      msg.append(" Origem: ProdutorUm;");
      msg.append(" Data: ").append(dateFormat.format(new Date())).append(";");
      msg.append(" Conteúdo: ").append(mensagem);

      textMessage.setText(msg.toString());

      System.out.println("ProdutorUm > " + fila.getQueueName() + " ["
          + msg.toString() + " ] ");

      messageProducer.send(textMessage);
      messageProducer.close();
      session.close();

    }
    catch (Exception e) {
      /* tratar */
    }
  }

}