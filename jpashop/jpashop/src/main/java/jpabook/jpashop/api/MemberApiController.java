package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        return memberService.findMembers();
    }
    @GetMapping("/api/v2/members")
    public Result membersV2(){


    List<MemberDto> collect=
        memberService.findMembers().
        stream().
        map(m->new MemberDto(m.getName())).
                collect(Collectors.toList());
        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T>{
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto{
        private String name;
    }

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
