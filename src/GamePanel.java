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
    final int x[] = new int[GAME_UNITS]; //???
    final int y[] = new int[GAME_UNITS];
    static final int ROCK_SIZE = 50;
    int rockX = -1;
    int rockY = -1;
    int bodyParts = 6;
    int bodyParts2 = 6;
    int applesEaten;
    int applesEaten2;
    int appleX;
    int appleX2;
    int appleY;
    int appleY2;
    char direction = 'R';
    char direction2 = 'L';
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
        newApple();
        isCrawling=true;
        timer = new Timer(DELAY,this); //was macht Timer()?; aus welcher Klasse wurde ein Objekt erzeugt?; was bedeutet this in diesem Kontext?
        timer.start();
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
            if (rockX != -1 && rockY != -1) {
                g.fillOval(rockX - ROCK_SIZE/2, rockY - ROCK_SIZE/2, ROCK_SIZE, ROCK_SIZE);
            }


            for(int i=0; i<bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            /*for(int i=0; i< spieler1.bodyParts1; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(spieler1.x1[i], spieler1.y1[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(spieler1.x1[i], spieler1.x1[i], UNIT_SIZE, UNIT_SIZE);
                }
            }*/
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD,40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
        }
        else {
            gameOver(g);
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

    public void move(){
        for (int i=bodyParts; i>0;i--){ //i=6
            x[i] = x[i-1]; //array x[6] hat jetzt die Koordinaten von x[5] usw.
            y[i] = y[i-1];
        }
        /*Die aktualisierung muss als erstes erfolgen, denn:
         * x[1] == x[0] bedeutet x1,x0 sind beide an der x Koordinate 0
         * x[1] == x[0] bedeutet x1,x0 sind beide an der x Koordinate 0
         * danach wird x[0] erhöht, demnach ist x[0]=25; x[1]=0
         * würde mann x[0] erst erhöhen wäre x[0]=25, x[1]=25
         * */
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE; //x[0] ist erst an x=0 dann x=25 usw..
                break;
        }

    }
    public void checkApple(){
        if ((x[0] == appleX)&&(y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkRock(){
        int rockBoxLeft = rockX - ROCK_SIZE / 2;
        int rockBoxRight = rockX + ROCK_SIZE / 2;
        int rockBoxTop = rockY - ROCK_SIZE / 2;
        int rockBoxBottom = rockY + ROCK_SIZE / 2;

        if (x[0] >= rockBoxLeft && x[0] <= rockBoxRight && y[0] >= rockBoxTop && y[0] <= rockBoxBottom) {
            isCrawling = false;
        }
    }
    public void scoreEffects(){
        int i = 2;
        if (applesEaten>=i){
            newRock();
            checkRock();
            i = i + 2;
        }
    }
    public void checkCollisions(){
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
    }
    public void gameOver(Graphics g){ //???
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2);

        g.setFont(new Font("Ink Free", Font.BOLD,20));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Apples eaten in total: "+applesEaten,(SCREEN_WIDTH - metrics2.stringWidth("Apples eaten in total: "+applesEaten))/2,SCREEN_HEIGHT/2 + g.getFont().getSize()+20);
    }

    @Override
    public void actionPerformed(ActionEvent e)  {
        if (isCrawling) {
            move();
            //spieler1.move();
            checkApple();
            //spieler1.checkApple();
            scoreEffects();
            checkCollisions();
            //spieler1.checkCollisions1();
        }
        repaint();
    }
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            System.out.println(e.getKeyCode());
            switch (e.getKeyCode()){

                case 65: //left
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case 68: //right
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case 87: //up
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case 83: //down
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;

                case 37: //left
                    if (direction != 'R'){
                        direction = 'L';
                    }
                    break;

                case 39: //right
                    if (direction != 'L'){
                        direction = 'R';
                    }
                    break;

                case 38: //up
                    if (direction != 'D'){
                        direction = 'U';
                    }
                    break;

                case 40: //down
                    if (direction != 'U'){
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
