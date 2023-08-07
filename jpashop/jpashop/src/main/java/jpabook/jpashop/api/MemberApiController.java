package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody Member member){

        Long id=memberService.join(member);
        return new CreateMemberResponse(id);

    }
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody CreateMemberRequest request){

        Member member= new Member();
        member.setName(request.getName());
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);


    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody UpdateMemberRequest request){

        memberService.update(id, request.getName());

        return new UpdateMemberResponse(id, memberService.findOne(id).getName());

/*
        Member member= new Member();
        member.setName(request.getName());
        Long id=memberService.join(member);
        return new CreateMemberResponse(id);

 */
    }


    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private  Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {

        private  String name;
    }
    @Data
    static class CreateMemberResponse{
        public CreateMemberResponse(Long id) {
            this.id = id;
        }

        private Long id;
    }

    @Data
    static class CreateMemberRequest {
        private  String name;
    }

}
