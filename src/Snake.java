public class Snake {
    private int posX[] = new int [GamePanel.GAME_UNITS];;
    private int posY[] = new int [GamePanel.GAME_UNITS];
    private int buttonDown;
    private int buttonUp;
    private int buttonLeft;
    private int buttonRight;
    private int applesEaten = 0;
    private int bodyParts = 6;
    private int snakeNum;
    private char direction;



    Snake(int spawnX, int spawnY, int buttonDown, int buttonUp, int buttonLeft, int buttonRight, char spawnDirection, int snakeNum) {
        this.buttonDown = buttonDown;
        this.buttonUp = buttonUp;
        this.buttonLeft = buttonLeft;
        this.buttonRight = buttonRight;
        this.direction = spawnDirection;
        this.posX[snakeNum] = spawnX;
        this.posY[snakeNum] = spawnY;
    }
    public int[] getPosX() {
        return posX;
    }

    public void setPosX(int posX, int i) {
        this.posX[i] = posX;
    }

    public int[] getPosY() {
        return posY;
    }

    public void setPosY(int posY, int i) {
        this.posY[i] = posY;
    }

    public int getButtonDown() {
        return buttonDown;
    }

    public void setButtonDown(int buttonDown) {
        this.buttonDown = buttonDown;
    }

    public int getButtonUp() {
        return buttonUp;
    }

    public void setButtonUp(int buttonUp) {
        this.buttonUp = buttonUp;
    }

    public int getButtonLeft() {
        return buttonLeft;
    }

    public void setButtonLeft(int buttonLeft) {
        this.buttonLeft = buttonLeft;
    }

    public int getButtonRight() {
        return buttonRight;
    }

    public void setButtonRight(int buttonRight) {
        this.buttonRight = buttonRight;
    }

    public int getApplesEaten() {
        return applesEaten;
    }

    public void addApplesEaten() {
        applesEaten++;
    }

    public void addBodyPart(){
        bodyParts++;
    }

    public int getBodyParts(){
        return bodyParts;
    }
    public void setApplesEaten(int applesEaten) {
        this.applesEaten = applesEaten;
    }

    public void setBodyParts(int bodyParts) {
        this.bodyParts = bodyParts;
    }

    public char getDirection() {
        return direction;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }
}

