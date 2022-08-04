import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public abstract class Enemy {
    // image that represents the player's position on the board
    private static BufferedImage image;
    public static int WIDTH;
    public static int HEIGHT;

    // current position of the player on the board grid
    private Point pos;

    static int speed;
    static int maxHealth;
    static int damage;
    static int bounty;

    private int health = maxHealth;

    public Enemy(String filename) {
        // initialize the state
        loadImage(filename);
        WIDTH = image.getWidth();
        HEIGHT = image.getHeight();
        Random random = new Random();
        int position = random.nextInt(4);
        switch (position) {
            case 0 -> pos = new Point(0, random.nextInt(Board.HEIGHT));
            case 1 -> pos = new Point(Board.WIDTH, random.nextInt(Board.HEIGHT));
            case 2 -> pos = new Point(random.nextInt(Board.WIDTH), 0);
            case 3 -> pos = new Point(random.nextInt(Board.WIDTH), Board.HEIGHT);
        }
    }

    public static void buff(int spd, int hp) {
        speed += spd;
        maxHealth += hp;
    }

    public static int getDamage() {
        return damage;
    }

    public static int getBounty() {
        return bounty;
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

    public void damaged(int value) {
        health -= value;
    }
    public boolean isDead() {
        return health <= 0;
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
}
