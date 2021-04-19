package com.restapi.demoinfleanrestapi.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    private Integer id;
    
    @Column(unique = true)
    private String email;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 어차피 매번 가져와야 하고, 단순하기 때문에 설정
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
