package com.guerrero.app.ws.ui.model.response;

public enum RequestOperationName {

  DELETE("DELETE");


  private String operationName;

  RequestOperationName(String operationName) {
    this.operationName = operationName;
  }


  public String getOperationName() {
    return operationName;
  }

  public void setOperationName(String operationName) {
    this.operationName = operationName;
  }
}



