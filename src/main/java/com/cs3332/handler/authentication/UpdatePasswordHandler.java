package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.auth.LoginPayload;
import com.cs3332.core.payload.object.auth.UpdatePasswordPayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.LoginSuccessResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.data.object.UserInformation;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class UpdatePasswordHandler extends AbstractBodyHandler<UpdatePasswordPayload> {
    private String token;
    public UpdatePasswordHandler(Server server) {
        super(server, RequestMethod.POST);
    }

    @Override
    protected ServerResponse resolve() {
        if(!dataManager.getRole(token).contains(Role.ADMIN)&&!dataManager.getRole(token).contains(Role.MANAGER)){
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }

        if (payload.getUsername() == null || payload.getUsername().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Username is required."));
        }
        if (payload.getPassword() == null || payload.getPassword().length() < 8) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Password must be at least 8 characters long."));
        }

        if(payload.getOld_password().equals(payload.getPassword())){
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Password must be different!"));
        }

        Response response = server.getDataManager().getAuthenticationSource().updateUserPassword(
                payload.getUsername(), payload.getOld_password(), payload.getPassword());

        if(response.getState()){
            return new ServerResponse(ResponseCode.OK, new TextResponse("success"));
        }
        return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse(response.getResponse()));
    }
}
