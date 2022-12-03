package pl.spkteam.worklifeintegrationserver.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityListDto;
import pl.spkteam.worklifeintegrationserver.task.dto.PlacementLimitDto;
import pl.spkteam.worklifeintegrationserver.task.mapper.PlacementLimitMapper;
import pl.spkteam.worklifeintegrationserver.task.repo.PlacementLimitRepository;
import pl.spkteam.worklifeintegrationserver.task.service.PlacementLimitService;


@RestController
@RequestMapping("/placementLimit")
@RequiredArgsConstructor
public class PlacementLimitController {

    private final PlacementLimitRepository placementLimitRepository;
    private final PlacementLimitService placementLimitService;
    private final PlacementLimitMapper placementLimitMapper;

    @GetMapping
    public EntityListDto<PlacementLimitDto> getPlacementLimits() {
        var dtoList = placementLimitService.getPlacementLimits().stream().map(placementLimitMapper::mapPlacementLimitEntityToDto).toList();
        return new EntityListDto<>(dtoList);
    }

    @PostMapping
    public PlacementLimitDto savePlace(@RequestBody @Valid PlacementLimitDto placementLimitDto) {
        var placementLimit = placementLimitRepository.save(placementLimitMapper.mapPlacementLimitDtoToEntity(placementLimitDto));
        return placementLimitMapper.mapPlacementLimitEntityToDto(placementLimit);
    }

    @DeleteMapping("/{id}")
    public void deletePlacementLimit(@PathVariable("id") Long id) {
        placementLimitRepository.deleteById(id);
    }
}
