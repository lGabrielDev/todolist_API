package br.com.lGabrielDev.todolist_project.models.person;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.lGabrielDev.todolist_project.exceptions.DefaultExceptionBody;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonCreateDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonFullDto;
import br.com.lGabrielDev.todolist_project.models.person.dtos.PersonWithoutTasksDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@RestController
@RequestMapping("/v1/api")
public class PersonController {
    
    //injected attributes
    @Autowired
    private PersonService personService;

    // ----------------------------------- CREATE -----------------------------------
    @Operation(
        tags = {"person"},
        summary = "create a new person",
        description = "You don't need to be authenticated to create a person. Anyone can do that.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "CREATED - successfully.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = PersonFullDto.class
                    )     
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "CONFLICT - username or password is wrong.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)
                )
            )
        } 
    )
    @PostMapping("/person")
    public ResponseEntity<PersonFullDto> createPerson(@RequestBody PersonCreateDto personDto){
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(this.personService.createPerson(personDto));
    }

    // ------------------------- Give the ADMIN permission -------------------------
    @Operation(
        tags = {"admin"},
        summary = "give the admin authority to a regular person",
        description = "Only 'regular person' can receive that authority.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK - admin authority given sucessfully.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = PersonFullDto.class
                    )     
                )
            ), 
            @ApiResponse(
                responseCode = "401",
                description = "UNAUTHORIZED - username and password are wrong.",
                content = @Content() //no content
            ), 
            @ApiResponse(
                responseCode = "403",
                description = "FORBIDDEN - authenticated person does not have the admin authority.",
                content = @Content() //no content
            ), 
            @ApiResponse(
                responseCode = "404",
                description = "NOT_FOUND - person does not exists in our database.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)
                )
            ),
            @ApiResponse(
                responseCode = "417",
                description = "EXPECTATION_FAILED - person already has the admin authority.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DefaultExceptionBody.class)
                )
            )
        }
    )
    @PutMapping("/person/give-admin-permission/{id}")
    public ResponseEntity<PersonFullDto> giveAdminPermission(@PathVariable(value = "id") Long id){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.personService.giveAdminPermission(id));   
    }
    
    // ----------------------------------- READ -----------------------------------
    @Operation(
        tags = {"admin"},
        summary = "read all persons",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "List of all persons.",
                content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                        schema = @Schema(
                            implementation = PersonWithoutTasksDto.class
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
                responseCode = "403",
                description = "FORBIDDEN - authenticated person does not have the admin authority.",
                content = @Content() //no content
            ), 
        }
    )
    @GetMapping("/person")
    public ResponseEntity<List<PersonWithoutTasksDto>> readAllPersons(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(this.personService.readAllPersons());
    }
}