package javamagazine.artigo.jms.corporativo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.DeliveryMode;
import javax.jms.Queue;
import javax.jms.Session;

@Startup
@Singleton
public class ProdutorDoisBean {

  @Resource(mappedName = "java:/ConnectionFactory")
  private javax.jms.ConnectionFactory connectionFactory;

  @Resource(mappedName = "java:/jms/queue/FilaDois")
  private Queue fila;

  @Resource
  private TimerService timerService;

  private static DateFormat dateFormat = new SimpleDateFormat(
      "dd/MM/yyyy HH:mm:ss");

  public ProdutorDoisBean() {}

  @PostConstruct
  public void prepararAgendamento() {

    ScheduleExpression service = new ScheduleExpression();

    service.year("*");
    service.month("*");
    service.dayOfMonth("*");
    service.dayOfWeek("*");
    service.hour("*");
    service.minute("*");
    service.second("0");

    timerService.createCalendarTimer(service, new TimerConfig(null, false));
  }

  @Timeout
  public void run(Timer timer) {

    enviarMensagensTexto();

  }

  private void enviarMensagensTexto() {

    try {
      javax.jms.Connection connection = connectionFactory.createConnection();
      javax.jms.Session session = connection.createSession(false,
          Session.CLIENT_ACKNOWLEDGE);
      javax.jms.MessageProducer messageProducer = session.createProducer(fila);
      messageProducer.setPriority(9);

      javax.jms.TextMessage textMessage = session.createTextMessage();

      StringBuilder mensagem = new StringBuilder();
      mensagem.append(" Origem: ProdutorDoisBean; ");
      mensagem.append(" Data: ").append(dateFormat.format(new Date()));
      mensagem.append(" Conteúdo: ").append("Mensagem de teste!");

      textMessage.setText(mensagem.toString());
      textMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);
      textMessage.setJMSExpiration(60000);

      textMessage.setStringProperty("EXEMPLO", "EXEMPLO-DOIS");
      textMessage.setIntProperty("CODIGO", 1234);

      System.out.println("ProdutorDoisBean > Enviando mensagem para "
          + fila.getQueueName());

      messageProducer.send(textMessage);

      messageProducer.close();
      session.close();
      connection.close();
    }
    catch (Exception e) {
      /* tratar */
    }
  }

}
