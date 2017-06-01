package javamagazine.artigo.jms.standalone;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumidorTres implements ConsumidorDeMensagens {

  private static List<Observador> observadores;

  private ConnectionFactory connectionFactory;
  private Topic topico;
  private Connection connection;
  private TopicSubscriber subscriber;
  private Session session;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ConsumidorTres() throws NamingException, JMSException {

    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,
        "org.jboss.naming.remote.client.InitialContextFactory");
    env.put(Context.PROVIDER_URL, "remote://localhost:4447");

    Context jndiContext = new InitialContext(env);
    connectionFactory = (ConnectionFactory) jndiContext
        .lookup("jms/RemoteConnectionFactory");
    topico = (Topic) jndiContext.lookup("jms/topic/TopicoTres");
    connection = connectionFactory.createConnection();
    connection.setClientID("IdExemploTres");

    observadores = new ArrayList<Observador>();
  }

  public void finalizar() {
    try {
      /* Finalizando consumidor de mensagens... */
      subscriber.close();
      session.close();
      connection.close();
    }
    catch (JMSException e) {
      /* tratar */
    }
  }

  public void receberMensagens() {

    try {

      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      subscriber = session.createDurableSubscriber(topico, "IdExemploTres");
      subscriber.setMessageListener(new ListenerExemploTres());
      connection.start();

    }
    catch (JMSException e) {
      /* tratar */
    }
  }

  private class ListenerExemploTres implements MessageListener {

    public void onMessage(Message message) {

      try {
        if (message != null) {

          TextMessage textMessage = (TextMessage) message;

          String mensagem = textMessage.getText();

          System.out.println("ConsumidorTres < " + topico.getTopicName() + "["
              + mensagem + "]");

          notificarObservadores(mensagem);

        }
      }
      catch (JMSException e) {
        /* tratar */
      }
    }
  }

  private void notificarObservadores(String mensagem) {

    for (Observador observador : observadores) {
      /* Notificando observador... */
      observador.notificarRecebimentoMensagem(mensagem);
    }
  }

  public void adicionarObservadorMensagem(Observador observador) {

    observadores.add(observador);

  }

}
