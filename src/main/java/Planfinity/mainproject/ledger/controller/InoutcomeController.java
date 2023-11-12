package Planfinity.mainproject.ledger.controller;

import Planfinity.mainproject.ledger.InoutcomeDto.InoutcomePatchDto;
import Planfinity.mainproject.ledger.InoutcomeDto.InoutcomePostDto;
import Planfinity.mainproject.ledger.InoutcomeDto.InoutcomeResponseDto;
import Planfinity.mainproject.ledger.domain.Inoutcome;
import Planfinity.mainproject.ledger.service.InoutcomeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inoutcomes")
public class InoutcomeController {

    private final InoutcomeService inoutcomeService;

    public InoutcomeController(InoutcomeService inoutcomeService) {
        this.inoutcomeService = inoutcomeService;
    }

    @PostMapping
    public ResponseEntity<InoutcomeResponseDto> createInoutcome(
            @Valid @RequestBody InoutcomePostDto inoutcomePostDto) {
        Inoutcome createdInoutcome = inoutcomeService.createInoutcome(inoutcomePostDto);
        InoutcomeResponseDto responseDto = new InoutcomeResponseDto(createdInoutcome);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{in-outcome-id}")
    public ResponseEntity<InoutcomeResponseDto> updateInoutcome(
            @PathVariable("in-outcome-id") @Positive Long inoutcomeId,
            @Validated @RequestBody InoutcomePatchDto inoutcomePatchDto) {
        Inoutcome updatedInoutcome = inoutcomeService.updateInoutcome(inoutcomeId, inoutcomePatchDto);
        InoutcomeResponseDto responseDto = new InoutcomeResponseDto(updatedInoutcome);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{in-outcome-id}")
    public ResponseEntity<InoutcomeResponseDto> getInoutcome(
            @PathVariable("in-outcome-id") @Positive Long inoutcomeId) {
        Inoutcome inoutcome = inoutcomeService.getInoutcome(inoutcomeId);
        InoutcomeResponseDto responseDto = new InoutcomeResponseDto(inoutcome);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<InoutcomeResponseDto>> getInoutcomes() {
        List<Inoutcome> inoutcomes = inoutcomeService.getInoutcomes();
        List<InoutcomeResponseDto> responseDtoList = inoutcomes.stream()
                .map(InoutcomeResponseDto::new)
                .collect(Collectors.toList());
        return new ResponseEntity<>(responseDtoList, HttpStatus.OK);
    }

    @DeleteMapping("/{in-outcome-id}")
    public ResponseEntity<Void> deleteInoutcome(
            @PathVariable("in-outcome-id") @Positive Long inoutcomeId) {
        inoutcomeService.deleteInoutcome(inoutcomeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

