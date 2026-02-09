import com.example.demo.entity.DocsPP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocsPPRepository extends JpaRepository<DocsPP, Long> {
}