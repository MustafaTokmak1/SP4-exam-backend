package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "conference")
@Entity
@NamedQueries({
        @NamedQuery(name = "Conference.deleteAllRows", query = "DELETE from Conference"),
        @NamedQuery(name = "Conference.getAllRows", query = "SELECT c from Conference c"),
        @NamedQuery(name = "Conference.getConference", query = "SELECT c from Conference c WHERE c.name = :c")
})
public class Conference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "conference", cascade = CascadeType.PERSIST)
    private List<Talk> talkList;

    public Conference(String name, String location, int capacity) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.talkList = new ArrayList<>();
    }

    public Conference() {
    }
    public void addTalk(Talk talk) {
        this.talkList.add(talk);
        if (talk != null){
            talk.setConference(this);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Talk> getTalkList() {
        return talkList;
    }

    public void setTalkList(List<Talk> talkList) {
        this.talkList = talkList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}