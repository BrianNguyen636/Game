import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class Board extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener{
    private final int DELAY = 25;
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static ArrayList<Bullet> bullets = new ArrayList<>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    private static Timer timer;
    private Player player;
    private static int gameTime = 0;
    private static boolean shopping = false;
    private static int wave = 1;


    public Board() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(232, 232, 232));

        timer = new Timer(DELAY, this);
        timer.start();
        player = new Player();

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
        drawText(g);
        if (shopping) drawShop(g);
        if (player.isDead()) drawGameOver(g);

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

    private void drawShop(Graphics g) {
        Rectangle rect1 = new Rectangle(100,100,300,300);
        Rectangle rect2 = new Rectangle(450,100,300,300);
        Rectangle rect3 = new Rectangle(WIDTH - 400, 100, 300,300);
        g.setColor(new Color(248, 200, 160));
        g.fillRect(rect1.x,rect1.y, rect1.width,rect1.height);
        g.fillRect(rect2.x,rect2.y, rect2.width,rect2.height);
        g.fillRect(rect3.x,rect3.y, rect3.width,rect3.height);
        g.setColor(Color.red);
        g.fillRect(rect2.x, 500, 300, 100);
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
        g2d.setColor(Color.black);
        g2d.setFont(new Font("Lato", Font.BOLD, 25));
        g2d.drawString("$200 Damage up", rect1.x + 50, rect1.y + rect1.height/2);
        g2d.drawString("$200 Fire rate up", rect2.x + 50, rect2.y + rect2.height/2);
        g2d.drawString("$500 Pierce up", rect3.x + 50, rect3.y + rect3.height/2);
        g2d.drawString("Skip", rect2.x + 50, 550);
    }

    private void drawGameOver(Graphics g) {
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
        g2d.setColor(new Color(255, 43, 43));
        g2d.setFont(new Font("Lato", Font.BOLD, 25));

        g2d.drawString("GAME OVER", WIDTH / 2 - 100, HEIGHT / 2);
    }

    private void drawText(Graphics g) {
        // set the text to be displayed
        int seconds = 30 - (gameTime / 40) ;
        String secs;
        if (seconds < 10) {
            secs = "0" + seconds;
        } else {
            secs = "" + seconds;
        }
        String text = "Health: " + player.getHealth() +
                    " | Money: $" + player.getMoney() +
                    " | Wave: " + wave +
                    " | Timer: " + secs;
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
        Rectangle rect = new Rectangle(0,0 , WIDTH, 100);
        // determine the x coordinate for the text
        int x = rect.x + 50;
        // determine the y coordinate for the text
        // (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = rect.y + rect.height / 2;
        // draw the string
        g2d.drawString(text, x, y);
    }
    private void waveEnd() {
        enemies.clear();
        player.setReady(false);
        shopping = true;
        gameTime = 0;
        wave++;
    }

    public void gameTick() {
        if (wave == 1) {
            enemies.add(new Grunt());
        } else if (wave == 2) {
            enemies.add(new Guard());
        } else {
            enemies.add(new Striker());
        }
        if (gameTime % (40 * 30) == 0 && gameTime > 0) {
            waveEnd();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (player.isDead()) {
            timer.stop();
        }
        player.tick();
        if (player.isReady())
        player.fire (
                MouseInfo.getPointerInfo().getLocation().x - this.getLocationOnScreen().x,
                MouseInfo.getPointerInfo().getLocation().y - this.getLocationOnScreen().y
        );
        if (gameTime % 40 == 0 && player.isReady()) gameTick();

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
                    if (bullet.getPierce() == 0) {
                        grave.add(bullet);
                    } else bullet.setPierce(bullet.getPierce() - 1);
                    enemy.damaged((int) (Bullet.getDamage() * player.getDamageMod()));
                }
            }
            if (enemy.isDead()) {
                kill.add(enemy);
                player.setMoney(player.getMoney() + Enemy.getBounty());
            }

            int xDiff = Player.getPos().x - enemy.getPos().x;
            int yDiff = Player.getPos().y - enemy.getPos().y;
            if (xDiff > 0 && xDiff < Enemy.WIDTH &&
                    yDiff > 0 && yDiff < Enemy.HEIGHT) {
                player.damage(Enemy.getDamage());
            }
            bullets.removeAll(grave);
        }
        enemies.removeAll(kill);
        repaint();
        if (player.isReady()) gameTime++;
    }
    public static void pause() {
        if (timer.isRunning()) {
            timer.stop();
        } else {
            timer.start();
        }
    }
    public void shop (int x, int y) {
        //shop 1
        int cost;
        if (x > 100 && x < 400
                && y > 100 && y < 400) {
            cost = 200;
            if (player.getMoney() >= cost) {
                player.setMoney(player.getMoney() - cost);

                player.setDamageMod(player.getDamageMod() * 1.5);

                player.setReady(true);
                shopping = false;
            }
        }
        //shop 2
        if (x > 450 && x < 750
                && y > 100 && y < 400) {
            cost = 200;
            if (player.getMoney() >= cost) {
                player.setMoney(player.getMoney() - cost);

                player.setFireRateMod(player.getFireRateMod() * .5);

                player.setReady(true);
                shopping = false;
            }
        }
        if (x > WIDTH - 400 && x < WIDTH - 100
                && y > 100 && y < 400) {
            cost = 500;
            if (player.getMoney() >= cost) {
                player.setMoney(player.getMoney() - cost);

                player.setPierceBonus(player.getPierceBonus() + 1);

                player.setReady(true);
                shopping = false;
            }
        }
        //skip
        if (x > 450 && x < 750
                && y > 500 && y < 600) {
            player.setReady(true);
            shopping = false;
        }
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
        if (shopping) {
            shop(e.getX(),e.getY());
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

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

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}

