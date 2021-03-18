package by.omeganessy.mtt.util;

import by.omeganessy.mtt.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FIleReaderUtil {
    static Logger logger = LogManager.getLogger();

    public List<String> readFile(String fileName) throws CustomException {
        if (fileName == null || fileName.isEmpty()) {
            throw new CustomException("Filename is Empty");
        }
        List<String> data;
        final Path path = Paths.get(fileName);

        try (Stream<String> lineStream = Files.lines(path)) {
            data = lineStream.collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("File not found: {}", fileName);
            throw new CustomException("File " + fileName + " not found");
        }
        logger.debug("Data from file: {}" , data);
        return data;
    }
}
