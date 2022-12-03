package pl.spkteam.worklifeintegrationserver.task.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.spkteam.worklifeintegrationserver.task.dto.EntityListDto;
import pl.spkteam.worklifeintegrationserver.task.dto.PlaceDto;
import pl.spkteam.worklifeintegrationserver.task.mapper.PlaceMapper;
import pl.spkteam.worklifeintegrationserver.task.repo.PlaceRepository;
import pl.spkteam.worklifeintegrationserver.task.service.PlaceService;


@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;
    private final PlaceService placeService;
    private final PlaceMapper placeMapper;

    @GetMapping
    public EntityListDto<PlaceDto> getPlaces() {
        var dtoList = placeService.getPlaces().stream().map(placeMapper::mapPlaceEntityToDto).toList();
        return new EntityListDto<>(dtoList);
    }

    @PostMapping
    public PlaceDto savePlace(@RequestBody @Valid PlaceDto placeDto) {
        var place = placeRepository.save(placeMapper.mapPlaceDtoToEntity(placeDto));
        return placeMapper.mapPlaceEntityToDto(place);
    }

    @DeleteMapping("/{id}")
    public void deletePlace(@PathVariable("id") Long id) {
        placeRepository.deleteById(id);
    }
}
