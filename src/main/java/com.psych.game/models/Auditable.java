package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


//This class keeps track of metadata relating all objects
/*
MappedSuper Class: Designates a class whose mapping information is applied to the entities that inherit from it.
 A mapped superclass has no separate table defined for it.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable implements Serializable {
    //These annotations are instructions to generate id for long id
    @Id
    @GeneratedValue(generator = "sequence",strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name ="sequence",allocationSize = 10) //--> allocationsize -> allocates 10 bits of size
    @Getter
    @Setter //This for Spring class to access these private fields
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = false,updatable = false) //This indicates this entry is non updatable
    @Getter
    @Setter
    private Date createdDate = new Date();

    //These annotations keep will keep track of what objects are being updated and how
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    @Column(nullable = false)
    @Getter
    @Setter
    private Date updateDate =new Date();



}
