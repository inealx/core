package id.ineal.util.Response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import id.ineal.util.__;
import lombok.NoArgsConstructor;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@NoArgsConstructor
@ToString
@JsonInclude(Include.NON_NULL) 
public class Response {
    
    private Integer status;
    private String message;
    private Object data;

    public static Response createStatus(Integer status) {
        return new Response().setStatus(status);
    }

    public Response setStatus(int status) {
        this.status = status;
        return this;
    }
    
    public Response message(String message) {
        this.message = message;
        return this;
    }
    public Response message(String message,Object... params) {
        this.message = String.format(message, params);
        return this;
    }

    public Response data(Object data) {
        this.data = data;
        return this;
    }
    public Response data(Object data,Class<?> ret) {
        this.data = data;
        return this;
    }
    /*===========================
    *			GETTER
    *============================*/
    public Integer getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public JsonNode getJson() {
        return __.toJson(this.data);
    }
    
    @JsonIgnore
    public Map<String,Object> getMap() {
        return __.toMap(this.data);
    }

    @JsonIgnore
    public Object getData() {
        return this.data;
    }

    @JsonIgnore
    public <T> T getData(Class<T> ret) {
        return __.toObject(this.data, ret);
    }
}
