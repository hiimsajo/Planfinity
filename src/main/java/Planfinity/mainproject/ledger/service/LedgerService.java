package Planfinity.mainproject.ledger.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.ledger.domain.Category;
import Planfinity.mainproject.ledger.domain.Inoutcome;
import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledger.domain.Payment;
import Planfinity.mainproject.ledger.ledgerDto.LedgerPatchDto;
import Planfinity.mainproject.ledger.ledgerDto.LedgerPostDto;
import Planfinity.mainproject.ledger.repository.CategoryRepository;
import Planfinity.mainproject.ledger.repository.InoutcomeRepository;
import Planfinity.mainproject.ledger.repository.LedgerRepository;
import Planfinity.mainproject.ledger.repository.PaymentRepository;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.ledgergroup.service.LedgerGroupService;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository ledgerRepository;
    private final MemberService memberService;
    private final LedgerGroupService ledgerGroupService;
    private final CategoryRepository categoryRepository;
    private final InoutcomeRepository inoutcomeRepository;
    private final PaymentRepository paymentRepository;

    public LedgerService(LedgerRepository ledgerRepository, MemberService memberService, LedgerGroupService ledgerGroupService,
                             CategoryRepository categoryRepository, InoutcomeRepository inoutcomeRepository, PaymentRepository paymentRepository) {
        this.ledgerRepository = ledgerRepository;
        this.memberService = memberService;
        this.ledgerGroupService = ledgerGroupService;
        this.categoryRepository = categoryRepository;
        this.inoutcomeRepository = inoutcomeRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional
    public Ledger createLedger(Long ledgerGroupId, LedgerPostDto postDto) {
        Member member = memberService.findMember();
        LedgerGroup ledgerGroup = ledgerGroupService.findByGroupId(ledgerGroupId);
        Category category = null;  // 카테고리 초기값 설정
        Long categoryId = postDto.getCategoryId();
        if (categoryId != null) {  // 요청 바디로 받은 카테고리 ID가 null이 아닌 경우에만 카테고리 조회
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));
        }
        Inoutcome inoutcome = null;
        Long inoutcomeId = postDto.getInoutcomeId();
        if (inoutcomeId != null) {
            inoutcome = inoutcomeRepository.findById(inoutcomeId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INOUTCOME_NOT_FOUND));
        }

        Payment payment = null;
        Long paymentId = postDto.getPaymentId();
        if (paymentId != null) {
            payment = paymentRepository.findById(paymentId)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.PAYMENT_NOT_FOUND));
        }

        Ledger ledger;
        if (category != null) {
            if (inoutcome != null) {
                if (payment != null) {
                    ledger = postDto.toEntity(member, ledgerGroup, category, inoutcome, payment); // aaa
                } else {
                    ledger = postDto.toEntity(member, ledgerGroup, category, inoutcome, null); // aab
                }
            } else {
                if (payment != null) {
                    ledger = postDto.toEntity(member, ledgerGroup, category, null, payment); // aba
                } else {
                    ledger = postDto.toEntity(member, ledgerGroup, category, null, null); // abb
                }
            }
        } else {
            if (inoutcome != null) {
                if (payment != null) {
                    ledger = postDto.toEntity(member, ledgerGroup, null, inoutcome, payment); // baa
                } else {
                    ledger = postDto.toEntity(member, ledgerGroup, null, inoutcome, null); // bab
                }
            } else {
                if (payment != null) {
                    ledger = postDto.toEntity(member, ledgerGroup, null, null, payment); // bba
                } else {
                    ledger = postDto.toEntity(member, ledgerGroup, null, null, null); // bbb
                }
            }
        }

        Ledger savedLedger = ledgerRepository.save(ledger);

        return savedLedger;

    }

    public Ledger updateLedger(Long ledgerGroupId, Long ledgerId, LedgerPatchDto patchDto) {
        Member member = memberService.findMember();
        LedgerGroup ledgerGroup = ledgerGroupService.findByGroupId(ledgerGroupId);
        Ledger updatedLedger = findVerifiedLedger(ledgerId);
        //Long categoryId = patchDto.getCategoryId();
        if (patchDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(patchDto.getCategoryId()).orElse(null);
            if (category == null) {
                throw new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND);
            }
            updatedLedger.setCategory(category);  // 카테고리 연결 업데이트
        } else {
            updatedLedger.setCategory(null);
        }
        Long inoutcomeId = patchDto.getInoutcomeId();
        if (inoutcomeId != null) {
            Inoutcome inoutcome = inoutcomeRepository.findById(inoutcomeId).orElse(null);
            if (inoutcome == null) {// 카테고리 ID가 유효하지 않은 경우
                throw new BusinessLogicException(ExceptionCode.INOUTCOME_NOT_FOUND);
            }
            updatedLedger.setInoutcome(inoutcome);  // 카테고리 연결 업데이트
        } else {
            if (updatedLedger.getInoutcome() != null) {// 카테고리 ID가 null인 경우 카테고리 연결을 유지하거나 제거
                updatedLedger.setInoutcome(null);
            }
        }
        //Long paymentId = patchDto.getPaymentId();
        if (patchDto.getPaymentId() != null) {
            Payment payment = paymentRepository.findById(patchDto.getPaymentId()).orElse(null);
            if (payment == null) {// 카테고리 ID가 유효하지 않은 경우
                throw new BusinessLogicException(ExceptionCode.PAYMENT_NOT_FOUND);
            }
            updatedLedger.setPayment(payment);  // 카테고리 연결 업데이트
        } else {
            updatedLedger.setPayment(null);
        }
        updatedLedger.setTitle(patchDto.getLedgerTitle());
        updatedLedger.setContent(patchDto.getLedgerContent());
        updatedLedger.setAmount(patchDto.getLedgerAmount());
        updatedLedger.setDate(LocalDate.parse(patchDto.getLedgerDate()));
        updatedLedger.setLedgerGroup(ledgerGroup); // 그룹 아이디 변경

        return updatedLedger;

    }

    @Transactional(readOnly = true)
    public Ledger getLedger(Long ledgerGroupId, Long ledgerId) {
        Member member = memberService.findMember();
        ledgerGroupService.findByGroupId(ledgerGroupId);
        return findVerifiedLedger(ledgerId);

    }

    @Transactional(readOnly = true)
    public List<Ledger> getLedgers(Long ledgerGroupId) {
        Member member = memberService.findMember();
        LedgerGroup ledgerGroup = ledgerGroupService.findByGroupId(ledgerGroupId);
        if (ledgerGroup == null) {
            // 그룹이 존재하지 않을 경우
            return Collections.emptyList();
        }
        List<Ledger> ledgers = ledgerGroup.getLedgers();
        return ledgers;

    }

    public List<Ledger> getLedgersByDate(Long ledgerGroupId, LocalDate startDate, LocalDate endDate) {
        Member member = memberService.findMember();
        LedgerGroup ledgerGroup = ledgerGroupService.findByGroupId(ledgerGroupId);

        List<Ledger> ledgers = this.ledgerRepository.findByLedgerGroupAndLedgerScheduleDateBetween(ledgerGroup ,startDate, endDate);
        ledgers.sort(Comparator.comparing(Ledger::getLedgerScheduleDate));

        return ledgers;

    }

    public void deleteLedger(Long ledgerGroupId, Long ledgerId) {
        Member member = memberService.findMember();
        ledgerGroupService.findByGroupId(ledgerGroupId);
        Ledger deletedLedger = findVerifiedLedger(ledgerId);
        ledgerRepository.delete(deletedLedger);


    }

    @Transactional(readOnly = true)
    public Ledger findById ( final Long ledgerId){
        return ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEDGER_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    private Ledger findVerifiedLedger (Long ledgerId){
        return ledgerRepository.findById(ledgerId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEDGER_NOT_FOUND));
    }
}
