package com.psych.game.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/*Role could have been Enumertation but let's say if require some new role to be entertained in the syetem, we'll have
make the code change. So to avoid that, we are considering it as an entity
 */
@Entity
@Table(name ="roles")
public class Roles extends Auditable{

    @NotBlank
    @Column(unique = true)
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String description;
}
