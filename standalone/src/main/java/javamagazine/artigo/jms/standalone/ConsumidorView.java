package javamagazine.artigo.jms.standalone;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ConsumidorView extends JFrame implements Observador {

  private static final long serialVersionUID = -3385261071487483160L;

  private static ConsumidorDeMensagens consumidor;

  private javax.swing.JButton btnEnviarMensagem;
  private javax.swing.JLabel lblMensagens;
  private javax.swing.JLabel lblConsumidor;
  private javax.swing.JComboBox comboConsumidor;
  private javax.swing.JScrollPane scrollMensagens;
  private javax.swing.JTextArea txtAreaMensagens;

  /**
   * Construtor
   */
  public ConsumidorView() {

    inicializarComponentes();
    configurarListenerBotao();

    addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {

        if (consumidor != null) {
          consumidor.finalizar();
        }
      }

    });

    setTitle("Consumidor de Mensagens");
    setSize(570, 340);
    setResizable(false);
    setLocationRelativeTo(null);

  }

  /**
   * Inicialização da aplicação.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {

          ConsumidorView frame = new ConsumidorView();
          frame.setVisible(true);

        }
        catch (Exception e) {
          /* tratar */
        }
      }
    });
  }

  /**
   * Inicialização dos componentes
   */
  private void inicializarComponentes() {

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    btnEnviarMensagem = new javax.swing.JButton();
    lblMensagens = new javax.swing.JLabel();
    lblConsumidor = new javax.swing.JLabel();
    scrollMensagens = new javax.swing.JScrollPane();
    txtAreaMensagens = new javax.swing.JTextArea();

    String[] consumidores = new String[] { "Exemplo Um", "Exemplo Dois",
        "Exemplo Três" };

    comboConsumidor = new javax.swing.JComboBox(consumidores);

    lblConsumidor.setText("Consumidor:");
    getContentPane().add(lblConsumidor);
    lblConsumidor.setBounds(20, 10, 370, 20);

    getContentPane().add(comboConsumidor);
    comboConsumidor.setBounds(20, 30, 370, 20);

    lblMensagens.setText("Mensagens Recebidas:");
    getContentPane().add(lblMensagens);
    lblMensagens.setBounds(20, 60, 370, 20);

    txtAreaMensagens.setColumns(20);
    txtAreaMensagens.setLineWrap(false);
    txtAreaMensagens.setRows(4);
    txtAreaMensagens.setEditable(false);
    scrollMensagens.setViewportView(txtAreaMensagens);

    getContentPane().add(scrollMensagens);
    scrollMensagens.setBounds(20, 90, 530, 160);

    btnEnviarMensagem.setText("Receber Mensagens");
    getContentPane().add(btnEnviarMensagem);
    btnEnviarMensagem.setBounds(180, 260, 200, 30);

    pack();
  }

  /**
   * Configuração do listener para o botão
   */
  private void configurarListenerBotao() {

    btnEnviarMensagem.addActionListener(new ButtonListener());

  }

  public void consumirMensagens(String consumidorSelecionado)
      throws NamingException, JMSException
  {
    
    if ("Exemplo Um".equals(consumidorSelecionado)) {
      consumidor = new ConsumidorUm();
    }
    else if ("Exemplo Dois".equals(consumidorSelecionado)) {
      consumidor = new ConsumidorDois();
    }
    else if ("Exemplo Três".equals(consumidorSelecionado)) {
      consumidor = new ConsumidorTres();
    }

    consumidor.adicionarObservadorMensagem(this);
    consumidor.receberMensagens();

  }

  /**
   * Implementação do método da interface de notificação.
   */
  public void notificarRecebimentoMensagem(String mensagem) {

    txtAreaMensagens.append(mensagem);
    txtAreaMensagens.append("\n");

  }

  /**
   * Listener de eventos do botão
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent evt) {

      try {

        String consumidorSelecionado = comboConsumidor.getSelectedItem()
            .toString();
        consumirMensagens(consumidorSelecionado);
        btnEnviarMensagem.setEnabled(false);
        comboConsumidor.setEnabled(false);
      }
      catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Falha ao criar consumidor de mensagens!", "Falha",
            JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
