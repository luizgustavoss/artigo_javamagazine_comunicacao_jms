package javamagazine.artigo.jms.standalone;

public interface ProdutorDeMensagens {
  
  void finalizar();
  
  void produzirMensagem(String mensagem);

}
