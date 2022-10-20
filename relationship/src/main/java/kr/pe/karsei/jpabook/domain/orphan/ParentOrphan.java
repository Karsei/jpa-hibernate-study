package kr.pe.karsei.jpabook.domain.orphan;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class ParentOrphan {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChildOrphan> children = new ArrayList<>();

    public void addChild(ChildOrphan child) {
        children.add(child);
        child.setParent(this);
    }
}
