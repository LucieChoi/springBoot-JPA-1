package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter@Setter
public class MemberForm {

    /**
     * 화면 요구사항이 복잡해지기 시작하면, 엔티티에 화면을 처리하기 위한 기능이 점점 증가.
     * 결과적으로 엔티티가 점점 화면에 종속적으로 변하고,
     * 이렇게 되면 화면 기능때문에 지저분해진 엔티티를 유지보수하기 어려워진다.
     *
     * 화면이나 API의 요구사항을 폼 객체나 DTO로 처리하고, 엔티티는 최대한 순수하게 유지하자.
     */

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
