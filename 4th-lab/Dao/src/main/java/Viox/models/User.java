package Viox.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            length = 50,
            name = "username",
            unique = true,
            nullable = false
    )
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    boolean enabled;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Cat> cats;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "authorities",
            joinColumns = @JoinColumn(name = "username", referencedColumnName = "username")
    )
    @Column(name = "authority")
    private List<String> authorities;

    public void addCat(Cat cat) {
        cats.add(cat);
    }

    public void removeCat(Cat cat) {
        cats.remove(cat);
    }
}