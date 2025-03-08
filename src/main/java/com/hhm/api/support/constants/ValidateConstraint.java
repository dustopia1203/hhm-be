package com.hhm.api.support.constants;

public interface ValidateConstraint {
    public static final class LENGTH {
        public static final int CODE_MAX_LENGTH = 36;
        public static final int USERNAME_MAX_LENGTH = 50;
        public static final int NAME_MAX_LENGTH = 100;
        public static final int EMAIL_MAX_LENGTH = 50;
        public static final int PHONE_MAX_LENGTH = 20;
        public static final int ADDRESS_MAX_LENGTH = 200;
        public static final int PASSWORD_MIN_LENGTH = 6;
        public static final int PASSWORD_MAX_LENGTH = 50;
    }
}
