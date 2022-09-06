package br.com.clayton.calc.visao;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Botao extends JButton {
	
	public Botao(String texto, Color cor) {
		setText(texto);
		setOpaque(true);
		setBackground(cor);
		setFont(new Font("courier", Font.PLAIN, 20));
		setFocusPainted(false);
		setForeground(Color.WHITE);
	}	
}
