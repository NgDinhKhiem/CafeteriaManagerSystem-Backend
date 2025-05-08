package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.*;
import com.cs3332.core.payload.object.auth.TargetUsernamePayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.auth.UserInformationResponse;
import com.cs3332.data.object.auth.UserInformation;
import com.cs3332.handler.constructor.AbstractBodyHandler;
import com.cs3332.handler.constructor.AbstractHandler;

public class UserInformationHandler extends AbstractHandler {
    private String token;
    @Param
    private String username;
    public UserInformationHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        if (username == null || username.isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Username is required."));
        }
        if(
                dataManager.getRole(token).contains(Role.ADMIN)&&
                !dataManager.getRole(token).contains(Role.MANAGER)&&
                !dataManager.isValidToken(token,username)){
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }

        UserInformation response = server.getDataManager().getAuthenticationSource().getUserInformation(
                username
        );

        if(response!=null)
            return new ServerResponse(ResponseCode.OK, new UserInformationResponse(
                    response.getUsername(),
                    response.getName(),
                    response.getEmail(),
                    response.getPhone(),
                    response.getDateOfBirth(),
                    response.getGender(),
                    response.getRoles()
            ));
        else return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Fail to gather information about this user!"));
    }
}
