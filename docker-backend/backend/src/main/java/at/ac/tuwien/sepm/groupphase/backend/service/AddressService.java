package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AddressService {

    /**
     * get one address by id.
     *
     * @param id of the address
     * @return the address with id
     */
    Address getOneById(Long id);

    /**
     * find all addresses.
     *
     * @param pageRequest the pagination
     * @return list of all addresses
     */
    List<Address> findAllEventLocations(Pageable pageRequest);

    /**
     * add a new address.
     *
     * @param address to be added
     * @return the added address with id set
     */
    Address addAddress(Address address);

    /**
     * Returns a list of addresses which match the search values.
     *
     * @param address with the values to search
     * @return list of addresses with all possible values
     */
    List<Address> search(Address address, Pageable pageable);
}
