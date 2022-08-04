import java.util.Random;

public class SMG extends Bullet {
    private double spread;

    public SMG(int x, int y) {
        super(x, y, "bullet.png");
        speed = 50;
        damage = 15;
        pierce = 0;
        spread = .1;
        fireDelay = 4;

        Random rand = new Random();
        double spreadX = rand.nextDouble(-spread,spread);
        double spreadY = rand.nextDouble(-spread,spread);
        xDiff += spreadX * distance;
        yDiff += spreadY * distance;
        xTrajectory = xDiff / distance;
        yTrajectory = yDiff / distance;
    }
}
