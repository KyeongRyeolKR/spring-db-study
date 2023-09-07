package hello.itemservice.repository.v2;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 간단한 쿼리(기본 CRUD)를 위한 리포지토리 - Spring Data JPA
 */
public interface ItemRepositoryV2 extends JpaRepository<Item, Long> {
}
