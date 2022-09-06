package br.com.clayton.calc.modelo;
import java.util.ArrayList;
import java.util.List;

public class Memoria {
	
	private enum TipoComando {
		ZERAR, SINAL, NUMERO, DIV, MULT, SUB, SOMA, IGUAL, VIRGULA;
	}	
	// O Padrão Singleton "tem como definição garantir que uma classe tenha apenas uma instância de si mesma e que forneça um ponto global 
	// de acesso a ela. Você contranla isso criando o construtor dessa classe como Privado"
	
	private static final Memoria instancia = new Memoria();
	
	private final List<MemoriaObservador> observadores =  // irá armazenar todos os observadores que forem cadastrados
			new ArrayList<>();
	
	private TipoComando ultimaOperacao = null;
	private boolean substituir = false;
	private String textoAtual= ""; // inicia o display sempre vazia
	private String textoBuffer = "";	
	
	private Memoria () {
		
	}	
	public static Memoria getInstancia() {
		return instancia;
	}
	
	public void adicionarObservador(MemoriaObservador observador) {  // metodo para registrar todos os observadores
		observadores.add(observador);
	}

	public String getTextoAtual() {
		return textoAtual.isEmpty() ? "0" : textoAtual;  // se o <textoAtual> for vazia aparece 0 no display
	}
	
	public void processarComando(String texto ) {
		
		TipoComando tipoComando = detectarTipoComando(texto);		
		
		if(tipoComando == null) {
			return; // se o tipo de comando for nulo ele não precisa fazer nada então retorne ao inicio 
		} else if(tipoComando == TipoComando.ZERAR) { // se o camando for zerar....
			textoAtual = "";
			textoBuffer = "";
			substituir = false;
			ultimaOperacao = null;	//.... vai ressetar todas as memorias
		} else if(tipoComando == TipoComando.SINAL && textoAtual.contains("-")) {
			textoAtual = textoAtual.substring(1);
		} else if(tipoComando == TipoComando.SINAL && !textoAtual.contains("-")) {
			textoAtual = "-" + textoAtual;
		} else if(tipoComando == TipoComando.NUMERO  // se digitar <numero> ou <virgula> e <substituir for verdadeiro então....
				|| tipoComando == TipoComando.VIRGULA) {
			textoAtual	 = 	substituir ? texto    	  :     		textoAtual + texto; 
	//....|<textoAtual> irá substituir <texto> |caso contrario| pegerá <textoAtual> + <texto>  e substituirá pelo TextoAtual  
			// texto atual será acrescentado quando <substituir> for falso e irá substituir quando <substituir> for verdadeiro 
			substituir = false;
		} else {
			substituir = true; 
			textoAtual = obterResultadoOperacao();  
			textoBuffer = textoAtual; // na próxima vez que digitar algo você vai substituir pelo texto digitado e trará o resultado atual da operação
			ultimaOperacao = tipoComando;
		}		
		observadores.forEach(o -> o.valorAlterado(getTextoAtual()));  // notifica os observadores da alteração 
	}

	private String obterResultadoOperacao() {
		if(ultimaOperacao == null || ultimaOperacao == TipoComando.IGUAL) {
			return textoAtual;
		}
		
		double numeroBuffer = Double.parseDouble(textoBuffer.replace(",", "."));
		double numeroAtual = Double.parseDouble(textoAtual.replace(",", "."));
		
		double resultado = 0;
			
		if(ultimaOperacao == TipoComando.SOMA ) {
			resultado = numeroBuffer + numeroAtual;
		} else if(ultimaOperacao == TipoComando.SUB ) {
			resultado = numeroBuffer - numeroAtual;
		} else if(ultimaOperacao == TipoComando.MULT ) {
			resultado = numeroBuffer * numeroAtual;
		} else if(ultimaOperacao == TipoComando.DIV ) {
			resultado = numeroBuffer / numeroAtual;
		}
		
		String texto = Double.toString(resultado).replace(".", ",");
		boolean inteiro = texto.endsWith(",0");
		return inteiro ? texto.replace(",0", "") : texto;
	}

	private TipoComando detectarTipoComando(String texto) {
		
		if(textoAtual.isEmpty() && texto == "0") {
			return null;
		} try {
			Integer.parseInt(texto);
			return TipoComando.NUMERO;
		} catch (NumberFormatException e) {
			// Quando não for numero...
			if("AC".equals(texto)) {
				return TipoComando.ZERAR;
			} else if ("/".equals(texto)) {
				return TipoComando.DIV;
			} else if ("*".equals(texto)) {
				return TipoComando.MULT;
			} else if ("+".equals(texto)) {
				return TipoComando.SOMA;
			} else if ("-".equals(texto)) {
				return TipoComando.SUB;
			} else if ("=".equals(texto)) {
				return TipoComando.IGUAL;
			} else if ("±".equals(texto)) {
				return TipoComando.SINAL;
			} else if (",".equals(texto)					
				&& !textoAtual.contains(",")) {
				return TipoComando.VIRGULA;				
			}			
		}
		return null;
	}

}