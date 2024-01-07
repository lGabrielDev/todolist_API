package br.com.lGabrielDev.todolist_project.models.person.dtos;

public class PersonCreateDto {

    //attributes
    private String username;
    private String password;

    //constructors
    public PersonCreateDto(){}

    public PersonCreateDto(String username, String password){
        this.username = username;
        this.password = password;
    }

    //getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //toString()
    @Override
    public String toString(){
        return
            String.format(
                "username: %s\n" +
                "password: %s\n", this.username, this.password
            );
     }
}