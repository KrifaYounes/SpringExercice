package com.exercice.spring.demo.repository.specification;

import com.exercice.spring.demo.domain.User;
import com.exercice.spring.demo.domain.User_;
import com.exercice.spring.demo.dto.UserDto;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Ici entre <XXX> en met l'objet du domain (entité JPA/Hibernate)
 */
public class UserSpecification implements Specification<User> {


    private UserDto userDto;

    public UserSpecification(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();

        if (!Strings.isEmpty(userDto.getFirstName())) {
            predicates.add(builder.equal(root.get(User_.firstName), userDto.getFirstName()));
        }

        if (!Strings.isEmpty(userDto.getLastName())) {
            predicates.add(builder.equal(root.get(User_.lastName), userDto.getLastName()));
        }

        return builder.and(predicates.toArray(new Predicate[] {}));
    }

}