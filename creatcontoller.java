@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCard(@RequestBody CardCreationRequestDTO request) {
        String cardNumber = cardService.createCard(request);
        return ResponseEntity.ok("Card created successfully. Card number: " + cardNumber);
    }
}