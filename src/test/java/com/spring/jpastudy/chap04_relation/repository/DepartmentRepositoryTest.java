package com.spring.jpastudy.chap04_relation.repository;

import com.spring.jpastudy.chap04_relation.entity.Department;
import com.spring.jpastudy.chap04_relation.entity.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class DepartmentRepositoryTest {
    @Autowired
EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;

    @Test
    @DisplayName("특정 부서를 조회하면 해당 소속부서원들이 함께 조회된다")
    void findDeptTest() {
        //given
        Long id = 1L;
        //when
        Department department = departmentRepository.findById(id).orElseThrow();

        //then
        System.out.println("\n\n\n\n");

        System.out.println("department = " + department);

        System.out.println("\n\n\n\n");
        List<Employee> employees = department.getEmployees();
        System.out.println();
        employees.forEach(System.out::println);
        System.out.println();
    }

    //양방향 연관관계에서 리스트에 데이터 갱신 시 주의 사항
    @Test
    @DisplayName("양방향 연관관계에서 연관 데이터 수정")
    void changeTest() {
        //given

        //3번 사원의 부서를 2번 부서에서 1번 부서로 수정

        //3번 사원 정보 조회
   Employee employee =     employeeRepository.findById(3L).orElseThrow();

        //1번 부서 정보 조회
        Department department = departmentRepository.findById(1L).orElseThrow();
        //when
        //사원 정보 수정
//        employee.setDepartment(department);
        //이게 핵심 코드
        // 핵심: 양방향에서는 수정 시 반대편도 같이 수정
//        department.getEmployees().add(employee);

        employee.changeDepartment(department);
        employeeRepository.save(employee);

        /*
        *  사원 정보가 employee 엔터티에서 수정되어도
        *  반대편 엔터티인 department에서는 리스트에 바로
        *  반영되지 않는다.
        *
        * 해결 방안은 데이터 수정 시에 반대편 엔터티에도 같이 수정을 해줘라
        * */
        //바뀐 부서의 사원 목록 조회
        List<Employee> employees = department.getEmployees();
        System.out.println("\n\n\n");
        employees.forEach(System.out::println);
        System.out.println("\n\n\n");
        //then
    }

}