package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "talk")
@Entity
@NamedQueries({
        @NamedQuery(name = "Talk.deleteAllRows", query = "DELETE from Talk"),
        @NamedQuery(name = "Talk.getAllRows", query = "SELECT t from Talk t"),
        @NamedQuery(name = "Talk.getTalk", query = "SELECT t from Talk t WHERE t.topic = :t")
})
public class Talk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "topic")
    private String topic;


    @Column(name = "duration")
    private int duration;
/*
    @Column(name = "propsList")
    private List<Props> propsList;

 */

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Speaker> speakers;

    //@JoinColumn(name = "conference_ID")
    @ManyToOne
    private Conference conference;

    public Talk(String topic, int duration) {
        this.topic = topic;
        this.duration = duration;
        this.speakers = new ArrayList<>();
    }

    public Talk() {
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }

    public Conference getConference() {
        return conference;
    }


    public void setSpeakers(List<Speaker> speakers) {
        this.speakers = speakers;
    }

    public List<Speaker> getSpeakers() {
        return speakers;
    }


    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
/*
    public List<String> getPropsList() {
        return propsList;
    }

    public void setPropsList(List<String> propsList) {
        this.propsList = propsList;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void addSpeaker(Speaker speaker){
        this.speakers.add(speaker);
        if(speaker != null){
            List<Talk> talks = new ArrayList<>();
            talks.add(this);
            speaker.setTalks(talks);
        }
    }
}