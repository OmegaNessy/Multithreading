package by.omeganessy.mtt.entity;

import by.omeganessy.mtt.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Vehicle extends Thread{
    public static final Logger logger = LogManager.getLogger();

    private String vehicleNumber;
    private double size;
    private double weight;
    private Type type;

    public Vehicle(String vehicleNumber,double size,double weight,Type type){
        this.vehicleNumber = vehicleNumber;
        this.size = size;
        this.weight = weight;
        this.type = type;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public void run() {
        Lorry lorry = Lorry.getInstance();
        try {
            lorry.loading(this);
        } catch (CustomException e) {
            logger.info("Catched FarryException: {}", e);
        }
        try {
            lorry.unloading();
        } catch (CustomException e) {
            logger.error("Err");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle vehicle = (Vehicle) o;
        return Double.compare(vehicle.size, size) == 0 &&
                Double.compare(vehicle.weight, weight) == 0 &&
                vehicleNumber == vehicle.vehicleNumber || (vehicleNumber!=null && vehicleNumber.equals(vehicle.vehicleNumber)) &&
                type.equals(vehicle.type);
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((vehicleNumber == null) ? 0 : vehicleNumber.hashCode());
        result = PRIME * result + (int) size;
        result = PRIME * result + (int) weight;
        result = PRIME * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("Vehicle {");
        string.append("Number of Vehical:");
        string.append(vehicleNumber);
        string.append(", Size: ");
        string.append(size);
        string.append(", Weight: ");
        string.append(weight);
        string.append(", Type: ");
        string.append(type);
        string.append("}");
        return string.toString();
    }
}
