import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public abstract class Bullet {
    BufferedImage image;
    Point pos;
    Point target;
    double xTrajectory;
    double yTrajectory;
    double distance;
    double xDiff, yDiff;

    static int speed;
    int pierce;
    static int damage;
    static int fireDelay;

    public Bullet(int x, int y) {
        // load the assets

        // initialize the state
        pos = new Point(Player.getPos().x + 22,Player.getPos().y + 22);
        target = new Point(x,y);
        xDiff = target.x - pos.x;
        yDiff = target.y - pos.y;
        distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
        xTrajectory = xDiff / distance;
        yTrajectory = yDiff / distance;
    }

    public void tick() {

        pos.translate(
                (int) (speed * (xTrajectory)),
                (int) (speed * (yTrajectory))
        );
    }
    public void loadImage(String filename) {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File(filename));
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

    public static int getFireDelay() {
        return fireDelay;
    }

    public static void setFireDelay(int delay) {
        fireDelay = delay;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        Bullet.speed = speed;
    }

    public int getPierce() {
        return pierce;
    }

    public void setPierce(int pierce) {
        this.pierce = pierce;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        Bullet.damage = damage;
    }

    public Point getPos() {
        return pos;
    }
}
