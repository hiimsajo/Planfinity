package Planfinity.mainproject.ledger.controller;

import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledger.ledgerDto.LedgerPatchDto;
import Planfinity.mainproject.ledger.ledgerDto.LedgerPostDto;
import Planfinity.mainproject.ledger.ledgerDto.LedgerResponseDto;
import Planfinity.mainproject.ledger.service.LedgerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ledgergroups/{ledger-group-id}/ledgers")
public class LedgerController {

    private final LedgerService ledgerService;

    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    @PostMapping
    public ResponseEntity<LedgerResponseDto> createLedger(
            @PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
            @Validated @RequestBody LedgerPostDto postDto) {
        Ledger ledger = ledgerService.createLedger(ledgerGroupId, postDto);

        return new ResponseEntity<>(new LedgerResponseDto(ledger), HttpStatus.CREATED);
    }
    @PatchMapping("/{ledger-id}")
    public ResponseEntity patchLedger(@PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
                                      @PathVariable("ledger-id") @Positive Long ledgerId,
                                      @Valid @RequestBody LedgerPatchDto patchDto) {
        Ledger ledger = ledgerService.updateLedger(ledgerGroupId, ledgerId, patchDto);

        return new ResponseEntity(new LedgerResponseDto(ledger), HttpStatus.OK);
    }

    @GetMapping("/{ledger-id}")
    public ResponseEntity getLedger(@PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
                                    @PathVariable("ledger-id") @Positive Long ledgerId) {
        Ledger ledger = ledgerService.getLedger(ledgerGroupId, ledgerId);
        return new ResponseEntity(new LedgerResponseDto(ledger), HttpStatus.OK);
    }//, true, true, true

    @GetMapping()
    public ResponseEntity<List<LedgerResponseDto>> getLedgers(@PathVariable("ledger-group-id") @Positive Long ledgerGroupId) {
        List<Ledger> ledgers = this.ledgerService.getLedgers(ledgerGroupId);
        List<LedgerResponseDto> responses = ledgers.stream()
                .map((ledger -> new LedgerResponseDto(ledger)))
                .collect(Collectors.toList());

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/dates")
    public ResponseEntity<List<LedgerResponseDto>> getLedgersBetweenDates(
            @PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Ledger> ledgers = ledgerService.getLedgersByDate(ledgerGroupId, start, end);
            List<LedgerResponseDto> responseDtos = ledgers.stream()
                    .map(LedgerResponseDto::new)
                    .collect(Collectors.toList());

            return new ResponseEntity<>(responseDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/totals")
    public ResponseEntity<Long> getTotalAmountByDate(
            @PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            List<Ledger> ledgers = ledgerService.getLedgersByDate(ledgerGroupId, start, end);

            // 수입이 붙은 가계부의 금액 합과 지출이 붙은 가계부의 금액 합계
            Long totalIncome = ledgers.stream()
                    .filter(ledger -> ledger.getInoutcome() != null)
                    .filter(ledger -> "수입".equals(ledger.getInoutcome().getInoutcomeName()))
                    .mapToLong(Ledger::getLedgerAmount)
                    .sum();

            Long totalOutcome = ledgers.stream()
                    .filter(ledger -> ledger.getInoutcome() != null)
                    .filter(ledger -> "지출".equals(ledger.getInoutcome().getInoutcomeName()))
                    .mapToLong(Ledger::getLedgerAmount)
                    .sum();

            Long totalAmount = totalIncome - totalOutcome;

            return new ResponseEntity<>(totalAmount, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("/{ledger-id}")
    public ResponseEntity deleteLedger(@PathVariable("ledger-group-id") @Positive Long ledgerGroupId,
                                       @PathVariable("ledger-id") @Positive Long ledgerId) {
        ledgerService.deleteLedger(ledgerGroupId, ledgerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

