package entities;

import javax.persistence.*;
import java.util.List;

@Table(name = "speaker")
@Entity
public class Speaker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "profession")
    private String profesion;

    @Column(name = "gender")
    private String gender;

    @ManyToMany(mappedBy = "speakers", cascade = CascadeType.PERSIST)
    private List<Talk> talks;

    public Speaker(String name, String profesion, String gender, List<Talk> talks) {
        this.name = name;
        this.profesion = profesion;
        this.gender = gender;
        this.talks = talks;
    }

    public Speaker() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Talk> getTalks() {
        return talks;
    }

    public void setTalks(List<Talk> talks) {
        this.talks = talks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}