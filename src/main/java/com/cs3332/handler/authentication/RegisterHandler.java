package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.auth.AccountRegisterPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class RegisterHandler extends AbstractBodyHandler<AccountRegisterPayload> {
    public RegisterHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getUsername() == null || payload.getUsername().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Username is required."));
        }
        if (payload.getPassword() == null || payload.getPassword().length() < 8) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Password must be at least 8 characters long."));
        }
        if (payload.getName() == null || payload.getName().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Name is required."));
        }
        if (payload.getEmail() == null || !payload.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid email format."));
        }
        if (payload.getPhone() != null && !payload.getPhone().matches("^\\+?[0-9]{7,15}$")) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid phone number format."));
        }

        if (payload.getGender() == null || (!payload.getGender().equalsIgnoreCase("male") &&
                !payload.getGender().equalsIgnoreCase("female") &&
                !payload.getGender().equalsIgnoreCase("other"))) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Invalid gender value."));
        }
        if (payload.getRoles() == null || payload.getRoles().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("At least one role must be assigned."));
        }

        Response response = server.getDataManager().getAuthenticationSource().createUser(
                new UserAuthInformation(
                        this.payload.getUsername(),
                        this.payload.getPassword()
                ),
                new UserInformation(
                        this.payload.getUsername(),
                        this.payload.getName(),
                        this.payload.getEmail(),
                        this.payload.getPhone(),
                        this.payload.getDateOfBirth(),
                        this.payload.getGender(),
                        this.payload.getRoles()
                )
        );

        if(response.getState())
            return new ServerResponse(ResponseCode.CREATED, new TextResponse("Register Successful"));
        else return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR, new ErrorResponse(response.getResponse()));
    }
}
