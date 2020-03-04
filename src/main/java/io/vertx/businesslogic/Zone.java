package io.vertx.businesslogic;

import java.util.concurrent.atomic.AtomicInteger;

public class Zone {
  private static final AtomicInteger COUNTER = new AtomicInteger();

  private final int id;
  private String name;
  private String description;

  public Zone(String name, String description){
    this.id = COUNTER.getAndIncrement();
    this.name = name;
    this.description = description;
  }

  public Zone(){
    this.id = COUNTER.getAndIncrement();
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getId() {
    return id;
  }
}
