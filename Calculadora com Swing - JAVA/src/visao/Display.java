package br.com.clayton.calc.visao;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.clayton.calc.modelo.Memoria;
import br.com.clayton.calc.modelo.MemoriaObservador;

@SuppressWarnings("serial")
public class Display  extends JPanel  implements MemoriaObservador { // JPanel trabalha como um container, é um componente que agrupa outros componentes 

	private final JLabel label; 
	
	public Display() {	
		Memoria.getInstancia().adicionarObservador(this); //sempre que alterar o valor este método irá ser chamado
		
		setBackground(new Color(46, 49, 50));		
		label = new JLabel(Memoria.getInstancia().getTextoAtual());
		label.setForeground(Color.WHITE);
		label.setFont(new Font("courier", Font.PLAIN, 30));
		
		setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 25));
		
		add(label);
	}
	
	public void valorAlterado(String novoValor) { //sempre que houver uma mudança na memoria essa mudança será notificada
		label.setText(novoValor);				   // a quem estiver interessado que neste caso é o display

	}
}
