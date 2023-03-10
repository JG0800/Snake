import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Spieler1 extends JPanel implements ActionListener {
    GamePanel panel = new GamePanel();
    int bodyParts1 = 6;
    int x1[] = new int[panel.GAME_UNITS];
    int y1[] = new int[panel.GAME_UNITS];
    int apple1X;
    int apple1Y;
    int applesEaten1;
    char direction1 = '4';
    boolean running1 = false;

    public void move1() {
        for (int i = bodyParts1; i > 0; i--) {
            x1[i] = x1[i - 1];
            y1[i] = y1[i - 1];
        }
        switch (direction1) {
            case '8':
                y1[0] = y1[0] - panel.UNIT_SIZE;
                break;
            case '5':
                y1[0] = y1[0] + panel.UNIT_SIZE;
                break;
            case '4':
                x1[0] = x1[0] - panel.UNIT_SIZE;
                break;
            case '6':
                x1[0] = x1[0] + panel.UNIT_SIZE; //x[0] ist erst an x=0 dann x=25 usw..
                break;
        }
    }
    public void newApple1(){
        apple1X = panel.random.nextInt(panel.SCREEN_WIDTH/panel.UNIT_SIZE)*panel.UNIT_SIZE;
        apple1Y = panel.random.nextInt(panel.SCREEN_HEIGHT/panel.UNIT_SIZE)*panel.UNIT_SIZE;
    }

    public void checkApple1(){
        if ((x1[0] == apple1X)&&(y1[0] == apple1Y)){
            bodyParts1++;
            applesEaten1++;
            newApple1();
        }
    }
    public void checkCollisions1(){
        for (int i = bodyParts1; i>0; i--){ //i=6
            //prüft ob der Kopf die einzelnen Teile der Schlange frisst
            if ((x1[0] == x1[i])&&(y1[0] == y1[i])){ //wenn x[0] == x[6] also der Kopf, den letzten Teil der Schlange frisst
                panel.running = false;
            }
        }
        //check if head touches left border
        if (x1[0] < 0){
            panel.running = false;
        }
        //check if head touches right border
        if (x1[0] > panel.SCREEN_WIDTH){
            panel.running = false;
        }
        //check if head touches top border
        if (y1[0] < 0) {
            panel.running = false;
        }
        //check if head touches bottom border
        if (y1[0] > panel.SCREEN_HEIGHT) {
            panel.running = false;
        }
        if (!panel.running){
            panel.timer.stop();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)  {
        if (running1) {
            move1();
            checkApple1();
            checkCollisions1();
            //spieler1.checkCollisions1();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction1 != '6') {
                        direction1 = '4';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if (direction1 != '4') {
                        direction1 = '6';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if (direction1 != '5') {
                        direction1 = '8';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if (direction1 != '8') {
                        direction1 = '5';
                    }
                    break;
            }
        }
    }
}