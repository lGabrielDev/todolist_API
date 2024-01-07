package br.com.lGabrielDev.todolist_project.models.role.dtos;

import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.lGabrielDev.todolist_project.enums.RoleEnum;
import br.com.lGabrielDev.todolist_project.models.role.Role;

public class RoleDtoTest {
    
    @Test
    @DisplayName("It should convert a set of 'Role' to a set of 'RoleDto', and return a set with 2 roles successfully")
    public void itShouldReturnASetOfRoleDtoSuccessfully(){
        //arrange
        Role role1 = new Role();
        role1.setId(1l);
        role1.setRole(RoleEnum.ADMIN);

        Role role2 = new Role();
        role2.setId(2l);
        role2.setRole(RoleEnum.REGULAR_USER);

        Set<Role> roleListToConvert = Set.of(role1, role2);
        //act
        Set<RoleDto> methodResult = RoleDto.convertTaskList(roleListToConvert);
        //assert
        Assertions.assertThat(methodResult).isNotNull();
        Assertions.assertThat(methodResult).size().isEqualTo(2);
    }
}