package bd.edu.seu.biye_shaddi.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class User {
    // Basic Information
    @Id
    private String userId;
    private String name;
    private Integer age;
    private String gender;
    private String location;
    private String education;
    private String religion;
    private String height; // can be string to allow units or formatted height
    private String relationshipStatus;
    private String bio;
    // Preferences
    private Integer preferredAgeMin;
    private Integer preferredAgeMax;
    private String preferredGender;
    private Integer preferredHeightMin;
    private Integer preferredHeightMax;

    // Interests
    private List<String> interests;
    // Profile picture URL or file path
    private String profilePictureUrl;

    public User(Integer age, String bio, String education, String gender, String height, List<String> interests, String location, String name, Integer preferredAgeMax, Integer preferredAgeMin, String preferredGender, Integer preferredHeightMax, Integer preferredHeightMin, String profilePictureUrl, String relationshipStatus, String religion, String userId) {
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
        this.profilePictureUrl = profilePictureUrl;
        this.relationshipStatus = relationshipStatus;
        this.religion = religion;
        this.userId = userId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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

    public Integer getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(Integer preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public Integer getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(Integer preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public Integer getPreferredHeightMax() {
        return preferredHeightMax;
    }

    public void setPreferredHeightMax(Integer preferredHeightMax) {
        this.preferredHeightMax = preferredHeightMax;
    }

    public Integer getPreferredHeightMin() {
        return preferredHeightMin;
    }

    public void setPreferredHeightMin(Integer preferredHeightMin) {
        this.preferredHeightMin = preferredHeightMin;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    /*public User(Integer age, String bio, String education, String gender, String height, List<String> interests, String location, String name, Integer preferredAgeMax, Integer preferredAgeMin, String preferredGender, Integer preferredHeightMax, Integer preferredHeightMin, String profilePictureUrl, String relationshipStatus, String religion) {
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
        this.profilePictureUrl = profilePictureUrl;
        this.relationshipStatus = relationshipStatus;
        this.religion = religion;
    }
    public User() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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

    public Integer getPreferredAgeMax() {
        return preferredAgeMax;
    }

    public void setPreferredAgeMax(Integer preferredAgeMax) {
        this.preferredAgeMax = preferredAgeMax;
    }

    public Integer getPreferredAgeMin() {
        return preferredAgeMin;
    }

    public void setPreferredAgeMin(Integer preferredAgeMin) {
        this.preferredAgeMin = preferredAgeMin;
    }

    public String getPreferredGender() {
        return preferredGender;
    }

    public void setPreferredGender(String preferredGender) {
        this.preferredGender = preferredGender;
    }

    public Integer getPreferredHeightMax() {
        return preferredHeightMax;
    }

    public void setPreferredHeightMax(Integer preferredHeightMax) {
        this.preferredHeightMax = preferredHeightMax;
    }

    public Integer getPreferredHeightMin() {
        return preferredHeightMin;
    }

    public void setPreferredHeightMin(Integer preferredHeightMin) {
        this.preferredHeightMin = preferredHeightMin;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
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
    }*/
}
