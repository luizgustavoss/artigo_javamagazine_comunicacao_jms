package javamagazine.artigo.jms.standalone;

public interface ConsumidorDeMensagens extends Observado {

  void finalizar();

  void receberMensagens();
}
