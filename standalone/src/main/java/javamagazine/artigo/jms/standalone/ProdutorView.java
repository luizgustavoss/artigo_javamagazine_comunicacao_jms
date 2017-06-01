package javamagazine.artigo.jms.standalone;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ProdutorView extends JFrame {

  private static final long serialVersionUID = 4719118020713564707L;

  private static ProdutorDeMensagens produtor = null;

  private javax.swing.JButton btnEnviarMensagem;
  private javax.swing.JLabel lblMensagem;
  private javax.swing.JScrollPane scrollMensagem;
  private javax.swing.JTextArea txtAreaMensagem;

  /**
   * Construtor
   */
  public ProdutorView() {

    inicializarComponentes();
    configurarListenerBotao();

    addWindowListener(new WindowAdapter() {

      public void windowClosing(WindowEvent e) {

        if (produtor != null) {
          produtor.finalizar();
        }
      }

    });

    setTitle("Produtor Exemplo Um");
    setSize(570, 340);
    setResizable(false);
    setLocationRelativeTo(null);

    try {
      produtor = new ProdutorUm();
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(null,
          "Falha ao criar produtor de mensagens!", "Falha",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Inicialização da aplicação.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {

          ProdutorView frame = new ProdutorView();
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

    btnEnviarMensagem = new javax.swing.JButton();
    lblMensagem = new javax.swing.JLabel();
    scrollMensagem = new javax.swing.JScrollPane();
    txtAreaMensagem = new javax.swing.JTextArea();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    getContentPane().setLayout(null);

    btnEnviarMensagem.setText("Enviar Mensagem");

    getContentPane().add(btnEnviarMensagem);
    btnEnviarMensagem.setBounds(180, 260, 200, 30);

    lblMensagem.setText("Mensagem:");
    getContentPane().add(lblMensagem);
    lblMensagem.setBounds(20, 20, 370, 20);

    txtAreaMensagem.setColumns(20);
    txtAreaMensagem.setLineWrap(true);
    txtAreaMensagem.setRows(5);
    txtAreaMensagem.setWrapStyleWord(true);
    scrollMensagem.setViewportView(txtAreaMensagem);

    getContentPane().add(scrollMensagem);
    scrollMensagem.setBounds(20, 60, 530, 190);

    pack();
  }

  /**
   * Configuração do listener para o botão
   */
  private void configurarListenerBotao() {

    btnEnviarMensagem.addActionListener(new ButtonListener());

  }

  /**
   * Listener de eventos do botão
   */
  private class ButtonListener implements ActionListener {

    public void actionPerformed(ActionEvent e) {

      String mensagem = txtAreaMensagem.getText();
      txtAreaMensagem.setText("");

      try {

        if (produtor != null) {
          if (mensagem != null && !"".equals(mensagem.trim())) {
            produtor.produzirMensagem(mensagem);
          }
        }
        else {
          JOptionPane.showMessageDialog(null, "Não há produtor de mensagem!",
              "Falha", JOptionPane.ERROR_MESSAGE);
        }
      }
      catch (Exception e1) {
        JOptionPane.showMessageDialog(null, "Falha no envio da mensagem!",
            "Falha", JOptionPane.ERROR_MESSAGE);
      }
    }
  }
}
