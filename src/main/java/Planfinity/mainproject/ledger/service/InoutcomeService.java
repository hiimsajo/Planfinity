package Planfinity.mainproject.ledger.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.ledger.InoutcomeDto.InoutcomePatchDto;
import Planfinity.mainproject.ledger.InoutcomeDto.InoutcomePostDto;
import Planfinity.mainproject.ledger.domain.Inoutcome;
import Planfinity.mainproject.ledger.repository.InoutcomeRepository;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class InoutcomeService {
    private final MemberService memberService;
    private final InoutcomeRepository inoutcomeRepository;

    public InoutcomeService(MemberService memberService, InoutcomeRepository inoutcomeRepository) {
        this.memberService= memberService;
        this.inoutcomeRepository = inoutcomeRepository;
    }
    public Inoutcome createInoutcome(InoutcomePostDto postDto) {
        Member member = memberService.findMember();
        Inoutcome postedInoutcome = inoutcomeRepository.save(postDto.toEntity(member));
        return postedInoutcome;
    }
    public Inoutcome updateInoutcome(Long inoutcomeId, InoutcomePatchDto patchDto) {
        Member member = memberService.findMember();
        Inoutcome updatedInoutcome = existingInoutcome(inoutcomeId);
        updatedInoutcome.changeName(patchDto.getInoutcomeName());

        return updatedInoutcome;
    }
    public Inoutcome getInoutcome(Long inoutcomeId){
        Member member = memberService.findMember();
        return existingInoutcome(inoutcomeId);
    }
    public List<Inoutcome> getInoutcomes() {
        Member member = memberService.findMember();
        return inoutcomeRepository.findAll();
    }

    public void deleteInoutcome(Long inoutcomeId) {
        Member member = memberService.findMember();
        Inoutcome deletedInoutcome = existingInoutcome(inoutcomeId);
        inoutcomeRepository.delete(deletedInoutcome);
    }

    @Transactional
    public Inoutcome findByInoutcomeId(final Long inoutcomeId) {
        return inoutcomeRepository.findById(inoutcomeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INOUTCOME_NOT_FOUND));
    }

    @Transactional(readOnly = true )
    public Inoutcome existingInoutcome(Long inoutcomeId) {
        Inoutcome existingInoutcome = inoutcomeRepository.findById(inoutcomeId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.INOUTCOME_NOT_FOUND));
        return existingInoutcome;
    }

}
