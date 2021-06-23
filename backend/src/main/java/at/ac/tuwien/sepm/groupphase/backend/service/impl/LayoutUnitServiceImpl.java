package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LayoutUnitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LayoutUnitServiceImpl implements LayoutUnitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final LayoutUnitRepository layoutUnitRepository;

    public LayoutUnitServiceImpl(LayoutUnitRepository layoutUnitRepository) {
        this.layoutUnitRepository = layoutUnitRepository;
    }

    @Override
    public LayoutUnit add(LayoutUnit layoutUnit) {
        LOGGER.trace("add({})", layoutUnit);
        return layoutUnitRepository.save(layoutUnit);
    }

    @Override
    public List<LayoutUnit> saveAll(Set<LayoutUnit> layoutUnits) {
        LOGGER.trace("saveAll({})", layoutUnits);
        return layoutUnitRepository.saveAll(layoutUnits);
    }

    @Override
    public LayoutUnit findById(Long id) {
        LOGGER.trace("findById({})", id);
        Optional<LayoutUnit> layoutUnit = layoutUnitRepository.findById(id);
        return layoutUnit.orElse(null);
    }
}
