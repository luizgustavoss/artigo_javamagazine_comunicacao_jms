package javamagazine.artigo.jms.standalone;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ConsumidorDois implements ConsumidorDeMensagens {

  private static List<Observador> observadores;

  private ConnectionFactory connectionFactory;
  private Queue fila;
  private Connection connection;
  private MessageConsumer messageConsumer;
  private Session session;

  @SuppressWarnings({ "rawtypes", "unchecked" })
  public ConsumidorDois() throws NamingException, JMSException {

    Hashtable env = new Hashtable();
    env.put(Context.INITIAL_CONTEXT_FACTORY,
        "org.jboss.naming.remote.client.InitialContextFactory");
    env.put(Context.PROVIDER_URL, "remote://localhost:4447");

    Context jndiContext = new InitialContext(env);
    connectionFactory = (ConnectionFactory) jndiContext
        .lookup("jms/RemoteConnectionFactory");
    fila = (Queue) jndiContext.lookup("jms/queue/FilaDois");
    connection = connectionFactory.createConnection();

    observadores = new ArrayList<Observador>();
  }

  public void finalizar() {
    try {
      /* Finalizando consumidor de mensagens... */
      messageConsumer.close();
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

      String seletor = "EXEMPLO = 'EXEMPLO-DOIS' AND CODIGO = 1234";

      messageConsumer = session.createConsumer(fila, seletor);
      connection.start();

      new Thread(new Runnable() {
        public void run() {

          boolean continuar = true;

          do {

            try {
              /* Aguardando mensagens... */
              Message msg = messageConsumer.receive();

              if (msg != null) {
                TextMessage textMessage = (TextMessage) msg;
                String mensagem = textMessage.getText();
                System.out.println("ConsumidorDois < " + fila.getQueueName()
                    + "[" + mensagem + "]");
                notificarObservadores(mensagem);
              }
            }
            catch (Exception e) {
              continuar = false;
              /* Cancelando recebimento pela thread... */
            }
          } while (continuar);
          /* Encerrando thread... */
        }
      }).start();

    }
    catch (JMSException e) {
      /* tratar */
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
