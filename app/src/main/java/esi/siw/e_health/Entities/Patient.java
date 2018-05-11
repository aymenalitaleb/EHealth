package esi.siw.e_health.Entities;

import java.util.Date;

public class Patient {

    private int idPatient;
    private String nom;
    private String prenom;
    private String sexe;
    private int Age;
    private Date Date_Naissance;
    private String Lieut_Naissance;
    private String telephone;
    private String NumeroAS;
    private String Email;
    private String Avatar;

    public int getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient = idPatient;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public Date getDate_Naissance() {
        return Date_Naissance;
    }

    public void setDate_Naissance(Date date_Naissance) {
        Date_Naissance = date_Naissance;
    }

    public String getLieut_Naissance() {
        return Lieut_Naissance;
    }

    public void setLieut_Naissance(String lieut_Naissance) {
        Lieut_Naissance = lieut_Naissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getNumeroAS() {
        return NumeroAS;
    }

    public void setNumeroAS(String numeroAS) {
        NumeroAS = numeroAS;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }
}
