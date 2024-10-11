import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataGenerationController {

    @Autowired
    private DataGenerationService dataGenerationService;

    @PostMapping("/generateData/{count}")
    public String generateData(@PathParam("count") int count) {
        long startTime = System.currentTimeMillis();
        dataGenerationService.generateData(count);
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        return String.format("Generated %d records in %d ms", count, duration);
    }
}
