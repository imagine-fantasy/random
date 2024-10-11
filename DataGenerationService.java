package com.yourcompany.ccards.service;

import com.github.javafaker.Faker;
import com.yourcompany.ccards.model.Cardholder;
import com.yourcompany.ccards.model.CardCreation;
import com.yourcompany.ccards.repository.CardholderRepository;
import com.yourcompany.ccards.repository.CardCreationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class DataGenerationService {

    @Autowired
    private CardholderRepository cardholderRepository;

    @Autowired
    private CardCreationRepository cardCreationRepository;

    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Transactional
    public void generateData(int count) {
        List<Cardholder> cardholders = new ArrayList<>();
        List<CardCreation> cardCreations = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Cardholder cardholder = createRandomCardholder();
            cardholders.add(cardholder);

            int cardCreationsCount = random.nextInt(3) + 1; // 1 to 3 card creations per cardholder
            for (int j = 0; j < cardCreationsCount; j++) {
                cardCreations.add(createRandomCardCreation(cardholder));
            }

            // Batch insert every 1000 records
            if (i % 1000 == 0 && i > 0) {
                cardholderRepository.saveAll(cardholders);
                cardCreationRepository.saveAll(cardCreations);
                cardholders.clear();
                cardCreations.clear();
            }
        }

        // Insert any remaining records
        if (!cardholders.isEmpty()) {
            cardholderRepository.saveAll(cardholders);
            cardCreationRepository.saveAll(cardCreations);
        }
    }

    private Cardholder createRandomCardholder() {
        Cardholder cardholder = new Cardholder();
        cardholder.setFirstName(faker.name().firstName());
        cardholder.setLastName(faker.name().lastName());
        cardholder.setEmail(faker.internet().emailAddress());
        cardholder.setCompany(faker.company().name());
        cardholder.setCreditLimit(BigDecimal.valueOf(random.nextInt(90000) + 10000)); // 10000 to 100000
        cardholder.setCreatedAt(OffsetDateTime.now());
        return cardholder;
    }

    private CardCreation createRandomCardCreation(Cardholder cardholder) {
        CardCreation cardCreation = new CardCreation();
        cardCreation.setCardholder(cardholder);
        cardCreation.setRequestDate(OffsetDateTime.now().minusDays(random.nextInt(30))); // Random date within last 30 days
        cardCreation.setProcessorResponse(generateRandomProcessorResponse());
        cardCreation.setStatus(random.nextBoolean() ? "APPROVED" : "DECLINED");
        return cardCreation;
    }

    private String generateRandomProcessorResponse() {
        String cardNumber = faker.business().creditCardNumber();
        String expirationDate = faker.business().creditCardExpiry();
        String cvv = faker.number().digits(3);
        String approvalCode = faker.number().digits(6);

        return String.format("{\"card_number\": \"%s\", \"expiration_date\": \"%s\", \"cvv\": \"%s\", \"approval_code\": \"%s\"}",
                cardNumber, expirationDate, cvv, approvalCode);
    }

     public List<Cardholder> getAllCardholders() {
        return cardholderRepository.findAll();
    }

    public List<CardCreation> getAllCardCreations() {
        return cardCreationRepository.findAll();
    }

    public long getCardholderCount() {
        return cardholderRepository.count();
    }

    public long getCardCreationCount() {
        return cardCreationRepository.count();
    }

    // Helper method to get counts of both entities
    public String getDataCounts() {
        long cardholderCount = getCardholderCount();
        long cardCreationCount = getCardCreationCount();
        return String.format("Cardholders: %d, Card Creations: %d", cardholderCount, cardCreationCount);
    }
}
