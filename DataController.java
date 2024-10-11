@RestController
@RequestMapping("/api/data")
public class DataController {

    @Autowired
    private DataGenerationService dataGenerationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateData(@RequestParam int count) {
        dataGenerationService.generateData(count);
        return ResponseEntity.ok(dataGenerationService.getDataCounts());
    }

    @GetMapping("/count")
    public ResponseEntity<String> getDataCounts() {
        return ResponseEntity.ok(dataGenerationService.getDataCounts());
    }
}
