import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Bullet {
    private BufferedImage image;
    private Point pos;
    private Point target;
    private double xTrajectory;
    private double yTrajectory;
    private int speed = 50;
    private double xDiff, yDiff;
    private int pierce = 0;
    private double spread = .10;
    public final int damage = 10;

    public Bullet(int x, int y) {
        // load the assets
        loadImage();

        // initialize the state
        pos = new Point(Player.getPos().x + 22,Player.getPos().y + 22);
        target = new Point(x,y);
        xDiff = target.x - pos.x;
        yDiff = target.y - pos.y;
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
        Random rand = new Random();
        double spreadX = rand.nextDouble(-spread,spread);
        double spreadY = rand.nextDouble(-spread,spread);
        xDiff += spreadX * distance;
        yDiff += spreadY * distance;
        xTrajectory = xDiff / distance;
        yTrajectory = yDiff / distance;
    }
    public void tick() {

        pos.translate(
                (int) (speed * (xTrajectory)),
                (int) (speed * (yTrajectory))
        );
    }

    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("bullet.png"));
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
    public Point getPos() {
        return pos;
    }
}
