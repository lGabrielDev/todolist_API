package br.com.lGabrielDev.todolist_project.models.role;

import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;

@DataJpaTest
public class RoleRepositoryTest {
    
    //injected attributes
    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("It should find a role")
    public void itShouldFindARoleByRoleNameSuccessfully(){
        //arrange
        Role adminRole = new Role();
        adminRole.setRole(RoleEnum.ADMIN);
        this.roleRepository.save(adminRole);

        //act
        Optional<Role> methodResult = this.roleRepository.findByRole(RoleEnum.ADMIN);
        //assert
        Assertions.assertThat(methodResult).isPresent();
        Assertions.assertThat(methodResult.get().getRole()).isEqualTo(RoleEnum.ADMIN);
    }

    @Test
    @DisplayName("It should NOT find a role by 'role name'")
    public void itShouldNotFindARole(){
        //arrange
        //act
        Optional<Role> methodResult = this.roleRepository.findByRole(RoleEnum.ADMIN);
        //assert
        Assertions.assertThat(methodResult).isEmpty();
    }
}