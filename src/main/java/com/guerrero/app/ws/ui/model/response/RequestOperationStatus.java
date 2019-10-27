package com.guerrero.app.ws.ui.model.response;

public enum RequestOperationStatus {


  ERROR("ERROR"),
  SUCCESS("ERROR");


  private String operationStatus;

  RequestOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }


  public String getOperationStatus() {
    return operationStatus;
  }

  public void setOperationStatus(String operationStatus) {
    this.operationStatus = operationStatus;
  }
}
