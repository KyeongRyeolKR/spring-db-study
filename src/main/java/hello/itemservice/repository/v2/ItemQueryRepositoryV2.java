package hello.itemservice.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static hello.itemservice.domain.QItem.item;

/**
 * 복잡한 쿼리나 동적 쿼리들을 위한 리포지토리 - QueryDSL
 */
@Repository
public class ItemQueryRepositoryV2 {

    private final JPAQueryFactory query;

    public ItemQueryRepositoryV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Item> findAll(ItemSearchCond cond) {
        return query
                .select(item)
                .from(item)
                .where(
                        likeItemName(cond.getItemName()),
                        maxPrice(cond.getMaxPrice())
                )
                .fetch();
    }

    // 재사용 가능한 쿼리 조건 메서드
    private BooleanExpression likeItemName(String itemName) {
        if (StringUtils.hasText(itemName)) {
            return item.itemName.like("%" + itemName + "%");
        }

        return null;
    }

    // 재사용 가능한 쿼리 조건 메서드
    private BooleanExpression maxPrice(Integer maxPrice) {
        if(maxPrice != null) {
            return item.price.loe(maxPrice);
        }

        return null;
    }
}
