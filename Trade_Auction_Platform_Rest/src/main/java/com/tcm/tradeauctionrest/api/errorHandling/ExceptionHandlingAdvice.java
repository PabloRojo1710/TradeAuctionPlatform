package com.tcm.tradeauctionrest.api.errorHandling;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tcm.tradeauctionrest.application.exceptions.InvalidParamException;
import com.tcm.tradeauctionrest.application.exceptions.NotEnoughBalanceException;
import com.tcm.tradeauctionrest.application.exceptions.NotPermissionException;
import com.tcm.tradeauctionrest.application.exceptions.PriceTooLowException;



@ControllerAdvice
public class ExceptionHandlingAdvice {

    @ResponseBody
    @ExceptionHandler({NotEnoughBalanceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Violation notEnoughBalance(Exception ex) {
        return new Violation("Not enough balance", ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({InvalidParamException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Violation invalidParam(Exception ex) {
        return new Violation("Invalid parameter", ex.getMessage());
    }
    
    @ResponseBody
    @ExceptionHandler({NotPermissionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Violation notPermission(Exception ex) {
        return new Violation("Not permission", ex.getMessage());
    }
    
    @ResponseBody
    @ExceptionHandler({PriceTooLowException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Violation priceTooLow(Exception ex) {
        return new Violation("Price too low", ex.getMessage());
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (@SuppressWarnings("rawtypes") ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation("Wrong parameter input", violation.getPropertyPath().toString() + ": " + violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation("Wrong parameter input", fieldError.getField()+ ": "+ fieldError.getDefaultMessage()));
        }
        return error;
    }
    
    public class ValidationErrorResponse {

        private List<Violation> violations = new ArrayList<>();

        public ValidationErrorResponse() {
        }

        public List<Violation> getViolations() {
            return violations;
        }
    }
    
    public class Violation{
    	
    	private String title;
    	private String message;
    	
		public Violation(String title, String message) {
			this.title = title;
			this.message = message;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
    }
}
