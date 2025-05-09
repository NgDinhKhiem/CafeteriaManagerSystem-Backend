package com.cs3332.data.constructor;

import com.cs3332.core.utils.Response;
import com.cs3332.data.object.auth.UserAuthInformation;
import com.cs3332.data.object.auth.UserInformation;

public interface AuthenticationSource extends DBSource{
    UserInformation getUserInformation(String username);
    Response verifyUser(String username, String password);
    Response updateUserInformation(String username, UserInformation userInformation);
    Response availableUsername(String username);
    Response createUser(UserAuthInformation auth, UserInformation user);
    Response deleteUser(String username);
    Response updateUserPassword(String username, String oldPassword, String newPassword);
}
