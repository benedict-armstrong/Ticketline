package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

/*
@Profile("generateData")
@Component
public class TicketDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_TICKETS_TO_GENERATE = 10;

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final FileRepository fileRepository;

    public TicketDataGenerator(TicketRepository ticketRepository,
                               EventRepository eventRepository,
                               AddressRepository addressRepository,
                               UserRepository userRepository,
                               ArtistRepository artistRepository,
                               FileRepository fileRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.fileRepository = fileRepository;
    }

    @PostConstruct
    public void generateTickets() throws Exception {
        if (ticketRepository.findAll().size() > 0) {
            LOGGER.debug("Tickets have already been generated");
        } else {
            LOGGER.debug("Generating {} tickets", NUMBER_OF_TICKETS_TO_GENERATE);

            Address performanceAddress = Address.builder()
                .name("Ticket Performance Address")
                .lineOne("Main Str")
                .postcode("1010")
                .city("Vienna")
                .country("Austria")
                .build();
            Address savedPerformanceAddress = addressRepository.save(performanceAddress);

            Artist artist = Artist.builder()
                .firstName("Ticket Perf. Artist")
                .lastName("Smith")
                .build();
            Artist savedArtist = artistRepository.save(artist);

            File image = generateImage();
            File savedImage = fileRepository.save(image);

            Event event = generateEvent(savedPerformanceAddress, savedArtist, savedImage);
            Performance performance = (Performance) event.getPerformances().toArray()[0];
            performance.setLocation(savedPerformanceAddress);
            Event savedEvent = eventRepository.save(event);

            Performance savedPerformance = (Performance) savedEvent.getPerformances().toArray()[0];
            TicketType ticketType = (TicketType) savedPerformance.getTicketTypes().toArray()[0];
            for (int i = 0; i < NUMBER_OF_TICKETS_TO_GENERATE; i++) {
                Address address = Address.builder()
                    .name("Ticket Buyer's Address " + i)
                    .lineOne("Main Str")
                    .postcode("1010")
                    .city("Vienna")
                    .country("Austria")
                    .build();
                ApplicationUser user = ApplicationUser.builder()
                    .email("ticketBuyer" + i + "@gmail.com")
                    .password("TicketBuyer " + i)
                    .role(ApplicationUser.UserRole.CLIENT)
                    .status(ApplicationUser.UserStatus.ACTIVE)
                    .firstName("Ticket Buyer " + i)
                    .lastName("Smith")
                    .telephoneNumber("012345" + i)
                    .address(address)
                    .lastLogin(LocalDateTime.now())
                    .build();
                ApplicationUser savedUser = userRepository.save(user);

                List<Long> seats = new ArrayList<>();
                seats.add(10L);
                Ticket ticket = Ticket.builder()
                    .owner(savedUser)
                    .performance(savedPerformance)
                    .ticketType(ticketType)
                    .status(Ticket.Status.IN_CART)
                    .updateDate(LocalDateTime.now().plusDays(1L))
                    .seats(seats)
                    .build();

                ticketRepository.save(ticket);
            }
        }
    }

    private Event generateEvent(Address address, Artist artist, File image) throws Exception {
        SectorType sectorType = SectorType.builder()
            .name("Tickt. Perf. Standing")
            .numberOfTickets(500)
            .build();
        Set<SectorType> sectorTypes = new HashSet<>();
        sectorTypes.add(sectorType);
        Set<TicketType> ticketTypes = new HashSet<>();
        ticketTypes.add(
            TicketType.builder()
                .title("TicketGen")
                .price(1000L)
                .sectorType(sectorType)
                .build()
        );
        Set<Performance> performances = new HashSet<>();
        performances.add(
            Performance.builder()
                .title("Performance for Tickets")
                .description("This is a performance generated by TicketDataGenerator")
                .date(LocalDateTime.now().plusDays(10))
                .location(
                    address
                )
                .artist(
                    artist
                )
                .sectorTypes(sectorTypes)
                .ticketTypes(ticketTypes)
                .build()
        );
        Set<File> images = new HashSet<>();
        images.add(image);
        return Event.builder()
            .name("Tickt. Event")
            .description("This is an event generated by TicketDataGenerator")
            .performances(performances)
            .duration(600)
            .eventType(Event.EventType.CONCERT)
            .startDate(LocalDate.now().plusDays(10))
            .endDate(LocalDate.now().plusDays(20))
            .images(images)
            .build();
    }

    private File generateImage() throws Exception {
        return File.builder()
            .data(recoverImageFromUrl("https://cdn.pixabay.com/photo/2015/05/15/14/50/concert-768722_1280.jpg"))
            .type(File.Type.IMAGE_JPG)
            .build();
    }

    private byte[] recoverImageFromUrl(String urlText) throws Exception {
        URL url = new URL(urlText);
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream inputStream = url.openStream()) {
            int n = 0;
            byte [] buffer = new byte[1024];
            while (-1 != (n = inputStream.read(buffer))) {
                output.write(buffer, 0, n);
            }
        }

        return output.toByteArray();
    }

}*/
