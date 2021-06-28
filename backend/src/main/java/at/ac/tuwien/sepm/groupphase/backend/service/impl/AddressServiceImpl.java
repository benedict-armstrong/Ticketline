package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AddressService;
import at.ac.tuwien.sepm.groupphase.backend.specification.AddressSpecificationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final AddressRepository addressRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address getOneById(Long id) {
        LOGGER.trace("getOneById({})", id);
        return addressRepository.findOneById(id);
    }

    @Override
    public List<Address> findAllEventLocations(Pageable pageRequest) {
        LOGGER.trace("findAllEventLocations({})", pageRequest);
        return addressRepository.findAllByEventLocationTrue(pageRequest).getContent();
    }

    @Override
    public Address addAddress(Address address) {
        LOGGER.trace("addAddress({})", address);
        return addressRepository.save(address);
    }

    @Override
    public List<Address> search(Address address, Pageable pageable) {
        LOGGER.trace("searchArtist({}, {}, {}, {}, {}, {})", address.getName(), address.getLineOne(), address.getLineTwo(), address.getCity(), address.getPostcode(), address.getCountry());

        AddressSpecificationBuilder builder = new AddressSpecificationBuilder();

        if (address.getName() != null) {
            builder.with("name", ":", address.getName());
        }
        if (address.getLineOne() != null) {
            builder.with("lineOne", ":", address.getLineOne());
        }
        if (address.getLineTwo() != null) {
            builder.with("lineTwo", ":", address.getLineTwo());
        }
        if (address.getCity() != null) {
            builder.with("city", ":", address.getCity());
        }
        if (address.getPostcode() != null) {
            builder.with("postCode", ":", address.getPostcode());
        }
        if (address.getCountry() != null) {
            builder.with("country", ":", address.getCountry());
        }

        builder.with("eventLocation", "?",  true);

        return addressRepository.findAll(builder.build(), pageable).getContent();
    }
}
