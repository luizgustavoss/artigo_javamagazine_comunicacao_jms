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
import javax.jms.Session;
import javax.jms.Topic;

@Startup
@Singleton
public class ProdutorTresBean {

  @Resource(mappedName = "java:/ConnectionFactory")
  private javax.jms.ConnectionFactory connectionFactory;

  @Resource(mappedName = "java:/jms/topic/TopicoTres")
  private Topic topico;

  @Resource
  private TimerService timerService;

  private static DateFormat dateFormat = new SimpleDateFormat(
      "dd/MM/yyyy HH:mm:ss");

  public ProdutorTresBean() {}

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
      javax.jms.MessageProducer messageProducer = session
          .createProducer(topico);

      javax.jms.TextMessage textMessage = session.createTextMessage();

      StringBuilder mensagem = new StringBuilder();
      mensagem.append(" Origem: ProdutorTresBean; ");
      mensagem.append(" Data: ").append(dateFormat.format(new Date()));
      mensagem.append(" Conteúdo: ").append("Mensagem de teste!");

      textMessage.setText(mensagem.toString());
      textMessage.setJMSDeliveryMode(DeliveryMode.PERSISTENT);
      textMessage.setJMSExpiration(0);

      System.out.println("ProdutorTresBean > Enviando mensagem para "
          + topico.getTopicName());

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
