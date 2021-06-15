package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.LayoutUnit;
import at.ac.tuwien.sepm.groupphase.backend.repository.LayoutUnitRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.LayoutUnitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class LayoutUnitServiceImpl implements LayoutUnitService {
    private final LayoutUnitRepository layoutUnitRepository;

    public LayoutUnitServiceImpl(LayoutUnitRepository layoutUnitRepository) {
        this.layoutUnitRepository = layoutUnitRepository;
    }

    @Override
    public LayoutUnit add(LayoutUnit layoutUnit) {
        return layoutUnitRepository.save(layoutUnit);
    }

    @Override
    public List<LayoutUnit> saveAll(Set<LayoutUnit> layoutUnits) {
        return layoutUnitRepository.saveAll(layoutUnits);
    }
}
