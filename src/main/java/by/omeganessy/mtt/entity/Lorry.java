package by.omeganessy.mtt.entity;

import by.omeganessy.mtt.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Lorry extends Thread{
    private static Logger logger = LogManager.getLogger();

    private static final int MOVING = 3;

    private static Lorry instance;
    private static Lock locker = new ReentrantLock(true);
    private double square;
    private double carring;
    private double parkingPlaceSquare;
    private int freeParkingPlacesCount;
    private static double currentWeight;
    private static Deque<Vehicle> queue = new ConcurrentLinkedDeque<>();
    private final Condition condition = locker.newCondition();
    private State state;

    public enum State {
        LOADING, TRANSPORTING, UNLOADING
    }


    private Lorry(){
    }

    public Lorry(double square, double carring, double parkingPlaceSquare, State state) {
        this.square = square;
        this.carring = carring;
        this.parkingPlaceSquare = parkingPlaceSquare;
        freeParkingPlacesCount = findParkingPlacesCount(square, false);
        this.state = state;
    }



    public static Lorry getInstance(){
        if(instance == null){
            locker.lock();
            if (instance==null){
                instance = new Lorry();
            }
            locker.unlock();
        }
        return instance;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public double getCarring() {
        return carring;
    }

    public void setCarring(double carring) {
        this.carring = carring;
    }

    public double getParkingPlaceSquare() {
        return parkingPlaceSquare;
    }

    public void setParkingPlaceSquare(double parkingPlaceSquare) {
        this.parkingPlaceSquare = parkingPlaceSquare;
    }

    public int getFreeParkingPlacesCount() {
        return freeParkingPlacesCount;
    }

    public void setFreeParkingPlacesCount(int freeParkingPlacesCount) {
        this.freeParkingPlacesCount = freeParkingPlacesCount;
    }

    public State getStateLorry() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void moving() throws CustomException {
        try {
            locker.lock();
            logger.debug("----- Ferry started moving. -----");
            try {
                TimeUnit.SECONDS.sleep(MOVING);
            } catch (InterruptedException e) {
                throw new CustomException("Thread was interrupted");
            }
            logger.debug("----- Ferry stopped moving. -----");
        } finally {
            locker.unlock();
        }
    }

    public boolean loading(Vehicle vehicle) throws CustomException{
        try {
            locker.lock();
            // Get parking places count for loading auto
            int parkingPlacesCount = findParkingPlacesCount(vehicle.getSize(), true);
            boolean result = false;

            if ((parkingPlacesCount <= freeParkingPlacesCount) && vehicle.getWeight() <= carring - currentWeight) {
                addToQueue(vehicle);
                result = true;

                freeParkingPlacesCount -= parkingPlacesCount;
                currentWeight += vehicle.getWeight();

                logger.debug("Auto " + vehicle.getVehicleNumber() + ", Free places: " + freeParkingPlacesCount + ", Current Weight: " + round(currentWeight, 2));
            } else {
                logger.debug("----- Stop loading automobiles ------");
                setState(State.UNLOADING);
            }
            return result;
        } finally {
            locker.unlock();
        }
    }
    public void addToQueue(Vehicle vehicle) {
        queue.add(vehicle);
    }

    public void unloading () throws CustomException {
        try {
            locker.lock();
            logger.debug("----- Start unloading automobiles ------");
            while (!queue.isEmpty()) {
                Vehicle vehicle = queue.pollLast();
                int parkingPlacesCount = findParkingPlacesCount(vehicle.getSize(), true);

                freeParkingPlacesCount += parkingPlacesCount;
                currentWeight -= vehicle.getWeight();

                logger.debug("Auto " + vehicle.getVehicleNumber() + ", Free places: " + freeParkingPlacesCount + ", Current Weight: " + round(currentWeight, 2));
            }
            logger.debug("----- Stop unloading automobiles ------");
        } finally {
            locker.unlock();
        }
    }

    public int findParkingPlacesCount(double square,boolean isCeil){
        int result;
        double parkingPlacesCount = square / parkingPlaceSquare;

        if (isCeil) {
            result = (int) Math.ceil(parkingPlacesCount);
        } else {
            result = (int) parkingPlacesCount;
        }

        return result;
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
