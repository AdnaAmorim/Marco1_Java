package com.exa863.anselmo_adna.utils;

import org.jline.utils.AttributedString;

/*
 * Criado e desenvolvido por: Anselmo (18 de Junho de 2020)
 * Código livre / Open Source
 */
public class AsciiBox {
	
	private int size;
	
	private Boolean fix = false;
	
	private String inside = " ";
	
	private String[] borders = new String[4];
	private String[] corners = new String[0];
	
	public String render(String[] content) {
		
		Boolean haveCorners = this.corners.length > 0;
		
		String verticalBorder = this.borders[0];
		
		String border = Strings.repeat(verticalBorder, haveCorners ? this.size - 2 : this.size);
		
		String message = "\n" + (haveCorners ? this.corners[0] + border + this.corners[1] : border);
		
		for (String contentItem: content) {
			message += "\n" + centerString(contentItem); 
		}
		
		message += "\n" + (haveCorners ? this.corners[2] + border + this.corners[3] : border);
		
		return message;
		
	}
	
	public AsciiBox border(String border) {
		this.borders[0] = border;
		return this;
	}
	
	public AsciiBox borders(String vertical, String horizontal) {
		this.borders = new String[] { vertical, horizontal }; 
		return this;
	}
	
	public AsciiBox corner(String corner) {
		this.corners = new String[] { corner, corner, corner, corner };
		return this;
	}
	
	public AsciiBox corners(String topLeft, String topRight, String bottomLeft, String bottomRight) {
		this.corners = new String[] { topLeft, topRight, bottomLeft, bottomRight };
		return this;
	}
	
	public AsciiBox size(int size) {
		this.size = size;
		return this;
	}
	
	public AsciiBox inside(String inside) {
		this.inside = inside;
		return this;
	}
	
	public AsciiBox fixSide(Boolean side) {
		this.fix = side;
		return this;
	}

	private String centerString(String string) {
		int stringLenght = AttributedString.fromAnsi(string).length();
		
		int diff = this.size - 2 - stringLenght;
		if (diff < 0) diff = 0;
		
		int calc = diff / 2;
		
		String leftSpace = Strings.repeat(this.inside, calc);
		String rightSpace = Strings.repeat(this.inside, diff - calc);
		
		if (this.fix && (diff % 2 != 0)) {
			// Inverte os espaços extras caso a flag fix seja verdadeira
			String temp = leftSpace;
			leftSpace = rightSpace;
			rightSpace = temp;
		}
		
		String horizontalBorder = this.borders.length == 2 ? this.borders[1] : this.borders[0]; 
		
		return horizontalBorder + leftSpace + string + rightSpace + horizontalBorder;
	}
	
}
