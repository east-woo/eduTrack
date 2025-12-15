package com.eastwoo.toy.edutrack.auth.notification.port;

public interface NotificationPort {

    void sendInviteEmail(String email, String token);

}