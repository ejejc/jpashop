package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * entity가 반환값으로 그대로 나가는 문제점
     * collection 형태로 반환되므로 스펙 확장이 용이하지 않는 문제점
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result memberV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @PostMapping("/api/v1/members")
    /**
     * 지금은 엔티티를 파라미터로 받고 있지만, 위와 같은 방식은 절때 좋은 방법이 아니다.
     * 엔티티를 그대로 사용하지 않고 dto를 생성해서 만드는 것이 좋다.
     */
    public CreateMemeberResponse saveMemverV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemeberResponse(id);
    }

    /**
     * 엔티티로 파라미터로 받지 않고 dto를 통해 파라미터로 받는 API 형식
     * @param request
     * @return
     */
    @PostMapping("/api/v2/members")
    public CreateMemeberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemeberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Getter @Setter
    static class CreateMemberRequest {
        private String name;
    }

    @Getter @Setter
    @NoArgsConstructor
    static class CreateMemeberResponse {
        private Long id;

        public CreateMemeberResponse(Long id) {
            this.id = id;
        }
    }
}
