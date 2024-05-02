package Viox.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cats")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private LocalDate dateOfBirth;

    private String breed;

    private CatColor color;

    @JoinColumn(name = "user_id")
    private Long ownerId;

    @ManyToMany
    @JoinTable(name = "cat_friends", joinColumns = @JoinColumn(name = "cat_id"), inverseJoinColumns = @JoinColumn(name = "friend_id"))
    private Set<Cat> friends = new HashSet<>();

    @ManyToMany(mappedBy = "friends")
    private Set<Cat> friendOf = new HashSet<>();

    public void addFriend(Cat friend) {
        friends.add(friend);
        friend.friendOf.add(this);
    }

    public void removeFriend(Cat friend) {
        friends.remove(friend);
        friend.friendOf.remove(this);
    }
}
