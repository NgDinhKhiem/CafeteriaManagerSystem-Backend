package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.auth.AccountInfoUpdatePayload;
import com.cs3332.core.payload.object.auth.TargetUsernamePayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.response.object.UserInformationResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.UserInformation;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class UserInformationHandler extends AbstractBodyHandler<TargetUsernamePayload> {
    private String token;
    public UserInformationHandler(Server server) {
        super(server, RequestMethod.GET);
    }

    @Override
    protected ServerResponse resolve() {
        if (payload.getUsername() == null || payload.getUsername().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Username is required."));
        }
        if(
                dataManager.getRole(token).contains(Role.ADMIN)&&
                !dataManager.getRole(token).contains(Role.MANAGER)&&
                !dataManager.isValidToken(token,payload.getUsername())){
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }

        UserInformation response = server.getDataManager().getAuthenticationSource().getUserInformation(
                payload.getUsername()
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
