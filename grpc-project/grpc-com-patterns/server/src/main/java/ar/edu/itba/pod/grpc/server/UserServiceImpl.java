package ar.edu.itba.pod.grpc.server;

import ar.edu.itba.pod.grpc.user.*;
import ar.edu.itba.pod.grpc.user.*;
import ar.edu.itba.pod.grpc.user.LoginInformation;
import ar.edu.itba.pod.grpc.user.User;
import ar.edu.itba.pod.grpc.user.UserRoles;
import ar.edu.itba.pod.grpc.user.UserServiceGrpc;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void doLogin(LoginInformation request, StreamObserver<User> responseObserver) {
        User user = User.newBuilder()
                    .setId(1)
                .setUserName("pina")
                .setDisplayName("pina display name")
                .setStatus(ar.edu.itba.pod.grpc.user.AccountStatus.ACCOUNT_STATUS_ACTIVE)
                .setPreferences(0, "preferencia")
                .build();
        responseObserver.onNext(user);
        responseObserver.onCompleted();
    }

    @Override
    public void getRoles(User request, StreamObserver<UserRoles> responseObserver) {
        UserRoles userRoles = UserRoles.newBuilder().
                putRolesBySite("firstRole", ar.edu.itba.pod.grpc.user.Role.ADMIN).
                build();
        responseObserver.onNext(userRoles);
        responseObserver.onCompleted();
    }
}
