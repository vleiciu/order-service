package com.org.os.persistance.entity;

import com.org.os.enums.TokenType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.type.YesNoConverter;

@Entity
@Table(name = "TOKENS")
@Data
@Builder
public class Token {
    @Id
    @Column(name = "TOKEN_ID")
    public Integer tokenId;

    @Column(name = "TOKEN_VALUE")
    public String tokenValue;

    @Column(name = "TOKEN_TYPE")
    @Enumerated(EnumType.STRING)
    public TokenType tokenType;

    @Column(name = "REVOKED")
    @Convert(converter = YesNoConverter.class)
    public boolean revoked;

    @Column(name = "EXPIRED")
    @Convert(converter = YesNoConverter.class)
    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    public Users users;
}
