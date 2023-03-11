import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    static final int ROCK_SIZE = 50;
    int rockX = -1;
    int rockY = -1;
    int appleY;
    int appleX;
    Snake[] snakes = new Snake[2];

    boolean isCrawling = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }
    public void startGame() {
        initSnakes();
        newApple();
        isCrawling=true;
        timer = new Timer(DELAY,this); //was macht Timer()?; aus welcher Klasse wurde ein Objekt erzeugt?; was bedeutet this in diesem Kontext?
        timer.start();
    }

    private void initSnakes() {
        this.snakes[0] = new Snake(GAME_UNITS,GAME_UNITS, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, 'R');
        this.snakes[1] = new Snake(GAME_UNITS, GAME_UNITS, KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, 'L');
    }

    public void paintComponent(Graphics g) { //??
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (isCrawling){
            g.setColor(Color.black);
            for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
                g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);
            }
            g.setColor(Color.red);
            g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE);

            g.setColor(Color.white);
            /*if (rockX != -1 && rockY != -1) {
                g.fillOval(rockX - ROCK_SIZE/2, rockY - ROCK_SIZE/2, ROCK_SIZE, ROCK_SIZE);
            }*/

            for (Snake snake : snakes) {
                for(int i=0; i<snake.getBodyParts(); i++) {
                    if (i == 0) {
                        g.setColor(Color.GREEN);
                        g.fillRect(snake.getPosX()[i], snake.getPosY()[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(Color.GREEN.darker());
                        g.fillRect(snake.getPosX()[i], snake.getPosY()[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

            /*g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());*/
        }
        else {
            //gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }
    public void newRock(){
        int i = 0;
        if (rockX == -1 && rockY == -1){
            rockX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
            rockY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
            i++;
        }
    }

    public void move() {
        for (Snake snake : snakes) {
            for (int i = snake.getBodyParts(); i > 0; i--) {
                snake.setPosX(snake.getPosX()[i - 1], i);
                snake.setPosY(snake.getPosY()[i - 1], i);
            }
            switch (snake.getDirection()) {
                case 'U':
                    snake.getPosY()[0] = snake.getPosY()[0] - UNIT_SIZE;
                    break;
                case 'D':
                    snake.getPosY()[0] = snake.getPosY()[0] + UNIT_SIZE;
                    break;
                case 'L':
                    snake.getPosX()[0] = snake.getPosX()[0] - UNIT_SIZE;
                    break;
                case 'R':
                    snake.getPosX()[0] = snake.getPosX()[0] + UNIT_SIZE;
                    break;
            }

        }
    }
    /*public void checkApple(){
        if ((x[0] == appleX)&&(y[0] == appleY)){
            snake;
            applesEaten++;
            newApple();
        }
    }*/
    /*public void checkRock(){
        int rockBoxLeft = rockX - ROCK_SIZE / 2;
        int rockBoxRight = rockX + ROCK_SIZE / 2;
        int rockBoxTop = rockY - ROCK_SIZE / 2;
        int rockBoxBottom = rockY + ROCK_SIZE / 2;

        if (x[0] >= rockBoxLeft && x[0] <= rockBoxRight && y[0] >= rockBoxTop && y[0] <= rockBoxBottom) {
            isCrawling = false;
        }
    }*/
    /*public void scoreEffects(){
        int i = 2;
        if (applesEaten>=i){
            newRock();
            checkRock();
            i = i + 2;
        }
    }*/
    /*public void checkCollisions(){
        for (int i = bodyParts; i>0; i--){ //i=6
            //prüft ob der Kopf die einzelnen Teile der Schlange frisst
            if ((x[0] == x[i])&&(y[0] == y[i])){ //wenn x[0] == x[6] also der Kopf, den letzten Teil der Schlange frisst
                isCrawling = false;
            }
        }
        //check if head touches left border
        if (x[0] < 0){
            isCrawling = false;
        }
        //check if head touches right border
        if (x[0] > SCREEN_WIDTH){
            isCrawling = false;
        }
        //check if head touches top border
        if (y[0] < 0) {
            isCrawling = false;
        }
        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT) {
            isCrawling = false;
        }
        if (!isCrawling){
            timer.stop();
        }
    }*/
    /*public void gameOver(Graphics g){ //???
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

        g.setFont(new Font("Ink Free", Font.BOLD,20));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Apples eaten in total: "+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Apples eaten in total: "+applesEaten))/2,SCREEN_HEIGHT/2 + g.getFont().getSize()+20);
    }*/

    @Override
    public void actionPerformed(ActionEvent e)  {
        if (isCrawling) {
            move();
            /*checkApple();*/
            /*scoreEffects();
            checkCollisions();*/
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyCode());
            Snake turningSnake = null;
            for (Snake snake:snakes) {
                if (e.getKeyCode() == snake.getButtonLeft() || e.getKeyCode() == snake.getButtonRight() || e.getKeyCode() == snake.getButtonUp() || e.getKeyCode() == snake.getButtonDown()) {
                    turningSnake = snake;
                }
            }
            if (turningSnake == null) {
                return;
            }

            if (e.getKeyCode() == turningSnake.getButtonUp()) {
                if (turningSnake.getDirection() != 'D') {
                    turningSnake.setDirection('U');
                }
            } else if (e.getKeyCode() == turningSnake.getButtonDown()) {
                if (turningSnake.getDirection() != 'U') {
                    turningSnake.setDirection('D');
                }
            } else if (e.getKeyCode() == turningSnake.getButtonLeft()) {
                if (turningSnake.getDirection() != 'R') {
                    turningSnake.setDirection('L');
                }
            } else if (e.getKeyCode() == turningSnake.getButtonRight()) {
                if (turningSnake.getDirection() != 'L') {
                    turningSnake.setDirection('R');
                }
            }
        }
    }
}
