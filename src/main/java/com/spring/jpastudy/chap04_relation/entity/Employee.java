package com.spring.jpastudy.chap04_relation.entity;

import lombok.*;

import javax.persistence.*;

@Setter @Getter
@ToString(exclude = "department")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_emp")
public class Employee {

//    @Override
//    public String toString() {
//        return "Employee{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
////                ", department=" + department +
//                '}';
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_id")
    private Long id; // 사원번호

    @Column(name = "emp_name", nullable = false)
    private String name;

    // 단방향 매핑 - 데이터베이스처럼 한쪽에 상대방의 PK를 FK로 갖는 형태
    //EAGER LOading : 연관된 데이터를 항상 조인을 통해 같이 가져옴  <<문제 일으킬 요지 많음>>
    //Lazy Loading : 해당 엔터티 데이터만 가져오고
                            // 필요한 경우 연관 엔터티를 가져옴
//    many to one 걸린 필드는 tostring 에서 제거해랴ㅏ~!~!~⭐️
    @ManyToOne(fetch =FetchType.LAZY)   //이게 실무적으로 좋음 이거만 써라⭐️
    @JoinColumn(name = "dept_id")  // FK 컬럼명
    private Department department;

    public void changeDepartment(Department department) {
        this.department = department;
        department.getEmployees().add(this);
    }

//    @ManyToOne
//    @JoinColumn(name = "receive_dept_id")
//    private Department department2;
}
