@Service
public class SimplifiedCardService {

    private final CardholderRepository cardholderRepository;
    private final CardCreationRepository cardCreationRepository;
    private final UniqueCardNumberGenerator cardNumberGenerator;
    private final Faker faker = new Faker();

    public SimplifiedCardService(CardholderRepository cardholderRepository,
                                 CardCreationRepository cardCreationRepository,
                                 UniqueCardNumberGenerator cardNumberGenerator) {
        this.cardholderRepository = cardholderRepository;
        this.cardCreationRepository = cardCreationRepository;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @Transactional
    public String createCard(CardCreationRequestDTO request) {
        Cardholder cardholder = createCardholder(request);
        CardCreation cardCreation = createCardCreation(cardholder, request);

        cardholderRepository.save(cardholder);
        cardCreationRepository.save(cardCreation);

        return cardCreation.getCardNumber();
    }

    private Cardholder createCardholder(CardCreationRequestDTO request) {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName(request.getFirstName());
        cardholder.setLastName(request.getLastName());
        cardholder.setEmail(request.getEmail());
        cardholder.setCompany(request.getCompany());
        cardholder.setCreditLimit(request.getCreditLimit());
        cardholder.setCreatedAt(OffsetDateTime.now());
        return cardholder;
    }

    private CardCreation createCardCreation(Cardholder cardholder, CardCreationRequestDTO request) {
        CardCreation cardCreation = new CardCreation();
        cardCreation.setCardholder(cardholder);
        cardCreation.setRequestDate(OffsetDateTime.now());
        cardCreation.setProcessorResponse(generateProcessorResponse(request.getCardTemplateId()));
        cardCreation.setStatus("APPROVED");
        cardCreation.setCardNumber(cardNumberGenerator.generateUniqueCardNumber());

        for (String formFactor : request.getFormFactors()) {
            CardFormFactor cardFormFactor = new CardFormFactor();
            cardFormFactor.setFormFactor(formFactor);
            cardCreation.addFormFactor(cardFormFactor);
        }

        return cardCreation;
    }

    private String generateProcessorResponse(String cardTemplateId) {
        String approvalCode = faker.number().digits(6);
        return String.format("{\"approval_code\": \"%s\", \"card_template_id\": \"%s\"}", approvalCode, cardTemplateId);
    }
}