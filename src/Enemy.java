import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Enemy {
    // image that represents the player's position on the board
    private BufferedImage image;
    public static int WIDTH;
    public static int HEIGHT;

    // current position of the player on the board grid

    private Point pos;
    private int speed = 5;
    private int health = 100;
    public Enemy() {
        // load the assets
        loadImage();

        // initialize the state
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        Random random = new Random();
        pos = new Point(random.nextInt(Board.WIDTH), random.nextInt(Board.HEIGHT));
    }

    public void tick() {

        int xDiff = Player.getPos().x - pos.x;
        int yDiff = Player.getPos().y - pos.y;
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
        pos.translate(
                (int) (xDiff * speed / distance),
                (int) (yDiff * speed / distance)
        );
//        collision();
    }
    //meh
    private void collision() {
        for (Enemy enemy : Board.enemies) {
            int xDiff = enemy.pos.x - pos.x;
            int yDiff = enemy.pos.y - pos.y;
            //collision from left
            if (xDiff < WIDTH && xDiff > 0) {
                pos.x = enemy.pos.x - WIDTH;
            } else if (xDiff > -WIDTH && xDiff < 0) {
                pos.x = enemy.pos.x + WIDTH;

            }
//            if (yDiff < 50 && yDiff > 0) {
//                pos.y = enemy.pos.y - 50;
//            } else if (yDiff > -50 && yDiff < 0) {
//                pos.y = enemy.pos.y + 50;
//            }
        }

    }

    public Point getPos() {
        return pos;
    }

    public void damage(int value) {
        health -= value;
    }
    public boolean isDead() {
        return health <= 0;
    }

    private void loadImage() {
        try {
            // you can use just the filename if the image file is in your
            // project folder, otherwise you need to provide the file path.
            image = ImageIO.read(new File("gangi.png"));
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
}
