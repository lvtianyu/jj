package com.liver_cloud.entity;

public interface Entity<IDClass extends java.io.Serializable> {

    public IDClass getId();

    public void setId(IDClass id);
}
