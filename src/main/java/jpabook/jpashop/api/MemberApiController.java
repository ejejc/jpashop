package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

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