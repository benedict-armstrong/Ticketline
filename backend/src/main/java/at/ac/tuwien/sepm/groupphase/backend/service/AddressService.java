package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    /**
     * find all addresses.
     *
     * @param pageRequest the pagination
     * @return list of all addresses
     */
    List<Address> findAllByCriteria(Pageable pageRequest);

    /**
     * add a new address.
     *
     * @param address to be added
     * @return the added address with id set
     */
    Address addAddress(Address address);
}
