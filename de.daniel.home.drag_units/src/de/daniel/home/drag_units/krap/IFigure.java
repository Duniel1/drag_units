package de.daniel.home.drag_units.krap;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface IFigure{
	double getWidth();
    double getHeight();
    void setX(double x);
    double getX();
    void setY(double y);
    double getY();
    Collision checkCollision(IFigure f, double nx, double ny);
    int getId();
    boolean isSelected();
    void setIsSelected(boolean b);
	BufferedImage getImage();
	void setImage(String image) throws IOException;
}
