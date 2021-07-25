package com.eascapeco.cinemapr.api.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity(name = "refresh_token")
@Getter @Setter @RequiredArgsConstructor
public class RefreshToken {

    @Id
    @Column(name = "tkn_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Long id;

    @Column(name = "tkn", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

//    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "adm_no", unique = true)
    private Long admNo;

    @Column(name = "exp_date", nullable = false)
    private Date expiryDate;

}
