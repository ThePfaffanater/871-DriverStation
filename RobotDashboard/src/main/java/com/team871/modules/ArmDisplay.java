package com.team871.modules;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.ArcType;

/**
 * @author T3Pfaffe on 2/1/2019.
 * @project DriverStation
 */
public class ArmDisplay {

    public ArmDisplay() {

    }

    private void drawArm(GraphicsContext gc, int length1, int length2, int size, double angle1, double angle2, double angle3, boolean drawLimitCircles, Paint jointFill, Paint connectorFill) {
        gc.save();

        gc.setLineDashes(5);

        double endX = length1 * Math.cos(Math.toRadians(angle1)) + length2 * Math.cos(Math.toRadians(angle1 + angle2));
        double endY = length1 * Math.sin(Math.toRadians(angle1)) + length2 * Math.sin(Math.toRadians(angle1 + angle2));

        if (drawLimitCircles) {

            gc.setStroke(Color.LIGHTGRAY);
            //System.out.println("1");
            gc.strokeLine(0, 0, endX, endY);
            //System.out.println("2");

            gc.strokeOval(-length1, -length1, length1 * 2, length1 * 2);
            gc.strokeOval(-(length1 + length2), -(length1 + length2), (length1 + length2) * 2, (length1 + length2) * 2);

            gc.setLineDashes(0);
            gc.strokeOval(-(length1 + length2 + size), -(length1 + length2 + size), (length1 + length2 + size) * 2, (length1 + length2 + size) * 2);

            gc.setLineDashes(5);
            gc.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
            gc.rotate(angle1);

            gc.strokeOval(-length2, -length2, length2 * 2, length2 * 2);

            gc.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

            gc.strokeOval(-size, -size, size * 2, size * 2);
        }

        gc.restore();

        //System.out.println(lastA1);


        gc.setStroke(Color.BLACK);


        gc.setStroke(connectorFill);
        gc.setLineWidth(size);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1)), (size / 2) * Math.sin(Math.toRadians(angle1)), (length1 - size / 2) * Math.cos(Math.toRadians(angle1)), (length1 - size / 2) * Math.sin(Math.toRadians(angle1)));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 + 90)), (size / 2) * Math.sin(Math.toRadians(angle1 + 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 + 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 + 90)));
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle1 - 90)), (size / 2) * Math.sin(Math.toRadians(angle1 - 90)), length1 * Math.cos(Math.toRadians(angle1)) + (size / 2) * Math.cos(Math.toRadians(angle1 - 90)), length1 * Math.sin(Math.toRadians(angle1)) + (size / 2) * Math.sin(Math.toRadians(angle1 - 90)));

        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);
        gc.setFill(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);

        gc.save();
        gc.translate(length1 * Math.cos(Math.toRadians(angle1)), length1 * Math.sin(Math.toRadians(angle1)));
        gc.rotate(angle1);

        gc.setStroke(Color.BLACK);


        gc.setStroke(connectorFill);
        gc.setLineWidth(size);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2)), (size / 2) * Math.sin(Math.toRadians(angle2)), (length2 - size / 2) * Math.cos(Math.toRadians(angle2)), (length2 - size / 2) * Math.sin(Math.toRadians(angle2)));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 + 90)), (size / 2) * Math.sin(Math.toRadians(angle2 + 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 + 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 + 90)));
        gc.strokeLine((size / 2) * Math.cos(Math.toRadians(angle2 - 90)), (size / 2) * Math.sin(Math.toRadians(angle2 - 90)), length2 * Math.cos(Math.toRadians(angle2)) + (size / 2) * Math.cos(Math.toRadians(angle2 - 90)), length1 * Math.sin(Math.toRadians(angle2)) + (size / 2) * Math.sin(Math.toRadians(angle2 - 90)));

        gc.setStroke(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);

        gc.translate(length2 * Math.cos(Math.toRadians(angle2)), length2 * Math.sin(Math.toRadians(angle2)));

        gc.setStroke(Color.BLACK);
        gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.setFill(jointFill);
        gc.fillOval(-size / 2, -size / 2, size, size);

        gc.rotate(-angle1 + angle3);
        gc.translate(20, 0);

        gc.strokeArc(-size / 2, -size / 2, size, size, 180 - 90, 180, ArcType.OPEN);

        //gc.strokeOval(-size / 2, -size / 2, size, size);
        gc.restore();
        //gc.restore();

    }
}
