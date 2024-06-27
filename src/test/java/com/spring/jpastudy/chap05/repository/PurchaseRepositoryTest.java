package com.spring.jpastudy.chap05.repository;

import com.spring.jpastudy.chap05.entity.Goods;
import com.spring.jpastudy.chap05.entity.Purchase;
import com.spring.jpastudy.chap05.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional    //항상 걸기
@Rollback
class PurchaseRepositoryTest {

    //유저, 상품, 구매 3개 걸기~!
    @Autowired
    UserRepository userRepository;
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    PurchaseRepository purchaseRepository;

    @Autowired
    EntityManager em;  // 영속성 컨텍스트를 관리하는 객체

    private User user1;
    private User user2;
    private User user3;
    private Goods goods1;
    private Goods goods2;
    private Goods goods3;

    @BeforeEach
    void setUp() {
        //유저 3개 상품 3개 등록하기~!
        user1 = User.builder().name("망곰이").build();
        user2 = User.builder().name("하츄핑").build();
        user3 = User.builder().name("쿠로미").build();
        goods1 = Goods.builder().name("뚜비모자").build();
        goods2 = Goods.builder().name("닭갈비").build();
        goods3 = Goods.builder().name("중식도").build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        goodsRepository.save(goods1);
        goodsRepository.save(goods2);
        goodsRepository.save(goods3);
    }


    @Test
    @DisplayName("유저와 상품을 연결한 구매 기록 생성 테스트")
    void createPurchaseTest() {
        //given
        //유저2가 1번상품 샀다
        Purchase purchase = Purchase.builder()
                .user(user2)
                .goods(goods1)
                .build();
        //when

        purchaseRepository.save(purchase);

        // 영속성 컨텍스트를 초기화하면 SELECT문을 볼 수 있다.
        //실제로는 안하는게 낫다 (select를 볼라고 그냥 해봄)
        em.flush();
        em.clear();

        //then
        Purchase foundPurchase = purchaseRepository.findById(purchase.getId()).orElseThrow();

        System.out.println("\n\n\n구매한 회원정보: " + foundPurchase.getUser() + "\n\n");
        System.out.println("\n\n\n구매한 상품정보: " + foundPurchase.getGoods() + "\n\n");

        assertEquals(user2.getId(), foundPurchase.getUser().getId());
        assertEquals(goods1.getId(), foundPurchase.getGoods().getId());
        //구매한 회원정보: User(id=2, name=하츄핑)
        //구매한 상품정보: Goods(id=1, name=뚜비모자)
    }



    @Test
    @DisplayName("특정 유저의 구매 목록을 조회한다.")
    void findPurchaseListTest() {
        //given
        Purchase purchase1 = Purchase.builder()
                .user(user1).goods(goods1).build();  //유저1 상품1 삼
        Purchase purchase2 = Purchase.builder()
                .user(user1).goods(goods3).build(); //유저 1 상품3 삼
        //when

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);

        em.flush();
        em.clear();

        //then
        User user = userRepository.findById(user1.getId()).orElseThrow();
        List<Purchase> purchases = user.getPurchaseList();

        for (Purchase p : purchases) {
            System.out.printf("\n\n%s 님이 구매한 물품명 : %s\n\n",
                    user.getName(), p.getGoods().getName());
        }
//        망곰이 님이 구매한 물품명 : 뚜비모자    == 1
        //망곰이 님이 구매한 물품명 : 중식도      == 3
        assertEquals(2, purchases.size());
        assertTrue(purchases.stream().anyMatch(p -> p.getGoods().equals(goods1)));
        assertTrue(purchases.stream().anyMatch(p -> p.getGoods().equals(goods3)));
    }


    @Test
    @DisplayName("특정 상품을 구매한 유저목록을 조회한다.")
    void findUserByGoodsTest() {
        //given
        Purchase purchase1 = Purchase.builder()
                .user(user2).goods(goods1).build();   //2번 사람   1번 상품 삼
        Purchase purchase2 = Purchase.builder()
                .user(user3).goods(goods1).build();  // 3번 사람도 1번 상품 삼

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);

        em.flush();
        em.clear();

        //when
        Goods goods = goodsRepository.findById(goods1.getId()).orElseThrow();
        List<Purchase> purchases = goods.getPurchaseList();

        //then
        for (Purchase p : purchases) {
            System.out.printf("\n\n%s 상품을 구매한 유저명 : %s\n\n",
                    goods.getName(), p.getUser().getName());
        }
//        뚜비모자 상품을 구매한 유저명 : 하츄핑
//        뚜비모자 상품을 구매한 유저명 : 쿠로미
        assertEquals(2, purchases.size());
        assertTrue(purchases.stream().anyMatch(p -> p.getUser().equals(user2)));
        assertTrue(purchases.stream().anyMatch(p -> p.getUser().equals(user3)));
    }


    @Test
    @DisplayName("구매기록 삭제 테스트")
    void deletePurchaseTest() {
        //given
        Purchase purchase = Purchase.builder()
                .user(user1).goods(goods1).build();

        Purchase savedPurchase = purchaseRepository.save(purchase);

        em.flush();
        em.clear();

        //when
        purchaseRepository.delete(savedPurchase);

        em.flush();
        em.clear();

        //then
        Purchase foundPurchase
                = purchaseRepository.findById(purchase.getId()).orElse(null);

        assertNull(foundPurchase);
    }



    @Test
    @DisplayName("회원이 탈퇴하면 구매기록이 모두 삭제되어야 한다")
    void cascadeRemoveTest() {
        //given
        Purchase purchase1 = Purchase.builder()
                .user(user1).goods(goods2).build();

        Purchase purchase2 = Purchase.builder()
                .user(user1).goods(goods3).build();

        Purchase purchase3 = Purchase.builder()
                .user(user2).goods(goods1).build();

        purchaseRepository.save(purchase1);
        purchaseRepository.save(purchase2);
        purchaseRepository.save(purchase3);

        em.flush();
        em.clear();

        User user = userRepository.findById(user1.getId()).orElseThrow();
        List<Purchase> purchases = user.getPurchaseList();

        System.out.println("\n\nuser1's purchases = " + purchases + "\n\n");
        System.out.println("\n\nall of purchases = " + purchaseRepository.findAll() + "\n\n");

        userRepository.delete(user);

        em.flush();
        em.clear();
        //when

        List<Purchase> purchaseList = purchaseRepository.findAll();

        System.out.println("\n\nafter removing purchaseList = " + purchaseList + "\n\n");

        //then
        assertEquals(1, purchaseList.size());
    }


}