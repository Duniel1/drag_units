package de.daniel.home.drag_units_2.krap;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IFigure{
	double getWidth();
    double getHeight();
    void setX(double x);
    double getX();
    void setY(double y);
    double getY();
    int getId();
	BufferedImage getImage();
	void setImage(String image);
	void setImageReference(BufferedImage image);
	String getName();
	void setName(String name);
	
	boolean isSelected();
    void setIsSelected(boolean b);
	
	Collision checkCollision(IFigure f, double nx, double ny);
    boolean checkCollisionRight(IFigure f, double nx, double ny);
    boolean checkCollisionLeft(IFigure f, double nx, double ny);
    boolean checkCollisionTop(IFigure f, double nx, double ny);
    boolean checkCollisionBottom(IFigure f, double nx, double ny);
}
