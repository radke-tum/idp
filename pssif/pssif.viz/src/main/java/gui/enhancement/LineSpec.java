package gui.enhancement;

import java.awt.Color;

public class LineSpec{
	Color color;
	int thickness;
	float[] type;
	
	public Color getColor() {
		return color;
	}
	public int getThickness() {
		return thickness;
	}
	public float[] getType() {
		return type;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public void setThickness(int thickness) {
		this.thickness = thickness;
	}
	public void setType(float[] type) {
		this.type = type;
	}
	
	
}