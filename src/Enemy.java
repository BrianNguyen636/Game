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
    // current position of the player on the board grid
    public Point pos;
    private int speed = 4;
    private int health = 100;

    public Enemy() {
        // load the assets
        loadImage();

        // initialize the state
        Random random = new Random();
        pos = new Point(random.nextInt(Board.WIDTH), 0);
    }
    public void tick() {

        int xDiff = Player.pos.x - pos.x;
        int yDiff = Player.pos.y - pos.y;

        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            if (Player.pos.x > pos.x) {
                pos.translate(speed, 0);
            } else if (Player.pos.x < pos.x) {
                pos.translate(-speed, 0);
            }
        } else {
            if (Player.pos.y > pos.y) {
                pos.translate(0, speed);
            } else if (Player.pos.y < pos.y) {
                pos.translate(0, -speed);
            }
        }
        collision();
    }

    private void collision() {
        for (Enemy enemy : Board.enemies) {
            int xDiff = enemy.pos.x - pos.x;
            int yDiff = enemy.pos.y - pos.y;
            //collision from left
            if (xDiff < 50 && xDiff > 0) {
                pos.x = enemy.pos.x - 50;
            } else if (xDiff > -50 && xDiff < 0) {
                pos.x = enemy.pos.x + 50;
            }
//            if (yDiff < 50 && yDiff > 0) {
//                pos.y = enemy.pos.y - 50;
//            } else if (yDiff > -50 && yDiff < 0) {
//                pos.y = enemy.pos.y + 50;
//            }
        }

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
