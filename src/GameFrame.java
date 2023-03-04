import javax.swing.*;

public class GameFrame extends JFrame { //extends erweitert eine Klasse um die Inhalte einer anderen Klasse
    GameFrame(){
        GamePanel Panel = new GamePanel();

        this.add(new GamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
