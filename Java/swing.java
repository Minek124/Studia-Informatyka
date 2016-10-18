
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;
import javax.swing.*;

class MyJPanel extends JPanel {

    public double[][] dane;
    public int funkcja;
    final int odstep = 30;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int maxX = getWidth();
        int maxY = getHeight();

        g.setColor(Color.black);
        g.drawLine(maxX / 2, maxY, maxX / 2, 0);
        g.drawLine(0, maxY / 2, maxX, maxY / 2);
        double ym = 0;
        double xm = 0;
        for (int i = 0; i < dane.length; i++) {
            if (Math.abs(dane[i][0]) > xm) {
                xm = Math.abs(dane[i][0]);
            }
        }
        switch (funkcja) {
            case 1:  //sin
                ym = 1;
                for (int i = 0; i < dane.length; i++) {
                    g.setColor(Color.black);
                    double realX = dane[i][0];
                    double realY = Math.sin(dane[i][0]);
                    double realXscaled = ((realX * (maxX / 2 - odstep))) / xm;
                    double realYscaled = ((realY * (maxY / 2 - odstep)) / ym);
                    int x = (int) (maxX / 2 + realXscaled);
                    int y = (int) (maxY - (maxY / 2 + realYscaled));
                    g.drawLine(maxX / 2 - 5, odstep, maxX / 2 + 5, odstep);
                    g.drawString("" + ym, maxX / 2 + 5, odstep);
                    g.drawLine(maxX - odstep, maxY / 2 - 5, maxX - odstep, maxY / 2 + 5);
                    g.drawString("" + xm, maxX - odstep, maxY / 2 - 10);
                    g.setColor(Color.green);
                    if (realY > dane[i][2]) {
                        g.setColor(Color.red);
                    }
                    if (realY < dane[i][1]) {
                        g.setColor(Color.yellow);
                    }
                    g.drawRect(x - 2, y - 2, 4, 4);
                    g.setColor(Color.black);
                    //g.drawString("("+(Math.floor(realX*100))/100+";"+(Math.floor(realY*100))/100+")", x, y+20);
                }
                break;
            case 2:  //cos
                ym = 1;
                for (int i = 0; i < dane.length; i++) {
                    g.setColor(Color.black);
                    double realX = dane[i][0];
                    double realY = Math.cos(dane[i][0]);
                    double realXscaled = ((realX * (maxX / 2 - odstep))) / xm;
                    double realYscaled = ((realY * (maxY / 2 - odstep)) / ym);
                    int x = (int) (maxX / 2 + realXscaled);
                    int y = (int) (maxY - (maxY / 2 + realYscaled));
                    g.drawLine(maxX / 2 - 5, odstep, maxX / 2 + 5, odstep);
                    g.drawString("" + ym, maxX / 2 + 5, odstep);
                    g.drawLine(maxX - odstep, maxY / 2 - 5, maxX - odstep, maxY / 2 + 5);
                    g.drawString("" + xm, maxX - odstep, maxY / 2 - 10);
                    g.setColor(Color.green);
                    if (realY > dane[i][2]) {
                        g.setColor(Color.red);
                    }
                    if (realY < dane[i][1]) {
                        g.setColor(Color.yellow);
                    }
                    g.drawRect(x - 2, y - 2, 4, 4);
                    g.setColor(Color.black);
                    //g.drawString("("+(Math.floor(realX*100))/100+";"+(Math.floor(realY*100))/100+")", x, y+20);
                }
                break;
            case 3: //abs
                ym = xm;
                for (int i = 0; i < dane.length; i++) {
                    g.setColor(Color.black);
                    double realX = dane[i][0];
                    double realY = Math.abs(dane[i][0]);
                    double realXscaled = ((realX * (maxX / 2 - odstep))) / xm;
                    double realYscaled = ((realY * (maxY / 2 - odstep)) / ym);
                    int x = (int) (maxX / 2 + realXscaled);
                    int y = (int) (maxY - (maxY / 2 + realYscaled));
                    g.drawLine(maxX / 2 - 5, odstep, maxX / 2 + 5, odstep);
                    g.drawString("" + ym, maxX / 2 + 5, odstep);
                    g.drawLine(maxX - odstep, maxY / 2 - 5, maxX - odstep, maxY / 2 + 5);
                    g.drawString("" + xm, maxX - odstep, maxY / 2 - 10);
                    g.setColor(Color.green);
                    if (realY > dane[i][2]) {
                        g.setColor(Color.red);
                    }
                    if (realY < dane[i][1]) {
                        g.setColor(Color.yellow);
                    }
                    g.drawRect(x - 2, y - 2, 4, 4);
                    g.setColor(Color.black);
                    //g.drawString("("+(Math.floor(realX*100))/100+";"+(Math.floor(realY*100))/100+")", x, y+20);
                }
                break;
            case 4: // sqrt
                ym = xm;
                for (int i = 0; i < dane.length; i++) {
                    if (dane[i][0] >= 0) {
                        g.setColor(Color.black);
                        double realX = dane[i][0];
                        double realY = Math.sqrt(dane[i][0]);
                        double realXscaled = ((realX * (maxX / 2 - odstep))) / xm;
                        double realYscaled = ((realY * (maxY / 2 - odstep)) / ym);
                        int x = (int) (maxX / 2 + realXscaled);
                        int y = (int) (maxY - (maxY / 2 + realYscaled));
                        g.drawLine(maxX / 2 - 5, odstep, maxX / 2 + 5, odstep);
                        g.drawString("" + ym, maxX / 2 + 5, odstep);
                        g.drawLine(maxX - odstep, maxY / 2 - 5, maxX - odstep, maxY / 2 + 5);
                        g.drawString("" + xm, maxX - odstep, maxY / 2 - 10);
                        g.setColor(Color.green);
                        if (realY > dane[i][2]) {
                            g.setColor(Color.red);
                        }
                        if (realY < dane[i][1]) {
                            g.setColor(Color.yellow);
                        }
                        g.drawRect(x - 2, y - 2, 4, 4);
                        g.setColor(Color.black);
                        //g.drawString("("+(Math.floor(realX*10))/10+";"+(Math.floor(realY*10))/10+")", x, y+20);
                    }
                }
                break;
            default:
                break;
        }

    }
}

