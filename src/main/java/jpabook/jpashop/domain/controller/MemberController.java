package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.form.MemberForm;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        log.info("Member Controller");
        model.addAttribute("memberForm", new MemberForm()); // validation 확인도 가능
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String finishForm(@Valid MemberForm memberForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
           return "members/createMemberForm";
        }
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setAddress(new Address(memberForm.getCity(), memberForm.getStreet(), memberForm.getZipcode()));

        try {
            memberService.join(member);
        } catch (IllegalStateException e){
            bindingResult.addError(new FieldError("memberForm", "name", "이미 존재하는 이름입니다."));
            return "members/createMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/members")
    public String findMembers(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
