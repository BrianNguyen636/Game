import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final int DELAY = 1000 / 60;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private Timer timer;
    private Player player;


    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(232, 232, 232));

        timer = new Timer(DELAY, this);
        timer.start();
        player = new Player();
        for (int i = 0; i < 3; i++) {
            enemies.add(new Enemy());
        }


    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // when calling g.drawImage() we can use "this" for the ImageObserver
        // because Component implements the ImageObserver interface, and JPanel
        // extends from Component. So "this" Board instance, as a Component, can
        // react to imageUpdate() events triggered by g.drawImage()

        // draw our graphics.
        drawBackground(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g, this);
        }

        player.draw(g, this);
        for (Enemy enemy : enemies) {
            enemy.draw(g, this);
        }

        // this smooths out animations on some systems
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawBackground(Graphics g) {
        // draw a checkered background
        g.setColor(new Color(214, 214, 214));
//        for (int row = 0; row < ROWS; row++) {
//            for (int col = 0; col < COLUMNS - 4; col++) {
//                // only color every other tile
//                if ((row + col) % 2 == 1) {
//                    // draw a square tile at the current row/column position
//                    g.fillRect(
//                            col * TILE_SIZE,
//                            row * TILE_SIZE,
//                            TILE_SIZE,
//                            TILE_SIZE
//                    );
//                }
//            }
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.tick();
        ArrayList<Bullet> grave = new ArrayList<>();
        ArrayList<Enemy> kill = new ArrayList<>();

        for (Bullet bullet : bullets) {
            bullet.tick();
            Point pos = bullet.getPos();
            if (pos.x < 0 || pos.y < 0 || pos.x >= WIDTH || pos.y >= HEIGHT) {
                grave.add(bullet);
            }
        }
        for (Enemy enemy : enemies) {
            enemy.tick();
            for (Bullet bullet : Board.bullets) {
                int xDiff = bullet.getPos().x - enemy.pos.x;
                int yDiff = bullet.getPos().y - enemy.pos.y;
                if (xDiff > 0 && xDiff < Enemy.WIDTH &&
                        yDiff > 0 && yDiff < Enemy.HEIGHT) {
                    grave.add(bullet);
                    enemy.damage(bullet.damage);
                }
            }
            if (enemy.isDead()) kill.add(enemy);
        }
        bullets.removeAll(grave);
        enemies.removeAll(kill);
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        player.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}

