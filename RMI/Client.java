import java.rmi.Naming;
import java.rmi.RemoteException;


public class Client {
    public static void main(String[] args) throws Exception {
        final MazeRMIInterface s = (MazeRMIInterface) Naming.lookup("rmi://localhost:1099/MAZE");
        //final Start s = new Start();
        System.out.println(s.getFuel(0));
        System.out.println(s.getPosition(0));
        s.setNumberOfVehicles(4);
        s.setFuel(3);

        s.setSleepTime(200);
        boolean[][] board = new boolean[3][3];

        board[1][1] = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = true;
            }
        }

        board[1][0] = false;

        s.setBoard(board);
        System.out.println(s.go(4, VehiclesInterface.Direction.SOUTH));
        System.out.println(s.go(-1, VehiclesInterface.Direction.SOUTH));
        System.out.println();


        int v = 0;
        VehiclesInterface.Position pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.EAST) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.WEST) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.NORTH) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.SOUTH) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));

        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.SOUTH) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.SOUTH) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));

        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.EAST) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        pos = s.getPosition(v);
        System.out.println(s.go(v, VehiclesInterface.Direction.EAST) + " " + "(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));

        final long time = System.currentTimeMillis();
        System.out.println();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int v = 1;
                    s.go(v, VehiclesInterface.Direction.WEST);
                    System.out.println(System.currentTimeMillis() - time);
                    s.go(v, VehiclesInterface.Direction.SOUTH);
                    System.out.println(System.currentTimeMillis() - time);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int v = 1;
                    s.go(v, VehiclesInterface.Direction.SOUTH);
                    System.out.println(System.currentTimeMillis() - time);
                    s.go(v, VehiclesInterface.Direction.EAST);
                    System.out.println(System.currentTimeMillis() - time);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        v = 1;
        Thread.sleep(800);
        pos = s.getPosition(v);
        System.out.println(System.currentTimeMillis() - time);
        System.out.println("(" + pos.getX() + "," + pos.getY() + ")" + " " + s.getFuel(v));
        System.out.println(System.currentTimeMillis() - time);
    }

}
