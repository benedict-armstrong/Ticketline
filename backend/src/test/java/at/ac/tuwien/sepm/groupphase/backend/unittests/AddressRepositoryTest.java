package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestDataAddress;
import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.specification.AddressSpecificationBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// This test slice annotation is used instead of @SpringBootTest to load only repository beans instead of
// the entire application context
@DataJpaTest
@ActiveProfiles("test")
public class AddressRepositoryTest implements TestDataAddress {
    @Autowired
    AddressRepository addressRepository;

    private Address address;

    @BeforeEach
    public void beforeEach(){
        address = Address.builder()
            .name(TEST_ADDRESS_NAME)
            .lineOne(TEST_ADDRESS_LINE_ONE)
            .city(TEST_ADDRESS_CITY)
            .postcode(TEST_ADDRESS_POSTCODE)
            .country(TEST_ADDRESS_COUNTRY)
            .build();
    }

    @AfterEach
    public void afterEach() {
        addressRepository.deleteAll();
    }

    @Test
    @DisplayName("Should return address when searching for address")
    public void givenAddress_whenSearch_ShouldReturnAddress() {
        addressRepository.save(address);

        AddressSpecificationBuilder builder = new AddressSpecificationBuilder();
        builder.with("name", ":", address.getName());

        assertEquals(address, addressRepository.findAll(builder.build()).get(0));
    }
}
