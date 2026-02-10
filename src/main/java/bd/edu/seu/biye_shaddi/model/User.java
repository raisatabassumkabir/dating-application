package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public class User {

    @Id
    private String id;
    private String name;
    private String emailId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private int age;
    private String gender;
    private String location;
    private String education;
    private String religion;
    private String profession;
    private String height; // can be string to allow units or formatted height
    private String relationshipStatus;
    private String bio;
    private String monthlyIncome;
    private String maritalStatus;

    // Family & Background
    private String familyStatus; // e.g., Middle class, Upper middle
    private String familyType; // e.g., Nuclear, Joint
    private String fatherStatus;
    private String motherStatus;
    private int numberOfSiblings;

    // Preferences
    private int preferredAgeMin;
    private int preferredAgeMax;
    private String preferredGender;
    private String preferredHeightMin;
    private String preferredHeightMax;
    private String preferredReligion;
    private List<String> preferredProfession;
    private String preferredLocation;
    private String preferredMaritalStatus;

    // ... (rest of the file as needed, but let's focus on the change)

    // Interests
    private List<String> interests;
    // Profile picture URL or file path
    private String profilePictureUrl;
    private Double compatibilityScore; // Stores compatibility percentage with current user

    private ContactDetails contactDetails;

    public User() {
    }

    public User(String id, String emailId, Date dateOfBirth, int age, String bio, String education, String gender,
            String height, List<String> interests, String location, String name, Integer preferredAgeMax,
            Integer preferredAgeMin, String preferredGender, String preferredHeightMax, String preferredHeightMin,
            String profession, String profilePictureUrl, String relationshipStatus, String religion) {
        this.id = id;
        this.age = age;
        this.bio = bio;
        this.education = education;
        this.gender = gender;
        this.height = height;
        this.interests = interests;
        this.location = location;
        this.name = name;
        this.preferredAgeMax = preferredAgeMax;
        this.preferredAgeMin = preferredAgeMin;
        this.preferredGender = preferredGender;
        this.preferredHeightMax = preferredHeightMax;
        this.preferredHeightMin = preferredHeightMin;
        this.profession = profession;
        this.profilePictureUrl = profilePictureUrl;
        this.relationshipStatus = relationshipStatus;
        this.religion = religion;
        this.emailId = emailId;
        this.dateOfBirth = dateOfBirth;

    }

    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetails contactDetails) {
        this.contactDetails = contactDetails;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(int preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public int getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(int preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public String getPreferredHeightMax() {
        return preferredHeightMax;
    }

    public void setPreferredHeightMax(String preferredHeightMax) {
        this.preferredHeightMax = preferredHeightMax;
    }

    public String getPreferredHeightMin() {
        return preferredHeightMin;
    }

    public void setPreferredHeightMin(String preferredHeightMin) {
        this.preferredHeightMin = preferredHeightMin;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public Double getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(Double compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }

    public String getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(String relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(String familyStatus) {
        this.familyStatus = familyStatus;
    }

    public String getFamilyType() {
        return familyType;
    }

    public void setFamilyType(String familyType) {
        this.familyType = familyType;
    }

    public String getFatherStatus() {
        return fatherStatus;
    }

    public void setFatherStatus(String fatherStatus) {
        this.fatherStatus = fatherStatus;
    }

    public String getMotherStatus() {
        return motherStatus;
    }

    public void setMotherStatus(String motherStatus) {
        this.motherStatus = motherStatus;
    }

    public int getNumberOfSiblings() {
        return numberOfSiblings;
    }

    public void setNumberOfSiblings(int numberOfSiblings) {
        this.numberOfSiblings = numberOfSiblings;
    }

    public String getPreferredReligion() {
        return preferredReligion;
    }

    public void setPreferredReligion(String preferredReligion) {
        this.preferredReligion = preferredReligion;
    }

    public List<String> getPreferredProfession() {
        return preferredProfession;
    }

    public void setPreferredProfession(List<String> preferredProfession) {
        this.preferredProfession = preferredProfession;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public String getPreferredMaritalStatus() {
        return preferredMaritalStatus;
    }

    public void setPreferredMaritalStatus(String preferredMaritalStatus) {
        this.preferredMaritalStatus = preferredMaritalStatus;
    }
}
