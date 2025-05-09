package com.example.alphasolutions.model;

public class Employee {

    private int empID;
    private String firstName;
    private String lastName;
    private String mail;
    private String password;
    //private Skill skill;  //har egen constructor - nice-to-have
    private Role role;


    public Employee(int empID, String firstName, String lastName, String mail, String password, Role role) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    //Empty constructor to add emp
    public Employee() {

    }

    //Constructor with SKILL included
    /*public Employee(int empID, String firstName, String lastName, String mail, String password, Skill skill, Role role) {
        this.empID = empID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mail = mail;
        this.password = password;
        this.skill = skill;
        this.role = role;
    }
     */


    public int getEmpID() {
        return empID;
    }

    //TODO if we need to set empID to a task
    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getFirstName() {
        return firstName;
    }

    //Maybe not needed
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    //Maybe not needed
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }
     */

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empID=" + empID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }
}
