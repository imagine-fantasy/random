@Entity
@Table(name = "card_form_factors", schema = "cc_cards")
public class CardFormFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "card_creation_id", nullable = false)
    private CardCreation cardCreation;

    @Column(name = "form_factor", nullable = false)
    private String formFactor;

    // Getters and setters
}