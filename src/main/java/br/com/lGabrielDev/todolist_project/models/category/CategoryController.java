package br.com.lGabrielDev.todolist_project.models.category;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.lGabrielDev.todolist_project.exceptions.DefaultExceptionBody;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryCreateDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryReadOneDto;
import br.com.lGabrielDev.todolist_project.models.category.dtos.CategoryWithIdNameAndOwnerIdDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/v1/api")
public class CategoryController {
    
    //injected attributes
    @Autowired
    private CategoryService categoryService;


    // ------------------------- CREATE -------------------------
    @Operation(
        tags = {"category"},
        summary = "create a category",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "You only have to inform the 'category name'. Remember, your categories must to be unique. You cannot have categories with the same name."
        ),
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "CREATED - category successfully",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(implementation = CategoryWithIdNameAndOwnerIdDto.class)
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong",
                content = @Content() //no content
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT - category name is wrong.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)    
                )
            )
        }
    )
    @PostMapping("/category")
    public ResponseEntity<List<CategoryWithIdNameAndOwnerIdDto>> createCategory(@RequestBody CategoryCreateDto categoryDto){
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.categoryService.createCategory(categoryDto));
    }

    // ------------------------- READ -------------------------
    // ----- read all -----
    @Operation(
        tags = {"category"},
        summary = "read all categories",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(
                            implementation = CategoryWithIdNameAndOwnerIdDto.class
                        )
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong",
                content = @Content() //no content
            )
        }
    )
    @GetMapping("/category")
    public ResponseEntity<List<CategoryWithIdNameAndOwnerIdDto>> readAll(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.categoryService.readAllCategories());
    }

    // ----- read one -----
    @Operation(
        tags = {"category"},
        summary = "read a particular category",
        description = "The authenticated person can only access its own categories.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = CategoryReadOneDto.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong",
                content = @Content() //no content
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND - category #ID does not exists in our database",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)
                )
            ),
            @ApiResponse(
                responseCode = "406",
                description = "NOT_ACCEPTABLE - category belongs to another person. The authenticated person cannot access others persons categories.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)
                )
            )
        }
    )
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryReadOneDto> readOneCategory(@PathVariable(value = "id") Long categoryId){
         return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.categoryService.readOneCategory(categoryId));
    }

    // ------------------------- UPDATE -------------------------
    @Operation(
        tags = {"category"},
        summary = "update a particular category",
        description = "The authenticated person can only update its own categories",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "You only have to inform the 'category name'."
        ),
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK - category updated successfully.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(
                            implementation = CategoryWithIdNameAndOwnerIdDto.class
                        )
                    )
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong.",
                content = @Content() //no content
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND - category #ID does not exists in our database.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)    
                )
            ),
            @ApiResponse(
                responseCode = "406",
                description = "NOT_ACCEPTABLE - category belongs to another person. The authenticated person cannot update others persons categories.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)    
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT - category name is wrong.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)    
                )
            )
        }
    )
    @PutMapping("/category/{id}")
    public ResponseEntity<List<CategoryWithIdNameAndOwnerIdDto>> updateCategory(@PathVariable(value = "id") Long categoryId, @RequestBody CategoryCreateDto categoryDto){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.categoryService.updateCategory(categoryId, categoryDto));
    }

    // ------------------------- DELETE -------------------------
    @Operation(
        tags = {"category"},
        summary = "delete a particular category",
        description = "The authenticated person can only delete its own categories.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK - category deleted successfully.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(
                            implementation = CategoryWithIdNameAndOwnerIdDto.class
                        )
                    )     
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong.",
                content = @Content() //no content
            ),
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND - category #ID does not exists in our database.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = DefaultExceptionBody.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "406",
                description = "NOT_ACCEPTABLE - category belongs to another person. The authenticated person cannot delete others persons categories.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = DefaultExceptionBody.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "417",
                description = "EXPECTATION_FAILED - cannot delete a category that contains tasks on it.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = DefaultExceptionBody.class
                    )
                )
            ) 
        }
    )
    @DeleteMapping("/category/{id}")
    public ResponseEntity<List<CategoryWithIdNameAndOwnerIdDto>> deleteCategory(@PathVariable(value = "id") Long categoryId){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.categoryService.deleteCategoryById(categoryId));
    }
}