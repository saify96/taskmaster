package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Location type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Locations")
public final class Location implements Model {
  public static final QueryField ID = field("Location", "id");
  public static final QueryField LON = field("Location", "lon");
  public static final QueryField LAT = field("Location", "lat");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Float", isRequired = true) Double lon;
  private final @ModelField(targetType="Float", isRequired = true) Double lat;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public Double getLon() {
      return lon;
  }
  
  public Double getLat() {
      return lat;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Location(String id, Double lon, Double lat) {
    this.id = id;
    this.lon = lon;
    this.lat = lat;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Location location = (Location) obj;
      return ObjectsCompat.equals(getId(), location.getId()) &&
              ObjectsCompat.equals(getLon(), location.getLon()) &&
              ObjectsCompat.equals(getLat(), location.getLat()) &&
              ObjectsCompat.equals(getCreatedAt(), location.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), location.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLon())
      .append(getLat())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Location {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("lon=" + String.valueOf(getLon()) + ", ")
      .append("lat=" + String.valueOf(getLat()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static LonStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   * @throws IllegalArgumentException Checks that ID is in the proper format
   */
  public static Location justId(String id) {
    try {
      UUID.fromString(id); // Check that ID is in the UUID format - if not an exception is thrown
    } catch (Exception exception) {
      throw new IllegalArgumentException(
              "Model IDs must be unique in the format of UUID. This method is for creating instances " +
              "of an existing object with only its ID field for sending as a mutation parameter. When " +
              "creating a new object, use the standard builder method and leave the ID field blank."
      );
    }
    return new Location(
      id,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      lon,
      lat);
  }
  public interface LonStep {
    LatStep lon(Double lon);
  }
  

  public interface LatStep {
    BuildStep lat(Double lat);
  }
  

  public interface BuildStep {
    Location build();
    BuildStep id(String id) throws IllegalArgumentException;
  }
  

  public static class Builder implements LonStep, LatStep, BuildStep {
    private String id;
    private Double lon;
    private Double lat;
    @Override
     public Location build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Location(
          id,
          lon,
          lat);
    }
    
    @Override
     public LatStep lon(Double lon) {
        Objects.requireNonNull(lon);
        this.lon = lon;
        return this;
    }
    
    @Override
     public BuildStep lat(Double lat) {
        Objects.requireNonNull(lat);
        this.lat = lat;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Double lon, Double lat) {
      super.id(id);
      super.lon(lon)
        .lat(lat);
    }
    
    @Override
     public CopyOfBuilder lon(Double lon) {
      return (CopyOfBuilder) super.lon(lon);
    }
    
    @Override
     public CopyOfBuilder lat(Double lat) {
      return (CopyOfBuilder) super.lat(lat);
    }
  }
  
}
