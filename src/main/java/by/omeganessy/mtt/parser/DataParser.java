package by.omeganessy.mtt.parser;

import by.omeganessy.mtt.entity.Type;
import by.omeganessy.mtt.entity.Vehicle;
import by.omeganessy.mtt.exception.CustomException;

public class DataParser {
    private final String DELIMITER = " ";

    public Vehicle parser(String vehicleDataString) throws CustomException {
        if (vehicleDataString == null || vehicleDataString.isEmpty()) {
            throw new CustomException("Filename is Empty");
        }
        String[] vehicleParameters = vehicleDataString.split(DELIMITER);
        String vehicleNumber = vehicleParameters[0];
        double size = Double.parseDouble(vehicleParameters[1]);
        double weight = Double.parseDouble(vehicleParameters[2]);
        Type type;

        switch (vehicleParameters[3]) {
            case "TRUCK":
                type = Type.TRUCK;
                break;
            case "CAR":
                type = Type.CAR;
                break;
            default:
                throw new CustomException("No such vehicle like: " + vehicleParameters[3]);
        }
        return new Vehicle(vehicleNumber,size,weight,type);
    }
}
