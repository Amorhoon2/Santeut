package com.santeut.common.entity;

import com.santeut.common.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "likes")
public class Like extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private int likeId;

    @NotNull
    @Column(name = "like_reference_type", length = 255)
    private String likeReferenceType;

    @NotNull
    @Column(name = "like_reference_id")
    private int likeReferenceId;

    @NotNull
    @Column(name = "user_id")
    private int userId;
}
