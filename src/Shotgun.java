import java.util.Random;

public class Shotgun extends Bullet {
    private double spread;

    public Shotgun(int x, int y) {
        super(x, y);
        loadImage("bullet.png");

        speed = 50;
        damage = 10;
        pierce = 0;
        spread = .10;
        fireDelay = 20;

        Random rand = new Random();
        double spreadX = rand.nextDouble(-spread,spread);
        double spreadY = rand.nextDouble(-spread,spread);
        double distance = Math.sqrt(Math.pow(xDiff,2) + Math.pow(yDiff, 2));
        xDiff += spreadX * distance;
        yDiff += spreadY * distance;
        xTrajectory = xDiff / distance;
        yTrajectory = yDiff / distance;
    }
}
