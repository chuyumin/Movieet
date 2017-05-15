package moviereview.repository;

import moviereview.model.*;
import moviereview.util.MovieGenre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Sorumi on 17/5/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest()
public class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActorFactorRepository actorFactorRepository;

    @Autowired
    DirectorFactorRepository directorFactorRepository;

    @Autowired
    GenreFactorRepository genreFactorRepository;

    @Autowired
    CollectRepository collectRepository;

    @Test
    public void proxy() throws Exception {
        System.out.println(userRepository.getClass());
    }

    @Test
    public void save() throws Exception {
        User user = new User();
        Integer integer = userRepository.findNextId();
        if (integer != null) {
            user.setId(integer + 1);
        } else {
            user.setId(0);
        }
        user.setUsername("125");
        user.setPassword("123");
        //
        ActorFactor actorFactor = new ActorFactor(1.7, "test");
        DirectorFactor directorFactor = new DirectorFactor(2, "test");
        GenreFactor genreFactor = new GenreFactor(5, MovieGenre.Action);
        actorFactor.setUser(user);
        directorFactor.setUser(user);
        genreFactor.setUser(user);

        Set<ActorFactor> actorFactors = new HashSet<>();
        actorFactors.add(actorFactor);
        Set<DirectorFactor> directorFactors = new HashSet<>();
        directorFactors.add(directorFactor);
        Set<GenreFactor> genreFactors = new HashSet<>();
        genreFactors.add(genreFactor);

        user.setActorFactors(actorFactors);
        user.setDirectorFactors(directorFactors);
        user.setGenreFactors(genreFactors);


        //genreFactorRepository.save(genreFactor);
        //actorFactorRepository.save(actorFactor);
        //directorFactorRepository.save(directorFactor);
        userRepository.save(user);
    }


    @Test
    public void findById() throws Exception {
        assertNotNull(userRepository.findNextId());
    }

    @Test
    public void findNextId() {
        System.out.println(userRepository.findNextId());
    }

    @Test
    public void findUserByUsername() {
        User user = userRepository.findUserByUsername("123");
        System.out.println(user.getUsername());
    }

    @Test
    public void post() {
        Collect collect = new Collect(1, 1, "34", LocalDateTime.now().withNano(0).toString());
        collectRepository.save(collect);
    }
}