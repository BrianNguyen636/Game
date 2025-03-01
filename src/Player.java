import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Player {
    // image that represents the player's position on the board
    private BufferedImage image;
    public static int WIDTH;
    public static int HEIGHT;
    // current position of the player on the board grid
    private static Point pos;
    private boolean up, down, left, right;

    private int money = 0;
    private boolean ready = true;
    private static int speed = 10;
    private static int fireTime = 0;
    private int health = 100;
    private static int iFrames = 0;
    private double damageMod = 1;
    private double fireRateMod = 1;
    private int pierceBonus = 0;

    public Player() {
        // load the assets
        loadImage();

        // initialize the state
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        pos = new Point((Board.WIDTH - WIDTH)/2,
                        (Board.HEIGHT - HEIGHT)/2);
    }

    public void tick() {
        // this gets called once every tick, before the repainting process happens.
        // so we can do anything needed in here to update the state of the player.

        // prevent the player from moving off the edge of the board sideways

        if (up) {
            if (!left && !right) {
                pos.translate(0,-speed);
            } else pos.translate(0, (int) (-speed / Math.sqrt(2)));
        }
        if (down) {
            if (!left && !right) {
                pos.translate(0,speed);
            } else pos.translate(0, (int) (speed / Math.sqrt(2)));
        }
        if (left) {
            if (!down && !up) {
                pos.translate(-speed,0);
            } else pos.translate((int) (-speed / Math.sqrt(2)), 0);
        }
        if (right) {
            if (!down && !up) {
                pos.translate(speed,0);
            } else pos.translate((int) (speed / Math.sqrt(2)), 0);
        }

        if (pos.x < 0) {
            pos.x = 0;
        } else if (pos.x >= Board.WIDTH - image.getWidth()) {
            pos.x = Board.WIDTH - image.getWidth();
        }
        // prevent the player from moving off the edge of the board vertically
        if (pos.y < 0) {
            pos.y = 0;
        } else if (pos.y >= Board.HEIGHT - image.getHeight()) {
            pos.y = Board.HEIGHT - image.getHeight();
        }

        if (fireTime != 0) fireTime--;
        if (iFrames != 0) iFrames--;

    }

    public int getHealth() {
        return health;
    }

    public void damage(int value) {
        if (iFrames == 0) {
            health -= value;
            iFrames = 30;
        }
    }

    public double getDamageMod() {
        return damageMod;
    }

    public void setDamageMod(double damageMod) {
        this.damageMod = damageMod;
    }

    public double getFireRateMod() {
        return fireRateMod;
    }

    public void setFireRateMod(double fireRateMod) {
        this.fireRateMod = fireRateMod;
    }

    public int getPierceBonus() {
        return pierceBonus;
    }

    public void setPierceBonus(int pierceBonus) {
        this.pierceBonus = pierceBonus;
    }

    public int getMoney() {
        return money;
    }
    public void setMoney(int money) {
        this.money = money;
    }
    public boolean isDead() {
        return health <= 0;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public static Point getPos() {
        return pos;
    }

    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("watame.png"));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }

    public void draw(Graphics g, ImageObserver observer) {

        g.drawImage(
                image,
                pos.x,
                pos.y,
                observer
        );
    }

    public void keyPressed(KeyEvent e) {
        // every keyboard get has a certain code. get the value of that code from the
        // keyboard event so that we can compare it to KeyEvent constants
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_F) {
            if (!isReady()) setReady(true);
        }
        if (key == KeyEvent.VK_ESCAPE) {
            Board.pause();
        }

        if (key == KeyEvent.VK_W) {
            up = true;
        }
        if (key == KeyEvent.VK_D) {
            right = true;
        }
        if (key == KeyEvent.VK_S) {
            down = true;
        }
        if (key == KeyEvent.VK_A) {
            left = true;
        }
    }
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_W) {
            up = false;
        }
        if (key == KeyEvent.VK_D) {
            right = false;
        }
        if (key == KeyEvent.VK_S) {
            down = false;
        }
        if (key == KeyEvent.VK_A) {
            left = false;
        }

    }
    private void shotgun(int x, int y) {
        if (fireTime == 0) {
            for (int i = 0; i < 6; i++) {
                Shotgun s = new Shotgun(x,y);
                s.setPierce(getPierceBonus());
                Board.bullets.add(s);
            }
            fireTime = (int) (Shotgun.getFireDelay() * fireRateMod);
        }
    }
    private void SMG(int x, int y) {
        if (fireTime == 0) {
            SMG s = new SMG(x,y);
            s.setPierce(getPierceBonus());
            Board.bullets.add(s);
            fireTime = (int) (SMG.getFireDelay() * fireRateMod);
        }
    }
    public void fire(int x, int y) {
        shotgun(x,y);
    }
    public void mousePressed(MouseEvent e) {
//        if (fireTime == 0) {
//            for (int i = 0; i < 6; i++) {
//                Bullet bullet = new Shotgun(e.getX() - 12,e.getY() - 35);
//                Board.bullets.add(bullet);
//            }
//            fireTime = fireRate;
//        }
    }

    public void mouseReleased(MouseEvent e) {

    }
}
