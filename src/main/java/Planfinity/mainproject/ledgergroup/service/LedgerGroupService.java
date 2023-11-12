package Planfinity.mainproject.ledgergroup.service;

import Planfinity.mainproject.exception.BusinessLogicException;
import Planfinity.mainproject.exception.ExceptionCode;
import Planfinity.mainproject.ledger.domain.Ledger;
import Planfinity.mainproject.ledger.repository.LedgerRepository;
import Planfinity.mainproject.ledgergroup.domain.LedgerGroup;
import Planfinity.mainproject.ledgergroup.dto.LedgerGroupPatchDto;
import Planfinity.mainproject.ledgergroup.dto.LedgerGroupPostDto;
import Planfinity.mainproject.ledgergroup.invitationDto.InvitationPostDto;
import Planfinity.mainproject.ledgergroup.repository.LedgerGroupRepository;
import Planfinity.mainproject.member.domain.Member;
import Planfinity.mainproject.member.repository.MemberRepository;
import Planfinity.mainproject.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
public class LedgerGroupService {

    private final LedgerRepository ledgerRepository;
    private final LedgerGroupRepository ledgerGroupRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    public LedgerGroupService(MemberService memberService, LedgerGroupRepository ledgerGroupRepository,
                                  LedgerRepository ledgerRepository, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.ledgerGroupRepository = ledgerGroupRepository;
        this.ledgerRepository = ledgerRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public LedgerGroup createLedgerGroup(LedgerGroupPostDto ledgerGroupPostDto) {
        Member member = memberService.findMember();
        LedgerGroup saveLedgerGroup = ledgerGroupRepository.save(ledgerGroupPostDto.toEntity(member));

        return saveLedgerGroup;

    }

    @Transactional
    public LedgerGroup updateLedgerGroup(Long ledgerGroupId, LedgerGroupPatchDto ledgerGroupPatchDto) {
        Member member = memberService.findMember();
        LedgerGroup foundLedgerGroup = findVerifiedLedgerGroup(ledgerGroupId);
        foundLedgerGroup.setTitle(ledgerGroupPatchDto.getLedgerGroupTitle());

        return foundLedgerGroup;
    }

    @Transactional(readOnly = true)
    public LedgerGroup getLedgerGroup(Long ledgerGroupId) {
        Member member = memberService.findMember();
        return findVerifiedLedgerGroup(ledgerGroupId);
    }

    @Transactional(readOnly = true)
    public List<LedgerGroup> getLedgerGroups() {
        Member member = memberService.findMember();
        List<LedgerGroup> ledgerGroups = this.ledgerGroupRepository.findAll();
        return ledgerGroups;
    }

    public void deleteLedgerGroup(Long ledgerGroupId) {
        Member member = memberService.findMember();
        LedgerGroup foundLedgerGroup = findVerifiedLedgerGroup(ledgerGroupId);
        ledgerGroupRepository.delete(foundLedgerGroup);
    }

    @Transactional(readOnly = true)
    public LedgerGroup findByGroupId(final Long ledgerGroupId) {
        return ledgerGroupRepository.findById(ledgerGroupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEDGER_GROUP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public LedgerGroup findVerifiedLedgerGroup(Long ledgerGroupId) {
        LedgerGroup foundLedgerGroup = ledgerGroupRepository.findById(ledgerGroupId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.LEDGER_GROUP_NOT_FOUND));
        return foundLedgerGroup;
    }

    @Transactional
    public LedgerGroup invite(Long ledgerGroupId, InvitationPostDto invitationPostDto) {
        LedgerGroup foundLedgerGroup  = findVerifiedLedgerGroup(ledgerGroupId);
        Member owner = memberService.findMember();

        if (!foundLedgerGroup.isOwner(owner)) {
            throw new BusinessLogicException(ExceptionCode.IS_NOT_OWNER);
        }
        List<String> emails = invitationPostDto.extractEmails();
        List<Member> membersByEmail = memberRepository.findByEmailIn(emails);
        if (emails.size() != membersByEmail.size()) {
            throw  new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        }
        foundLedgerGroup.inviteMembers(membersByEmail);
        return foundLedgerGroup;
    }

    @Transactional
    public LedgerGroup getInvitedMember(Long ledgerGroupId) {
        Member memebr = memberService.findMember();
        return findVerifiedLedgerGroup(ledgerGroupId);
    }
/*
    @Transactional
    public List<LedgerGroup> getInvitedMembers() {
        Member member = memberService.findMember();
        List<LedgerGroup> getInvitedMembers = this.ledgerGroupRepository.findAll();
        return getInvitedMembers;
    }

 */
}

