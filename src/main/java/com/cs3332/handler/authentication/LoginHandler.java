package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.auth.LoginPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.LoginSuccessResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.UserInformation;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class LoginHandler extends AbstractBodyHandler<LoginPayload> {
    public LoginHandler(Server server) {
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

        Response response = server.getDataManager().getAuthenticationSource().verifyUser(
                payload.getUsername(), payload.getPassword());

        if(response.getState()){
            UserInformation information = server.getDataManager().getAuthenticationSource().getUserInformation(payload.getUsername());
            return new ServerResponse(ResponseCode.OK, new LoginSuccessResponse(
                    dataManager.createToken(payload.getUsername()),
                    information.getName(),
                    information.getEmail(),
                    information.getPhone(),
                    information.getDateOfBirth(),
                    information.getGender(),
                    information.getRoles()
            ));
        }
        return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse(response.getResponse()));
    }
}
