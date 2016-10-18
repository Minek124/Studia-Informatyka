import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;

class Start extends java.rmi.server.UnicastRemoteObject implements MazeRMIInterface {

    boolean[][] board;
    long sleepTime = 0;
    int nVehicles = -1;
    int startFuel = -1;
    Vehicle[] vehicles;

    class Vehicle {
        public int fuel = -1;
        public VehiclesInterface.Position pos;
    }

    public static void main(String args[]) throws Exception {
        System.out.println("Binding MAZE...");
        MazeRMIInterface service = new Start();
        Context namingContext = new InitialContext();
        namingContext.rebind("rmi:MAZE", service);
        System.out.println("MAZE bound.");
    }

    protected Start() throws RemoteException {
        super();
    }

    @Override
    public void setBoard(boolean[][] board) throws RemoteException {
        this.board = board;
    }

    @Override
    public void setSleepTime(long msec) throws RemoteException {
        this.sleepTime = msec;
    }

    @Override
    public void setNumberOfVehicles(int vehicles) throws RemoteException {
        this.nVehicles = vehicles;
        this.vehicles = new Vehicle[nVehicles];
        for (int i = 0; i < nVehicles; i++) {
            this.vehicles[i] = new Vehicle();
            this.vehicles[i].pos = new VehiclesInterface.Position(0, 0);
            this.vehicles[i].fuel = startFuel;
        }
    }

    @Override
    public void setFuel(int level) throws RemoteException {
        this.startFuel = level;
        for (int i = 0; i < nVehicles; i++) {
            vehicles[i].fuel = startFuel;
        }
    }

    @Override
    public int getFuel(int vehicleID) throws RemoteException {
        if (vehicleID >= 0 && vehicleID < nVehicles) {
            synchronized (vehicles[vehicleID]) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println("Nie wolno przerywać");
                }
                return vehicles[vehicleID].fuel;
            }
        }
        return Integer.MIN_VALUE;

    }

    @Override
    public VehiclesInterface.Position getPosition(int vehicleID) throws RemoteException {
        if (vehicleID >= 0 && vehicleID < nVehicles) {
            synchronized (vehicles[vehicleID]) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println("Nie wolno przerywać");
                }
                return vehicles[vehicleID].pos;
            }
        }
        return null;
    }

    @Override
    public boolean go(int vehicleID, VehiclesInterface.Direction d) throws RemoteException {
        int x, y;
        if (vehicleID >= 0 && vehicleID < nVehicles) {
            synchronized (vehicles[vehicleID]) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    System.out.println("Nie wolno przerywać");
                }

                switch (d) {
                    case EAST:
                        x = vehicles[vehicleID].pos.getX();
                        y = vehicles[vehicleID].pos.getY();
                        if (x + 1 > board.length - 1) {
                            return false;
                        }
                        if (board[x + 1][y] && vehicles[vehicleID].fuel > 0) {
                            vehicles[vehicleID].pos = new VehiclesInterface.Position(x + 1, y);
                            vehicles[vehicleID].fuel -= 1;
                            return true;
                        }
                        break;
                    case WEST:
                        x = vehicles[vehicleID].pos.getX();
                        y = vehicles[vehicleID].pos.getY();
                        if (x - 1 < 0) {
                            return false;
                        }
                        if (board[x - 1][y] && vehicles[vehicleID].fuel > 0) {
                            vehicles[vehicleID].pos = new VehiclesInterface.Position(x - 1, y);
                            vehicles[vehicleID].fuel -= 1;
                            return true;
                        }
                        break;
                    case SOUTH:
                        x = vehicles[vehicleID].pos.getX();
                        y = vehicles[vehicleID].pos.getY();
                        if (y + 1 > board.length - 1) {
                            return false;
                        }
                        if (board[x][y + 1] && vehicles[vehicleID].fuel > 0) {
                            vehicles[vehicleID].pos = new VehiclesInterface.Position(x, y + 1);
                            vehicles[vehicleID].fuel -= 1;
                            return true;
                        }
                        break;
                    case NORTH:
                        x = vehicles[vehicleID].pos.getX();
                        y = vehicles[vehicleID].pos.getY();
                        if (y - 1 < 0) {
                            return false;
                        }
                        if (board[x][y - 1] && vehicles[vehicleID].fuel > 0) {
                            vehicles[vehicleID].pos = new VehiclesInterface.Position(x, y - 1);
                            vehicles[vehicleID].fuel -= 1;
                            return true;
                        }
                        break;
                }
            }
        }
        return false;
    }
}
