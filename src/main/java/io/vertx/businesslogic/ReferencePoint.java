package io.vertx.businesslogic;

import java.util.concurrent.atomic.AtomicInteger;

public class ReferencePoint {
  private static final AtomicInteger COUNTER = new AtomicInteger();

  private final int id;
  private String name;
  private String latitude;
  private String longitude;

  public ReferencePoint(){
    this.id = COUNTER.getAndIncrement();
  }

  public ReferencePoint(String name, String latitude, String longitude){
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }
}
