package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.repository.AddressRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomAddressService implements AddressService {
    private final AddressRepository addressRepository;

    @Autowired
    public CustomAddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public List<Address> findAllByCriteria(Pageable pageRequest) {
        return addressRepository.findAllByEventLocationTrue(pageRequest).getContent();
    }

    @Override
    public Address addAddress(Address address) {
        return addressRepository.save(address);
    }
}
