import javafx.scene.input.KeyCode;

import javax.management.DescriptorRead;
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
    boolean startMenu = true;
    boolean isCrawling = false;
    boolean gameOver = false;

    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
    }

    public void startGame() {
        initSnakes();
        newApple();
        isCrawling=true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    private void initSnakes() {
        this.snakes[0] = new Snake(0,300, KeyEvent.VK_DOWN, KeyEvent.VK_UP, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, 'R');
        this.snakes[1] = new Snake(0, 0, KeyEvent.VK_S, KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, 'R');
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        if (startMenu){
            startMenu(g);
        }
        else {
            if (isCrawling){
                    drawSnakes(g);
                    drawScores(g);
                }
                else {
                    gameOver(g);
                }
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
                snake.setPosX(snake.getPosX()[i - 1], i); //????
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
    public void checkApple(){
        for ( Snake snake : snakes) {
            if (snake.getPosX()[0] == appleX && snake.getPosY()[0] == appleY) {
                snake.addApplesEaten();
                newApple();
            }
        }
    }
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

    //checks if snake touches any other snake
    public void checkCollisions(){
        for (Snake snake : snakes){
            for (Snake snake2 : snakes){ //ich loope hier 2 mal durch die snakes um alle m??glichen Kombinationen von Schlangen, die sich ber??hren k??nnen zu ??berpr??fen
                if (snake != snake2){
                    for (int i = 0; i < snake.getBodyParts(); i++){
                        if (snake.getPosX()[0] == snake2.getPosX()[i] && snake.getPosY()[0] == snake2.getPosY()[i]){ //check if head touches body of other snake
                            isCrawling = false;
                        }
                    }
                }
            }
        }

        for (Snake snake : snakes){
            if (snake.getPosX()[0] < 0){
                isCrawling = false;
            }
            //check if head touches right border
            if (snake.getPosX()[0] > SCREEN_WIDTH){
                isCrawling = false;
            }
            //check if head touches top border
            if (snake.getPosY()[0] < 0) {
                isCrawling = false;
            }
            //check if head touches bottom border
            if (snake.getPosY()[0] >  SCREEN_HEIGHT) {
                isCrawling = false;
            }
            if (!isCrawling){
                timer.stop();
            }
        }

        //check if snake touches itself
        for (Snake snake : snakes) {
            for (int i = snake.getBodyParts(); i > 0; i--) {
                if ((snake.getPosX()[0] == snake.getPosX()[i]) && (snake.getPosY()[0] == snake.getPosY()[i])) {
                    isCrawling = false;
                }
            }
        }

    }
    public void drawSnakes(Graphics g){
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

        for (Snake snake : snakes) {//snake
            if ( snake == this.snakes[0] ){
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
            else if ( snake == this.snakes[1] ){
                for(int i=0; i<snake.getBodyParts(); i++) {
                    if (i == 0) {
                        g.setColor(Color.blue);
                        g.fillRect(snake.getPosX()[i], snake.getPosY()[i], UNIT_SIZE, UNIT_SIZE);
                    } else {
                        g.setColor(Color.blue.darker());
                        g.fillRect(snake.getPosX()[i], snake.getPosY()[i], UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }

        }
    }
    public void drawScores(Graphics g){
        g.setColor(Color.green);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+snakes[0].getApplesEaten(), (0 + metrics.stringWidth("Score: "+snakes[0].getApplesEaten())/2) ,g.getFont().getSize());

        g.setColor(Color.blue);
        g.setFont(new Font("Ink Free", Font.BOLD,40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: "+snakes[1].getApplesEaten(), (int) (SCREEN_WIDTH-(metrics1.stringWidth("Score: "+snakes[1].getApplesEaten())*1.5 )),g.getFont().getSize());
    }

    public void drawScoreTable(Graphics g){
        g.setColor(Color.white);
        g.setFont(new Font("Ink Free", Font.BOLD,70));
        FontMetrics scores = getFontMetrics(g.getFont());
        g.drawString("Scores: ", (SCREEN_WIDTH-scores.stringWidth("Score: "))/2,g.getFont().getSize());
    }

    public void gameOver(Graphics g){ //Graphics g??
        gameOver = true;
        if (snakes[0].getApplesEaten()>snakes[1].getApplesEaten()){
            drawScoreTable(g);
            drawControls(g);
            g.setColor(Color.green);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics greenSnake = getFontMetrics(g.getFont());
            g.drawString(snakes[0].getApplesEaten()+" Green Snake", (SCREEN_WIDTH-(greenSnake.stringWidth(snakes[0].getApplesEaten()+" Green Snake")))/2,SCREEN_HEIGHT/6);

            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics blueSnake = getFontMetrics(g.getFont());
            g.drawString(snakes[1].getApplesEaten()+" Blue Snake", (SCREEN_WIDTH-(blueSnake.stringWidth(snakes[1].getApplesEaten()+" Blue Snake")))/2,SCREEN_HEIGHT/4);
        }
        else if (snakes[0].getApplesEaten()<snakes[1].getApplesEaten()) {
            drawScoreTable(g);
            drawControls(g);
            g.setColor(Color.blue);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics blueSnake = getFontMetrics(g.getFont());
            g.drawString(snakes[1].getApplesEaten()+" Blue Snake", (SCREEN_WIDTH-(blueSnake.stringWidth(snakes[1].getApplesEaten()+" Blue Snake")))/2,SCREEN_HEIGHT/6);

            g.setColor(Color.green);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics greenSnake = getFontMetrics(g.getFont());
            g.drawString(snakes[0].getApplesEaten()+" Green Snake", (SCREEN_WIDTH-(greenSnake.stringWidth(snakes[0].getApplesEaten()+" Green Snake")))/2,SCREEN_HEIGHT/4);

        }
        else {
            drawControls(g);
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Draw! both snakes score: "+snakes[0].getApplesEaten(), (SCREEN_WIDTH-metrics.stringWidth("Draw! both snakes score: "+snakes[0].getApplesEaten()))/2,SCREEN_HEIGHT/4);
        }

    }
    public void startMenu(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,70));
        FontMetrics snakeMetrics = getFontMetrics(g.getFont());
        g.drawString("Snake", (SCREEN_WIDTH-snakeMetrics.stringWidth("Snake"))/2 ,SCREEN_HEIGHT/2-g.getFont().getSize());
        drawControls(g);
    }
    public void drawControls(Graphics g){
        if (startMenu){
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics startText = getFontMetrics(g.getFont());
            g.drawString("Space - Start Game", 0, SCREEN_HEIGHT-SCREEN_HEIGHT/8);
            g.drawString("ESC - Exit Game", 0, SCREEN_HEIGHT-SCREEN_HEIGHT/20);
        }
        else {
            g.setColor(Color.white);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics startText = getFontMetrics(g.getFont());
            g.drawString("Space - Start Menu", 0, SCREEN_HEIGHT-SCREEN_HEIGHT/8);
            g.drawString("ESC - Exit Game", 0, SCREEN_HEIGHT-SCREEN_HEIGHT/20);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e)  {

            if (isCrawling) {
                move();
                checkApple();
                //scoreEffects();
                checkCollisions();
            }
            repaint();

    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyCode());
            if (startMenu){
                if (e.getKeyCode()==32){
                    startMenu=false;
                    startGame();
                }
                else if (e.getKeyCode()==27){
                    SwingUtilities.getWindowAncestor(GamePanel.this).dispose();
                }
            }
            if (gameOver){
                if (e.getKeyCode()==32){
                    gameOver = false;
                    startMenu = true;
                    repaint();
                }
                else if (e.getKeyCode()==27){
                    SwingUtilities.getWindowAncestor(GamePanel.this).dispose();
                }
            }


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
