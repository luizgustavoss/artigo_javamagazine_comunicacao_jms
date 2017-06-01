package javamagazine.artigo.jms.corporativo;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/topic/TopicoTres"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class ConsumidorTresMDB implements MessageListener {

  public void onMessage(Message message) {

    try {
      TextMessage msg = (TextMessage) message;
      System.out.println("ConsumidorTresMDB > Mensagem recebida: "
          + msg.getText());
    }
    catch (JMSException e) {
      /* tratar */
    }
  }
}
