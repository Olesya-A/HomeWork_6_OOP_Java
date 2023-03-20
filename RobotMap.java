import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RobotMap {

    private final int n;
    private final int m;
    private final int robotsCount;

    private final List<Robot> robots;

    public RobotMap(int n, int m, int robotsCount) {
        this.n = n;
        this.m = m;
        validateMap(n, m);
        this.robotsCount = robotsCount;
        validateMaxCountOfRobots(robotsCount);
        this.robots = new ArrayList<>();
    }

    public RobotMap(int n, int m) {
        this(n, m, 5);
    }

    interface Robot {
        public void changeDirection(Direction direction);
        public void move(int step);
        public void move();
        public Long getID();
        public Point getPoint();
    }

    public Robot createRobot(Point point) {
        validatePoint(point);
        Robot robot = new RobotSample(point);
        robots.add(robot);
        validateCountOfRobots(robots);
        return robot;
    }

    public void delete(Long id) {
        for (int i = 0; i < robots.size(); i++) {
            if (robots.get(i).getID() == id) {
                robots.remove(i);
            }
        }
    }

    public void acceptRobots(Consumer<Robot> robotAcceptor) {
        for (Robot robot : robots) {
            robotAcceptor.accept(robot);
        }
    }

    public Optional<Robot> getById(Long id) {
        for (Robot robot : robots) {
            if (id.equals(robot.getID())) {
                return Optional.of(robot);
            }
        }

        return Optional.empty();
    }


    private void validateMap(int n, int m) {
        if (n < 0 || m < 0) {
            throw new IllegalStateException("Некоректное значение размеров поля!");
        }
    }

    private void validateMaxCountOfRobots(int robotsCount) {
        if (robotsCount > 5) {
            throw new IllegalStateException("Роботов должно быть не более 5!");
        }
    }

    private void validateCountOfRobots(List<Robot> robots) {
        if (robots.size() > robotsCount) {
            throw new IllegalStateException("Превышено количество роботов!");
        }
    }

    private void validatePoint(Point point) {
        validatePointIsCorrect(point);
        validatePointIsFree(point);
    }

    private void validatePointIsCorrect(Point point) {
        if (point.x() < 0 || point.x() > n || point.y() < 0 || point.y() > m) {
            throw new IllegalStateException("Некоректное значение точки!");
        }
    }

    private void validatePointIsFree(Point point) {
        for (Robot robot : robots) {
            Point robotPoint = robot.getPoint();
            if (robotPoint.equals(point)) {
                throw new IllegalStateException("Точка " + point + " занята!");
            }
        }
    }

    public class RobotSample implements Robot {

        public static final Direction DEFAULT_DIRECTION = Direction.TOP;

        private static Long idCounter = 1L;

        private final Long id;
        private Direction direction;
        private Point point;

        public RobotSample(Point point) {
            this.id = idCounter++;
            this.direction = DEFAULT_DIRECTION;
            this.point = point;
        }

        @Override
        public void changeDirection(Direction direction) {
            this.direction = direction;
        }

        @Override
        public void move(int step) {
            for (int i = 0; i < step; i++) {
                Point newPoint = switch (direction) {
                    case TOP -> new Point(point.x() - 1, point.y());
                    case RIGHT -> new Point(point.x(), point.y() + 1);
                    case BOTTOM -> new Point(point.x() + 1, point.y());
                    case LEFT -> new Point(point.x(), point.y() - 1);
                    default -> new Point(point.x(), point.y());
                };
                validatePoint(newPoint);
                System.out.println("Робот переместился с " + point + " на " + newPoint);
                this.point = newPoint;
            }
        }

        @Override
        public void move() {
            move(1);
        }

        @Override
        public Long getID() {
            return id;
        }

        @Override
        public Point getPoint() {
            return point;
        }

        @Override
        public String toString() {
            return "[" + id + "] " + point.toString() + " [" + direction.name() + "]";
        }
    }

}
