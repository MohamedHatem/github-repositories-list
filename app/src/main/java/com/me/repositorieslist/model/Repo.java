package com.me.repositorieslist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos")
public class Repo {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("full_name")
    @Expose
    public String fullName;


    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("forks")
    @Expose
    public Integer forks;

    @SerializedName("stargazers_count")
    @Expose
    public Integer stars;


    @SerializedName("html_url")
    public String url;


    @SerializedName("language")
    public String language;

    @Override
    public String toString() {
        return "id:" + id + " , name: " + name + " , fullname: "
                + fullName + " , forks: " + forks + " , stars: " + stars;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Repo or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Repo)) {
            return false;
        }

        // typecast o to Repo so that we can compare data members
        Repo c = (Repo) o;

        // Compare the data members and return accordingly
        return id.equals(c.id) && fullName.compareTo(c.fullName) == 0 &&
                description.compareTo(c.description) ==0 && forks.equals(forks) &&
                stars.equals(stars) && url.equals(url) && language.equals(language);

    }
}
