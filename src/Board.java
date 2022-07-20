import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener, KeyListener, MouseListener {
    private final int DELAY = 25;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private Timer timer;
    private Player player;
    private static int gameTime = 0;


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
        drawScore(g);

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

    private void drawScore(Graphics g) {
        // set the text to be displayed
        String text = "Health: " + player.getHealth() + " Timer: " + gameTime / 40;
        // we need to cast the Graphics to Graphics2D to draw nicer text
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        // set the text color and font
        g2d.setColor(new Color(30, 201, 139));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        // draw the score in the bottom center of the screen
        // https://stackoverflow.com/a/27740330/4655368
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        // the text will be contained within this rectangle.
        // here I've sized it to be the entire bottom row of board tiles
        Rectangle rect = new Rectangle(0,0 , WIDTH, 100);
        // determine the x coordinate for the text
        int x = rect.x + 50;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + rect.height / 2;
        // draw the string
        g2d.drawString(text, x, y);
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
                int xDiff = bullet.getPos().x - enemy.getPos().x;
                int yDiff = bullet.getPos().y - enemy.getPos().y;
                if (xDiff > 0 && xDiff < Enemy.WIDTH &&
                        yDiff > 0 && yDiff < Enemy.HEIGHT) {
                    grave.add(bullet);
                    enemy.damage(bullet.damage);
                }
            }
            if (enemy.isDead()) kill.add(enemy);
            int xDiff = Player.getPos().x - enemy.getPos().x;
            int yDiff = Player.getPos().y - enemy.getPos().y;
            if (xDiff > 0 && xDiff < Enemy.WIDTH &&
                    yDiff > 0 && yDiff < Enemy.HEIGHT) {
                player.damage(30);
            }

        }
        bullets.removeAll(grave);
        enemies.removeAll(kill);
        repaint();
        gameTime++;
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

