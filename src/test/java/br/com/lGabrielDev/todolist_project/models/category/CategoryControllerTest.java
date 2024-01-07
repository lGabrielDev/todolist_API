package br.com.lGabrielDev.todolist_project.models.category;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryCreateDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryReadOneDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryWithIdNameAndOwnerIdDto;
import br.com.lGabrielDev.todolist_project.models.category.exceptions.CategoryNameIsWrong;
import br.com.lGabrielDev.todolist_project.security.SecurityConfiguration;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class) //we pass the security config class that contains the filter chain method
public class CategoryControllerTest {
    
    //injected attributes
    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objMapper;

    
    // -------------- CREATE -----------------
    @Test
    @DisplayName("It should return a 401 Unauthorized because person was not found")
    @WithAnonymousUser
    public void createItShouldGet401UnauthorizedBecausePersonWasNotFound() throws Exception{
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto("sport");
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(categoryDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 403 Forbidden because person doesn't have the 'REGULAR_USER' authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void itShouldReturn403ForbiddenBecausePersonDoesntHaveRegularUserAuthority() throws Exception{
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto("sport");
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(categoryDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 201 CREATED and a list of categories as well")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void itShouldReturn201CreatedAndAListOfCategories() throws Exception{
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto("sport");
        CategoryWithIdNameAndOwnerIdDto expectedResult = new CategoryWithIdNameAndOwnerIdDto();
        expectedResult.setId(1l);
        expectedResult.setName("sport");

        Mockito.when(this.categoryService.createCategory(Mockito.any(CategoryCreateDto.class))).thenReturn(List.of(expectedResult));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(categoryDto))
        )
        //assert
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should get an exception because the 'category name' is null")
    @WithMockUser(authorities = {"REGULAR_USER"}) 
    public void itShouldGetAnExceptionBecauseCategoryNameIsNull() throws Exception{
        //arrange
        CategoryCreateDto categoryDto = new CategoryCreateDto();

        Mockito.when(this.categoryService.createCategory(Mockito.any(CategoryCreateDto.class))).thenThrow(new CategoryNameIsWrong("'categoryName' cannot be null"));
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .post("/v1/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(categoryDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(409))
        .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").value("'categoryName' cannot be null"))
        .andDo(MockMvcResultHandlers.print());
    }


    // -------------- READ -----------------
    // --- read all ---
    @Test
    @DisplayName("It should return a 401 Unauthorized because person was not found")
    @WithAnonymousUser
    public void readAllShouldGet401UnauthorizedBecausePersonWasNotFound() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 403 Forbidden because person doesn't have the 'REGULAR_USER' authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void readAllItShouldReturn403ForbiddenBecausePersonDoesntHaveRegularUserAuthority() throws Exception{
        //arrange
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 200 OK and a list of categories as well")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void readAllItShouldReturn200OKAndAListOfCategories() throws Exception{
        //arrange
        List<CategoryWithIdNameAndOwnerIdDto> expectedResult = List.of(
            new CategoryWithIdNameAndOwnerIdDto(1l, "category1"),
            new CategoryWithIdNameAndOwnerIdDto(2l, "category2")
        );
        Mockito.when(this.categoryService.readAllCategories()).thenReturn(expectedResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category")
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedResult.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(expectedResult.get(0).getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(expectedResult.get(1).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(expectedResult.get(1).getName()))
        .andDo(MockMvcResultHandlers.print());
    }

    // --- read one ---
    @Test
    @DisplayName("It should return a 401 Unauthorized because person was not found")
    @WithAnonymousUser
    public void readOneShouldGet401UnauthorizedBecausePersonWasNotFound() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 403 Forbidden because person doesn't have the 'REGULAR_USER' authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void readOneItShouldReturn403ForbiddenBecausePersonDoesntHaveRegularUserAuthority() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should get a 200 OK because we found a category by id successfully")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void itShouldGet200OkBecauseWeFoundACategoryByIdSuccessfully() throws Exception{
        //arrange
        Long categoryId = 1l;
        CategoryReadOneDto expectedResult = new CategoryReadOneDto();
        expectedResult.setId(categoryId);
        expectedResult.setName("work");

        Mockito.when(this.categoryService.readOneCategory(categoryId)).thenReturn(expectedResult);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedResult)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(expectedResult.getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedResult.getName()))
        .andDo(MockMvcResultHandlers.print());
    }


    // -------------- UPDATE -----------------
    @Test
    @DisplayName("It should return a 401 Unauthorized because person was not found")
    @WithAnonymousUser
    public void updateShouldGet401UnauthorizedBecausePersonWasNotFound() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 403 Forbidden because person doesn't have the 'REGULAR_USER' authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void updateItShouldReturn403ForbiddenBecausePersonDoesntHaveRegularUserAuthority() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .get("/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should return a 200 OK because we updated it successfully")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void itShouldReturn200OKBecauseWeUpdatedItSuccessfuly() throws Exception{
        //arrange
        Long categoryId = 1l;
        CategoryCreateDto categoryDto = new CategoryCreateDto("work");

        List<CategoryWithIdNameAndOwnerIdDto> expectedList = List.of(
            new CategoryWithIdNameAndOwnerIdDto(categoryId, "work")
        );
        Mockito.when(this.categoryService.updateCategory(Mockito.anyLong(), Mockito.any(CategoryCreateDto.class))).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .put("/v1/api/category/{id}", categoryId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objMapper.writeValueAsString(categoryDto))
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedList)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedList.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(expectedList.get(0).getName()))
        .andDo(MockMvcResultHandlers.print());
    }

    
    // -------------- DELETE -----------------
    @Test
    @DisplayName("It should get a 401 Unauthorized because person was not found")
    @WithAnonymousUser
    public void deleteItShouldGet401UnauthorizedBecausePersonWasNotFound() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isUnauthorized())
        .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("It should get a 403 Forbidden because person doesn't have the 'REGULAR_USER' authority")
    @WithMockUser(authorities = {"notTheAuthorityWeWant"})
    public void deleteItShouldGet403ForbiddenBecausePersonDoesntHaveTheRegularUserAuthority() throws Exception{
        //arrange
        Long categoryId = 1l;
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    @DisplayName("It should get a 200 OK because category was deleted successfully")
    @WithMockUser(authorities = {"REGULAR_USER"})
    public void deleteItShouldGet200OkBecauseCategoryWasDeletedSuccessFully() throws Exception{
        //arrange
        Long categoryId = 1l;
        List<CategoryWithIdNameAndOwnerIdDto> expectedList = List.of(new CategoryWithIdNameAndOwnerIdDto(1l, "work"));

        Mockito.when(this.categoryService.deleteCategoryById(Mockito.anyLong())).thenReturn(expectedList);
        //act
        this.mockMvc.perform( MockMvcRequestBuilders
            .delete("/v1/api/category/{id}", categoryId)
        )
        //assert
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(this.objMapper.writeValueAsString(expectedList)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(expectedList.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(expectedList.get(0).getName()))
        .andDo(MockMvcResultHandlers.print());
    }
}