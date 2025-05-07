package com.cs3332.handler.authentication;

import com.cs3332.Server;
import com.cs3332.core.object.RequestMethod;
import com.cs3332.core.object.ResponseCode;
import com.cs3332.core.object.Role;
import com.cs3332.core.object.ServerResponse;
import com.cs3332.core.payload.object.auth.TargetUsernamePayload;
import com.cs3332.core.response.object.ErrorResponse;
import com.cs3332.core.response.object.TextResponse;
import com.cs3332.core.utils.Response;
import com.cs3332.handler.constructor.AbstractBodyHandler;

public class DeleteUserHandler extends AbstractBodyHandler<TargetUsernamePayload> {
    private String token;
    public DeleteUserHandler(Server server) {
        super(server, RequestMethod.DELETE);
    }

    @Override
    protected ServerResponse resolve() {
        if(!dataManager.getRole(token).contains(Role.ADMIN)&&!dataManager.getRole(token).contains(Role.MANAGER)){
            return new ServerResponse(ResponseCode.UNAUTHORIZED, new ErrorResponse("You do not have permission to issue this action!"));
        }

        if (payload.getUsername() == null || payload.getUsername().isEmpty()) {
            return new ServerResponse(ResponseCode.BAD_REQUEST, new ErrorResponse("Username is required."));
        }

        Response response = server.getDataManager().getAuthenticationSource().deleteUser(
                payload.getUsername()
        );

        if(response.getState())
            return new ServerResponse(ResponseCode.OK, new TextResponse("Successful"));
        else return new ServerResponse(ResponseCode.UNPROCESSABLE_ENTITY, new ErrorResponse(response.getResponse()));
    }
}