class Start {

    static JButton przycisk1;
    static JButton przycisk2;
    static JButton przycisk3;
    static JButton przycisk4;
    static MyJPanel panel;
    JFrame ramka;

    public static void main(String[] argv) throws java.io.IOException {
        Scanner in = new Scanner(System.in);
        int N ;
        N = in.nextInt();
        double[][] dane = new double[N][3];
        for (int i = 0; i < N; i++) {
         dane[i][0] = in.nextDouble();
         dane[i][1] = in.nextDouble();
         dane[i][2] = in.nextDouble();
         }
    
        JFrame ramka = new JFrame("Wykresy");
        panel = new MyJPanel();
        panel.dane = dane;
        przycisk1 = new JButton("sinus");
        przycisk2 = new JButton("cosinus");
        przycisk3 = new JButton("abs");
        przycisk4 = new JButton("sqrt");
        przycisk1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.funkcja = 1;
                panel.repaint();
            }
        });
        przycisk2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.funkcja = 2;
                panel.repaint();

            }
        });
        przycisk3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.funkcja = 3;
                panel.repaint();
            }
        });
        przycisk4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.funkcja = 4;
                panel.repaint();
            }
        });
        ramka.getContentPane().add(BorderLayout.WEST, przycisk1);
        ramka.getContentPane().add(BorderLayout.EAST, przycisk2);
        ramka.getContentPane().add(BorderLayout.NORTH, przycisk3);
        ramka.getContentPane().add(BorderLayout.SOUTH, przycisk4);
        ramka.getContentPane().add(panel);
        ramka.setSize(400, 400);
        ramka.setVisible(true);
        ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}