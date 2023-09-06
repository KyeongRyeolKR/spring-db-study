package hello.itemservice.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // JPA 객체
//@Table(name = "item") // 객체 명이랑 같으면 생략 가능
public class Item {

    // 해당 필드를 테이블의 PK와 매핑
    @Id
    // DB에서 PK 값 생성하는 방식을 사용 ex) AUTO_INCREMENT
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 해당 필드를 테이블의 컬럼과 매핑(이름이 같으면 생략 가능)
    // 스프링부트와 통합해서 사용하면 필드명을 테이블 컬럼명으로 변환할 때,
    // 카멜 케이스를 언더스코어로 자동 변환해준다!
    // 즉, 여기서 굳이 name = "item_name" 지정을 안해줘도 된다는 것!!
    @Column(name = "item_name", length = 10)
    private String itemName;
    private Integer price;
    private Integer quantity;

    // JPA는 public 또는 protected의 기본 생성자가 필수로 있어야한다.
    // 프록시 기술을 쓰기 편하게 한다...
    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
