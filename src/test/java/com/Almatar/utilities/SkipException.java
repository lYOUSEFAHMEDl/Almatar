package com.Almatar.utilities;

import static com.Almatar.constants.GeneralConstants.SKIPPED_MESSAGE;

public class SkipException extends org.testng.SkipException {
    public SkipException(Exception exception) {
        super(SKIPPED_MESSAGE);
        Log.error(SKIPPED_MESSAGE, exception);
    }

    public SkipException(String skipMessage) {
        super(skipMessage);
    }

}