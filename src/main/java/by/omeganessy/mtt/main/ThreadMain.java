package by.omeganessy.mtt.main;

import by.omeganessy.mtt.entity.Lorry;
import by.omeganessy.mtt.entity.Vehicle;
import by.omeganessy.mtt.exception.CustomException;
import by.omeganessy.mtt.parser.DataParser;
import by.omeganessy.mtt.util.FIleReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class ThreadMain {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) throws CustomException {
        Lorry lorry = Lorry.getInstance();
        lorry.setSquare(300);
        lorry.setCarring(150);
        lorry.setParkingPlaceSquare(13.25);
        lorry.setFreeParkingPlacesCount(lorry.findParkingPlacesCount(lorry.getSquare(), false));
        logger.debug("Ferry: " + lorry);
        logger.debug("Count of parking places = " + lorry.findParkingPlacesCount(lorry.getSquare(), false));
        lorry.start();
        final String FILE_PATH = "resources/data/data.txt";
        List<String> vehicles;
        FIleReaderUtil reader = new FIleReaderUtil();
        DataParser parser = new DataParser();
        vehicles = reader.readFile(FILE_PATH);
        logger.debug("----- Start loading automobiles ------");
        for (String item: vehicles) {
            Vehicle vehicle = parser.parser(item);
            vehicle.start();
        }
    }
}
